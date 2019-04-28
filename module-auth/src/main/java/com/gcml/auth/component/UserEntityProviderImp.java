package com.gcml.auth.component;

import com.gcml.auth.model.UserRepository;
import com.gcml.common.data.UserEntity;
import com.gcml.common.service.IUserEntityProvider;
import com.sjtu.yifei.annotation.Route;

import io.reactivex.Observable;

@Route(path = "/common/business/user/provider")
public class UserEntityProviderImp implements IUserEntityProvider {
    @Override
    public Observable<UserEntity> getUserEntity() {
        return new UserRepository().getUserSignIn();
    }
}
