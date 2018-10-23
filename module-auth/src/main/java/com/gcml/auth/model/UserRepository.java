package com.gcml.auth.model;

import android.content.Context;
import android.text.TextUtils;

import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.repository.IRepositoryHelper;
import com.gcml.common.repository.RepositoryApp;
import com.gcml.common.user.UserToken;
import com.gcml.common.utils.RxUtils;

import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class UserRepository {

    private Context mContext = RepositoryApp.INSTANCE.app();

    private IRepositoryHelper mRepositoryHelper = RepositoryApp.INSTANCE.repositoryComponent().repositoryHelper();

    private UserService mUserService = mRepositoryHelper.retrofitService(UserService.class);

    private UserDao mUserDao = mRepositoryHelper.roomDb(UserDb.class, UserDb.class.getName()).userDao();

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


    public Observable<UserEntity> signIn(
            String deviceId,
            String userName,
            String pwd) {
        return mUserService.signIn(deviceId, userName, pwd)
                .compose(RxUtils.apiResultTransformer())
                .compose(userTokenTransformer());
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
                                UserSpHelper.setFaceId(user.xfid);
                                UserSpHelper.setEqId(user.deviceId);
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
        return mUserService.getProfile(userId)
                .compose(RxUtils.apiResultTransformer())
                .doOnNext(new Consumer<UserEntity>() {
                    @Override
                    public void accept(UserEntity userEntity) throws Exception {
                        mUserDao.addAll(userEntity);
                    }
                });
    }

    /**
     * @return users
     */
    public Observable<List<UserEntity>> getUsers() {
        return mUserDao.findAll()
                .toObservable()
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
                            if (i != size -1) {
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
        mUserDao.deleteAll();
    }

    public Observable<Object> isIdCardNotExit(String idCard) {
        return mUserService.isIdCardNotExit(idCard)
                .compose(RxUtils.apiResultTransformer());
    }
}
