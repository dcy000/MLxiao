package com.gcml.auth.model;

import android.content.Context;
import android.text.TextUtils;

import com.gcml.common.AppDelegate;
import com.gcml.common.RetrofitHelper;
import com.gcml.common.RoomHelper;
import com.gcml.common.RxCacheHelper;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.http.ApiException;
import com.gcml.common.user.UserToken;
import com.gcml.common.utils.RxUtils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.ProviderHelper;

public class UserRepository {

    private Context mContext = AppDelegate.INSTANCE.app();


    private UserService mUserService = RetrofitHelper.service(UserService.class);
    private UserProvider userProvider = RxCacheHelper.provider(UserProvider.class);

//    private UserDao mUserDao = RoomHelper.db(UserDb.class, UserDb.class.getName()).userDao();

    public Observable<UserEntity> signUp(String deviceId, String account, String pwd) {
        return mUserService.signUp(deviceId, account, pwd)
                .compose(RxUtils.apiResultTransformer())
                .flatMap(new Function<UserEntity, ObservableSource<UserEntity>>() {
                    @Override
                    public ObservableSource<UserEntity> apply(UserEntity user) throws Exception {
                        return signIn(deviceId, account, pwd)
                                .subscribeOn(Schedulers.io());
                    }
                });
    }

    public Observable<UserEntity> signUpByIdCard(String deviceId, String account, String pwd, String name) {
        return mUserService.signUpByIdCard(deviceId, account, pwd, name)
                .compose(RxUtils.apiResultTransformer())
                .flatMap(new Function<UserEntity, ObservableSource<UserEntity>>() {
                    @Override
                    public ObservableSource<UserEntity> apply(UserEntity user) throws Exception {
                        return signIn(deviceId, account, pwd)
                                .subscribeOn(Schedulers.io());
                    }
                });
    }


    public Observable<UserEntity> signIn(
            String deviceId,
            String userName,
            String pwd) {
        return mUserService.signIn(deviceId, userName, pwd)
                .compose(RxUtils.apiResultTransformer())
                .compose(userTokenTransformer());
    }

    public Observable<UserEntity> signInNoNetWork(String phone) {
        Observable<List<UserEntity>> exists = userProvider.usersLocal(Observable.empty(), new EvictProvider(false))
                .toObservable()
                .onErrorResumeNext(Observable.just(Collections.emptyList()));
        UserEntity user = new UserEntity();
        user.id = phone;
        user.name = phone;
        return exists.map(new Function<List<UserEntity>, List<UserEntity>>() {
            @Override
            public List<UserEntity> apply(List<UserEntity> userEntities) throws Exception {
                ArrayList<UserEntity> entities = new ArrayList<>();
                boolean exist = false;
                for (UserEntity entity : userEntities) {
                    if (!TextUtils.isEmpty(entity.id) && !entity.id.equals(user.id)) {
                        entities.add(entity);
                    } else {
                        exist = true;
                    }
                }
                if (!exist) {
                    entities.add(user);
                }
                return entities;
            }
        }).compose(new ObservableTransformer<List<UserEntity>, List<UserEntity>>() {
            @Override
            public ObservableSource<List<UserEntity>> apply(Observable<List<UserEntity>> upstream) {
                return userProvider.usersLocal(upstream, new EvictProvider(true))
                        .toObservable()
                        .onErrorResumeNext(Observable.just(Collections.emptyList()));
            }
        }).map(new Function<List<UserEntity>, UserEntity>() {
            @Override
            public UserEntity apply(List<UserEntity> userEntities) throws Exception {
                UserSpHelper.setUserId(phone);
                UserSpHelper.setNoNetwork(true);
                return user;
            }
        });
    }

