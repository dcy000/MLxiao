package com.gcml.auth.ui.findpassword;

import android.app.Application;
import android.support.annotation.NonNull;

import com.gcml.auth.model.UserRepository;
import com.gcml.common.mvvm.BaseViewModel;

import io.reactivex.Observable;

public class FindPasswordViewModel extends BaseViewModel {

    private UserRepository mUserRepository = new UserRepository();

    public FindPasswordViewModel(@NonNull Application application) {
        super(application);
    }

    public Observable<Boolean> hasAccount(String account) {
        return mUserRepository.hasAccount(account);
    }
}
