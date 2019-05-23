package com.gcml.module_auth_hospital.service;

import com.gcml.common.data.UserEntity;
import com.gcml.common.user.IUserService;
import com.gcml.common.user.UserPostBody;
import com.gcml.module_auth_hospital.model2.UserRepository;
import com.sjtu.yifei.annotation.Route;

import io.reactivex.Observable;

@Route(path = "/user/login/provider")
public class UserServiceImp implements IUserService {
    UserRepository repository = new UserRepository();


    @Override
    public Observable<UserEntity> signIn(UserPostBody body) {
        return repository.signIn(body);
    }

    @Override
    public Observable<UserEntity> signInByIdCard(UserPostBody body) {
        return repository.signInByIdCard(body);
    }

}
