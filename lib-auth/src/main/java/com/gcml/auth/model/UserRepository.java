package com.gcml.auth.model;

import android.annotation.SuppressLint;
import android.arch.persistence.room.EmptyResultSetException;
import android.content.Context;
import android.content.SharedPreferences;

import com.gcml.common.data.UserEntity;
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

    private SharedPreferences mPreferences;

    public SharedPreferences getPreferences() {
        if (mPreferences == null) {
            synchronized (UserRepository.class) {
                if (mPreferences == null) {
                    mPreferences = mContext.getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
                }
            }
        }
        return mPreferences;
    }

    public Observable<UserEntity> signUp(String deviceId, String account, String pwd) {
        return mUserService.signUp(deviceId, account, pwd)
                .compose(RxUtils.apiResultTransformer())
                .doOnNext(new Consumer<UserEntity>() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void accept(UserEntity user) throws Exception {
                        mUserDao.addAll(user);
                        getPreferences().edit()
                                .putInt("userId", user.getId())
                                .commit();
                    }
                });
    }

    public Observable<UserEntity> signIn(
            String userName,
            String pwd
    ){
        return mUserService.signIn(userName, pwd)
                .compose(RxUtils.apiResultTransformer())
                .doOnNext(new Consumer<UserEntity>() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void accept(UserEntity user) throws Exception {
                        mUserDao.addAll(user);
                        getPreferences().edit()
                                .putInt("userId", user.getId())
                                .commit();
                    }
                });
    }

    public Single<UserEntity> getUserSignIn() {
        return Single.create(new SingleOnSubscribe<Integer>() {
            @Override
            public void subscribe(SingleEmitter<Integer> emitter) throws Exception {
                int userId = getPreferences().getInt("userId", -1);
                if (userId == -1) {
                    emitter.onError(new EmptyResultSetException("no user sign in"));
                    return;
                }
                emitter.onSuccess(userId);
            }
        }).flatMap(new Function<Integer, SingleSource<? extends UserEntity>>() {
            @Override
            public SingleSource<? extends UserEntity> apply(Integer userId) throws Exception {
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

    public Observable<Boolean> updatePassword(String account, String pwd) {
        return mUserService.updatePassword(account, pwd)
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

}