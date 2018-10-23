package com.gcml.auth.ui.findpassword;

import android.app.Application;
import android.support.annotation.NonNull;

import com.gcml.auth.model.UserRepository;
import com.gcml.common.mvvm.BaseViewModel;

import io.reactivex.Observable;

public class SetPasswordViewModel extends BaseViewModel {

    private UserRepository mUserRepository = new UserRepository();

    public SetPasswordViewModel(@NonNull Application application) {
        super(application);
    }

    public Observable<Object> updatePassword(String account, String pwd) {
        return mUserRepository.updatePassword(account, pwd);
    }
}