    private ObservableTransformer<UserToken, UserEntity> userTokenTransformer() {
        return new ObservableTransformer<UserToken, UserEntity>() {
            @Override
            public ObservableSource<UserEntity> apply(Observable<UserToken> upstream) {
                return upstream
                        .doOnNext(new Consumer<UserToken>() {
                            @Override
                            public void accept(UserToken userToken) throws Exception {
                                UserSpHelper.setUserId(userToken.getUserId());
                                UserSpHelper.setToken(userToken.getToken());
                                UserSpHelper.setRefreshToken(userToken.getRefreshToken());
                            }
                        })
                        .flatMap(new Function<UserToken, ObservableSource<UserEntity>>() {
                            @Override
                            public ObservableSource<UserEntity> apply(UserToken userToken) throws Exception {
                                return fetchUser(userToken.getUserId());
                            }
                        })
                        .doOnNext(new Consumer<UserEntity>() {
                            @Override
                            public void accept(UserEntity user) throws Exception {
                                UserSpHelper.setNoNetwork(false);
                                UserSpHelper.setFaceId(user.xfid);
                                UserSpHelper.setEqId(user.deviceId);
                                UserSpHelper.setUserName(user.name);
                            }
                        });
            }
        };
    }

    public Observable<UserEntity> refreshToken(
            String deviceId,
            String userId) {
        return mUserService.refreshToken(deviceId, userId, UserSpHelper.getRefreshToken())
                .compose(RxUtils.apiResultTransformer())
                .compose(userTokenTransformer());
    }

    public Observable<UserEntity> getUserSignIn() {
        return fetchUser(UserSpHelper.getUserId());
    }

