package com.gcml.auth.ui.profile;

import com.gcml.auth.model.UserRepository;
import com.gcml.common.data.UserEntity;
import com.gcml.common.mvvm.BaseViewModel;

import io.reactivex.Observable;

public class ProfileInfoViewModel extends BaseViewModel {

    private UserRepository mUserRepository = new UserRepository();

    public Observable<UserEntity> updateUser(UserEntity user) {
        return mUserRepository.putProfile(user);
    }
}
