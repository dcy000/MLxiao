package com.gcml.auth.ui.signup;

import com.gcml.auth.model.UserRepository;
import com.gcml.common.data.UserEntity;
import com.gcml.common.mvvm.BaseViewModel;

import io.reactivex.Observable;

public class SignUpViewModel extends BaseViewModel {

    private UserRepository mUserRepository = new UserRepository();

    public Observable<Boolean> hasAccount(String account) {
        return mUserRepository.hasAccount(account);
    }

    public Observable<String> fetchCode(String phone) {
        return mUserRepository.fetchCode(phone);
    }

    public Observable<UserEntity> signUp(String deviceId, String account, String pwd) {
        return mUserRepository.signUp(deviceId, account, pwd);
    }
}
