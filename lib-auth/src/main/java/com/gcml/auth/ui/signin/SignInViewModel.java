package com.gcml.auth.ui.signin;

import android.app.Application;
import android.support.annotation.NonNull;

import com.gcml.common.data.UserEntity;
import com.gcml.auth.model.UserRepository;
import com.gcml.common.mvvm.BaseViewModel;

import io.reactivex.Observable;

public class SignInViewModel extends BaseViewModel {

    private UserRepository mUserRepository = new UserRepository();

    public SignInViewModel(@NonNull Application application) {
        super(application);
    }

    public Observable<UserEntity> signIn(
            String userName,
            String pwd) {
        return mUserRepository.signIn(userName, pwd);
    }

}
