package com.gcml.auth.model;

import android.arch.persistence.room.EmptyResultSetException;
import android.content.Context;
import android.text.TextUtils;

import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.repository.IRepositoryHelper;
import com.gcml.common.repository.RepositoryApp;
import com.gcml.common.user.UserToken;
import com.gcml.common.utils.RxUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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
                }).doOnNext(new Consumer<UserEntity>() {
                    @Override
                    public void accept(UserEntity userEntity) throws Exception {
                        UserSpHelper.setFaceId(userEntity.xfid);
                    }
                });
    }

    public Observable<UserEntity> getUserSignIn() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String userId = UserSpHelper.getUserId();
                if (TextUtils.isEmpty(userId)) {
                    if (!emitter.isDisposed()) {
                        emitter.onError(new EmptyResultSetException("user not sign in"));
                    }
                    return;
                }
                emitter.onNext(userId);
            }
        }).flatMap(new Function<String, ObservableSource<? extends UserEntity>>() {
            @Override
            public ObservableSource<? extends UserEntity> apply(String userId) throws Exception {
                return mUserDao.findOneById(userId).toObservable();
            }
        });
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
     *
     * @return users
     */
    public Observable<List<UserEntity>> getUsers() {
        return mUserDao.findAll()
                .toObservable();
    }

    public Observable<Object> isIdCardNotExit(String idCard) {
        return mUserService.isIdCardNotExit(idCard)
                .compose(RxUtils.apiResultTransformer());
    }
}
