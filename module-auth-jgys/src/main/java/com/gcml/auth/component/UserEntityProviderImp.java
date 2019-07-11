package com.gcml.auth.component;

import com.gcml.auth.model.UserRepository;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.service.IUserEntityProvider;
import com.sjtu.yifei.annotation.Route;

import io.reactivex.Observable;

@Route(path = "/common/business/user/provider")
public class UserEntityProviderImp implements IUserEntityProvider {
    @Override
    public Observable<UserEntity> getUserEntity() {
        return new UserRepository().getUserSignIn();
    }

    @Override
    public Observable<UserEntity> updateUserEntity(UserEntity user) {
        return new UserRepository().putProfile(user);
    }

    @Override
    public Observable<Object> isAccountExist(String account, int type) {
        return null;
    }

    @Override
    public Observable<Object> isIdCardNotExist(String idCard) {
        return new UserRepository().isIdCardNotExit(idCard);
    }

    @Override
    public Observable<UserEntity> fetchUser() {
        return new UserRepository().fetchUser(UserSpHelper.getUserId());
    }
}
