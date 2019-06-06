package com.gcml.auth.ui.signin;

import com.gcml.common.data.UserEntity;
import com.gcml.auth.model.UserRepository;
import com.gcml.common.mvvm.BaseViewModel;

import io.reactivex.Observable;

public class SignInViewModel extends BaseViewModel {

    private UserRepository mUserRepository = new UserRepository();

    public Observable<UserEntity> signIn(
            String deviceId,
            String userName,
            String pwd) {
        return mUserRepository.signIn(deviceId, userName, pwd);
    }

    public Observable<UserEntity> signInNoNetWork(String phone) {
        return mUserRepository.signInNoNetWork(phone);
    }
}
