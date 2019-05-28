package com.gcml.module_auth_hospital.model2;

import com.gcml.common.data.UserEntity;
import com.gcml.common.service.IUserEntityProvider;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.sjtu.yifei.annotation.Route;

import io.reactivex.Observable;

@Route(path = "/common/business/user/provider")
public class UserEntityProviderImp implements IUserEntityProvider {
    @Override
    public Observable<UserEntity> getUserEntity() {
        return new UserRepository().getUserInfoByToken();
    }

    @Override
    public Observable<UserEntity> updateUserEntity(UserEntity user) {
        return null;
    }

    @Override
    public Observable<UserEntity> fetchUser() {
        return null;
    }
}