    public Observable<Boolean> hasAccount(String account) {
        return mUserService.hasAccount("3", account)
                .compose(RxUtils.apiResultTransformer())
                .map(new Function<Object, Boolean>() {
                    @Override
                    public Boolean apply(Object o) throws Exception {
                        return true;
                    }
                })
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Boolean>>() {
                    @Override
                    public ObservableSource<? extends Boolean> apply(Throwable throwable) throws Exception {
                        if (throwable instanceof NullPointerException) {
                            return Observable.just(true);
                        }
                        return Observable.just(false);
                    }
                });
    }
    public Observable<Boolean> hasAccount2(String account,String name) {
        return mUserService.hasAccount2("3", account,name)
                .compose(RxUtils.apiResultTransformer())
                .map(new Function<Object, Boolean>() {
                    @Override
                    public Boolean apply(Object o) throws Exception {
                        return true;
                    }
                })
                .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Boolean>>() {
                    @Override
                    public ObservableSource<? extends Boolean> apply(Throwable throwable) throws Exception {
                        if (throwable instanceof NullPointerException) {
                            return Observable.just(true);
                        }
                        return Observable.just(false);
                    }
                });
    }


    public Observable<String> fetchCode(String phone) {
        return mUserService.fetchCode(phone)
                .compose(RxUtils.apiResultTransformer())
                .map(new Function<Code, String>() {
                    @Override
                    public String apply(Code code) throws Exception {
                        return code.code;
                    }
                });
    }

    public Observable<Object> updatePassword(String account, String pwd) {
        return mUserService.updatePassword(account, pwd)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<UserEntity> putProfile(UserEntity user) {
        String userId = UserSpHelper.getUserId();
        if (TextUtils.isEmpty(userId)) {
            return Observable.error(new IllegalStateException("user not sign in"));
        }
        user.id = userId;
        return mUserService.putProfile(userId, user)
                .compose(RxUtils.apiResultTransformer())
                .flatMap(new Function<Object, ObservableSource<UserEntity>>() {
                    @Override
                    public ObservableSource<UserEntity> apply(Object o) throws Exception {
                        return fetchUser(userId)
                                .subscribeOn(Schedulers.io());
                    }
                });
    }

    public Observable<UserEntity> fetchUser(String userId) {
        if (UserSpHelper.isNoNetwork()) {
            Observable<List<UserEntity>> usersLocal =
                    userProvider.usersLocal(Observable.empty(), new EvictProvider(false))
                            .toObservable()
                            .onErrorResumeNext(Observable.just(Collections.emptyList()));
            return usersLocal.flatMap(new Function<List<UserEntity>, ObservableSource<UserEntity>>() {
                @Override
                public ObservableSource<UserEntity> apply(List<UserEntity> userEntities) throws Exception {
                    for (UserEntity entity : userEntities) {
                        if (!TextUtils.isEmpty(entity.id) && entity.id.equals(userId)) {
                            return Observable.just(entity);
                        }
                    }
                    return Observable.error(new ApiException("Local User " + userId + " not exist", 404));
                }
            });
        }

        Observable<UserEntity> rxUserRemote = mUserService.getProfile(userId)
                .compose(RxUtils.apiResultTransformer());

        return rxUserRemote.flatMap(new Function<UserEntity, ObservableSource<UserEntity>>() {
            @Override
            public ObservableSource<UserEntity> apply(UserEntity userEntity) throws Exception {
                Observable<List<UserEntity>> users = userProvider.users(Observable.empty(), new EvictProvider(false))
                        .toObservable()
                        .onErrorResumeNext(Observable.just(Collections.emptyList()));
                return users.map(new Function<List<UserEntity>, List<UserEntity>>() {
                    @Override
                    public List<UserEntity> apply(List<UserEntity> userEntities) throws Exception {
                        ArrayList<UserEntity> entities = new ArrayList<>();
                        entities.add(userEntity);
                        for (UserEntity entity : userEntities) {
                            if (!TextUtils.isEmpty(entity.id) && !entity.id.equals(userEntity.id)) {
                                entities.add(userEntity);
                            }
                        }
                        return entities;
                    }
                }).compose(new ObservableTransformer<List<UserEntity>, UserEntity>() {
                    @Override
                    public ObservableSource<UserEntity> apply(Observable<List<UserEntity>> upstream) {
                        return userProvider.users(upstream, new EvictProvider(true))
                                .toObservable()
                                .map(new Function<List<UserEntity>, UserEntity>() {
                                    @Override
                                    public UserEntity apply(List<UserEntity> userEntities) throws Exception {
                                        return userEntity;
                                    }
                                });
                    }
                });
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * @return users
     */
    public Observable<List<UserEntity>> getUsers() {
        if (UserSpHelper.isNoNetwork()) {
            return userProvider.usersLocal(Observable.empty(), new EvictProvider(false))
                    .toObservable()
                    .onErrorResumeNext(Observable.just(Collections.emptyList()));
        }

        Observable<List<UserEntity>> list = userProvider.users(
                Observable.empty(), new EvictProvider(false)).toObservable();
        return list
                .flatMap(new Function<List<UserEntity>, ObservableSource<List<UserEntity>>>() {
                    @Override
                    public ObservableSource<List<UserEntity>> apply(List<UserEntity> users) throws Exception {
                        if (users.isEmpty()) {
                            return Observable.just(users);
                        }
                        StringBuilder userIdsBuilder = new StringBuilder();
                        int size = users.size();
                        for (int i = 0; i < size; i++) {
                            UserEntity user = users.get(i);
                            if (user == null || TextUtils.isEmpty(user.id)) {
                                continue;
                            }
                            userIdsBuilder.append(user.id);
                            if (i != size - 1) {
                                userIdsBuilder.append(",");
                            }
                        }
                        return mUserService.getAllUsers(userIdsBuilder.toString())
                                .compose(RxUtils.apiResultTransformer())
                                .subscribeOn(Schedulers.io());
                    }
                })
                .map(new Function<List<UserEntity>, List<UserEntity>>() {
                    @Override
                    public List<UserEntity> apply(List<UserEntity> userEntities) throws Exception {
                        Iterator<UserEntity> iterator = userEntities.iterator();
                        while (iterator.hasNext()) {
                            UserEntity entity = iterator.next();
                            if (entity == null) {
                                iterator.remove();
                            }
                        }
                        return userEntities;
                    }
                });
    }

    public void deleteUsers() {
        userProvider.users(ProviderHelper.withoutLoader(), new EvictProvider(true))
                .toObservable()
                .map(new Function<List<UserEntity>, String>() {
                    @Override
                    public String apply(List<UserEntity> userEntity) throws Exception {
                        return "detele";
                    }
                })
                .onErrorReturn(new Function<Throwable, String>() {
                    @Override
                    public String apply(Throwable throwable) throws Exception {
                        return "detele";
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String tips) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public Observable<Object> isIdCardNotExit(String idCard) {
        return mUserService.isIdCardNotExit(idCard)
                .compose(RxUtils.apiResultTransformer());
    }
}
