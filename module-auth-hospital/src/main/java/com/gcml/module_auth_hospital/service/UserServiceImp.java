package com.gcml.module_auth_hospital.service;

import com.gcml.common.data.UserEntity;
import com.gcml.common.user.IUserService;
import com.gcml.common.user.UserPostBody;
import com.gcml.common.user.UserToken;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.sjtu.yifei.annotation.Route;

import io.reactivex.Observable;

@Route(path = "/user/tourist/login/provider")
public class UserServiceImp implements IUserService {
    UserRepository repository = new UserRepository();

    @Override
    public Observable<UserToken> signIn(UserPostBody body) {
        return repository.signIn(body);
    }

    @Override
    public Observable<UserEntity> getUserEntity() {
        return repository.getUserInfoByToken();
    }
}
