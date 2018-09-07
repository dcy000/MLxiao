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
                .doOnNext(new Consumer<UserEntity>() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void accept(UserEntity user) throws Exception {
                        mUserDao.addAll(user);
                    }
                })
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
                .doOnNext(new Consumer<UserEntity>() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void accept(UserEntity user) throws Exception {
                        mUserDao.addAll(user);
                        UserSpHelper.setUserId(user.id);
                        UserSpHelper.setFaceId(user.xfid);
                        UserSpHelper.addAccount(user.id, user.xfid);
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
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> updatePassword(String account, String pwd) {
        return mUserService.updatePassword(account, pwd)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> putProfile(UserEntity user) {
        String userId = UserSpHelper.getUserId();

        if (TextUtils.isEmpty(userId)) {
            return Observable.error(new IllegalStateException("user not sign in"));
        }
        user.id = userId;
        return mUserService.putProfile(userId, user)
                .compose(RxUtils.apiResultTransformer());
    }

    public Observable<Object> hasIdCard(String idCard) {
        return mUserService.hasIdCard(idCard)
                .compose(RxUtils.apiResultTransformer());
    }
}
