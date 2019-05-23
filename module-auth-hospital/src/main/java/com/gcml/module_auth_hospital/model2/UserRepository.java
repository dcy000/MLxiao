package com.gcml.module_auth_hospital.model2;

import android.content.Context;

import com.gcml.common.AppDelegate;
import com.gcml.common.RetrofitHelper;
import com.gcml.common.RoomHelper;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.user.UserPostBody;
import com.gcml.common.user.UserToken;
import com.gcml.common.utils.RxUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class UserRepository {
    private UserService mUserService = RetrofitHelper.service(UserService.class);
    private Context mContext = AppDelegate.INSTANCE.app();
    private UserDao mUserDao = RoomHelper.db(UserDb.class, UserDb.class.getName()).userDao();


    public Observable<UserEntity> signIn(UserPostBody body) {
        return mUserService.signIn(body)
                .compose(RxUtils.apiResultTransformer())
                .compose(userTokenTransformer());
    }

    public Observable<UserEntity> signInByIdCard(UserPostBody body) {
        return mUserService
                .signInByIdCard(body)
                .compose(RxUtils.apiResultTransformer())
                .compose(userTokenTransformer());
    }

    private ObservableTransformer<UserToken, UserEntity> userTokenTransformer() {
        return upstream -> upstream
                .doOnNext(userToken -> {
                    UserSpHelper.setUserId(userToken.getUserId());
                    UserSpHelper.setToken(userToken.getToken());
                    UserSpHelper.setRefreshToken(userToken.getRefreshToken());
                })
                .flatMap((Function<UserToken, ObservableSource<UserEntity>>) userToken -> fetchUser(userToken.getUserId()))
                .doOnNext((Consumer<UserEntity>) user -> {
                    UserSpHelper.setFaceId(user.xfid);
                    UserSpHelper.setEqId(user.deviceId);
                });
    }

    public Observable<UserEntity> fetchUser(String userId) {
        return mUserService.getProfile(userId)
                .compose(RxUtils.apiResultTransformer())
                .doOnNext(userEntity -> {
//                        mUserDao.addAll(userEntity);
                });
    }


}
