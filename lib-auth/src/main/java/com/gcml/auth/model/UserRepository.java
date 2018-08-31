package com.gcml.auth.model;

import android.annotation.SuppressLint;
import android.arch.persistence.room.EmptyResultSetException;
import android.content.Context;
import android.text.TextUtils;

import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.repository.IRepositoryHelper;
import com.gcml.common.repository.RepositoryApp;
import com.gcml.common.utils.RxUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class UserRepository {

    private Context mContext = RepositoryApp.INSTANCE.app();

    private IRepositoryHelper mRepositoryHelper = RepositoryApp.INSTANCE.repositoryComponent().repositoryHelper();

    private UserService mUserService = mRepositoryHelper.retrofitService(UserService.class);

    private UserDao mUserDao = mRepositoryHelper.roomDb(UserDb.class, UserDb.class.getName()).userDao();

    public Observable<UserEntity> signUp(String deviceId, String account, String pwd) {
        return mUserService.signUp(deviceId, account, pwd)
                .compose(RxUtils.apiResultTransformer())
                .doOnNext(new Consumer<UserEntity>() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void accept(UserEntity user) throws Exception {
                        UserSpHelper.setUserId(user.id);
                        UserSpHelper.setFaceId(user.xfid);
                        mUserDao.addAll(user);
                    }
                });
    }

    public Observable<UserEntity> signIn(
            String deviceId,
            String userName,
            String pwd) {
        return mUserService.signIn(deviceId, userName, pwd)
                .compose(RxUtils.apiResultTransformer())
                .doOnNext(new Consumer<UserEntity>() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void accept(UserEntity user) throws Exception {
                        mUserDao.addAll(user);
                        UserSpHelper.setUserId(user.id);
                        UserSpHelper.setFaceId(user.xfid);
                    }
                });
    }

    public Single<UserEntity> getUserSignIn() {
        return Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> emitter) throws Exception {
                String userId = UserSpHelper.getUserId();
                if (TextUtils.isEmpty(userId)) {
                    emitter.onError(new EmptyResultSetException("no user sign in"));
                    return;
                }
                emitter.onSuccess(userId);
            }
        }).flatMap(new Function<String, SingleSource<? extends UserEntity>>() {
            @Override
            public SingleSource<? extends UserEntity> apply(String userId) throws Exception {
                return mUserDao.findOneById(userId);
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
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> updatePassword(String account, String pwd) {
        return mUserService.updatePassword(account, pwd)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> updateProfile(UserEntity user) {
        String userId = UserSpHelper.getUserId();

        if (TextUtils.isEmpty(userId)) {
            return Observable.error(new IllegalStateException("user not sign in"));
        }
        user.id = userId;
        return mUserService.updateProfile(userId, user)
                .compose(RxUtils.apiResultTransformer());
    }
}
