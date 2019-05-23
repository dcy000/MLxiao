package com.gcml.common.user;

import com.gcml.common.data.UserEntity;

import io.reactivex.Observable;

/**
 * 向外界暴露的服务
 * bearer token前缀
 * http://192.168.200.210:5555/doc.html 接口文档地址
 */
public interface IUserService {
    /**
     * 用户账号登录
     */
    Observable<UserEntity> signIn(UserPostBody body);

    /**
     * 用户身份证信息登录
     */
    Observable<UserEntity> signInByIdCard(UserPostBody body);
}
