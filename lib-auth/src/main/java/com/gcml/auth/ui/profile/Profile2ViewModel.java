package com.gcml.auth.ui.profile;

import android.app.Application;
import android.support.annotation.NonNull;

import com.gcml.auth.model.UserRepository;
import com.gcml.common.data.UserEntity;
import com.gcml.common.mvvm.BaseViewModel;

import io.reactivex.Observable;

public class Profile2ViewModel extends BaseViewModel {

    private UserRepository mUserRepository = new UserRepository();

    public Profile2ViewModel(@NonNull Application application) {
        super(application);
    }

    public Observable<UserEntity> updateProfile(UserEntity user){
        return mUserRepository.putProfile(user);
    }
}
