package com.gcml.auth.ui.findpassword;

import com.gcml.auth.model.UserRepository;
import com.gcml.common.mvvm.BaseViewModel;

import io.reactivex.Observable;

public class SetPasswordViewModel extends BaseViewModel {

    private UserRepository mUserRepository = new UserRepository();

    public Observable<Object> updatePassword(String account, String pwd) {
        return mUserRepository.updatePassword(account, pwd);
    }
}
