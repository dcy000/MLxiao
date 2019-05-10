package com.gcml.auth.ui.findpassword;

import com.gcml.auth.model.UserRepository;
import com.gcml.common.mvvm.BaseViewModel;

import io.reactivex.Observable;

public class FindPasswordViewModel extends BaseViewModel {

    private UserRepository mUserRepository = new UserRepository();

    public Observable<String> fetchCode(String phone) {
        return mUserRepository.fetchCode(phone);
    }

    public Observable<Boolean> hasAccount(String account) {
        return mUserRepository.hasAccount(account);
    }
}
