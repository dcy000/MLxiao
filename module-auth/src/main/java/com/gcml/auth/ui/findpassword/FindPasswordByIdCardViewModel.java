package com.gcml.auth.ui.findpassword;

import com.gcml.auth.model.UserRepository;
import com.gcml.common.mvvm.BaseViewModel;

import io.reactivex.Observable;

public class FindPasswordByIdCardViewModel extends BaseViewModel {

    private UserRepository mUserRepository = new UserRepository();

    public Observable<String> fetchCode(String phone) {
        return mUserRepository.fetchCode(phone);
    }

    public Observable<Boolean> hasAccount2(String account, String name) {
        return mUserRepository.hasAccount2(account, name);
    }
}
