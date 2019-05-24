package com.gcml.module_auth_hospital.service;

import com.gcml.common.http.ApiResult;
import com.gcml.common.user.IUserService;
import com.gcml.common.user.UserPostBody;
import com.gcml.common.user.UserToken;
import com.gcml.module_auth_hospital.model2.UserRepository;
import com.sjtu.yifei.annotation.Route;

import io.reactivex.Observable;

@Route(path = "/user/tourist/login/provider")
public class UserServiceImp implements IUserService {
    UserRepository repository = new UserRepository();

    @Override
    public Observable<ApiResult<UserToken>> signIn(UserPostBody body) {
        return repository.signIn(body);
    }
}
