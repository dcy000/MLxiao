package com.gcml.common.service;

import com.gcml.common.data.UserEntity;

import io.reactivex.Observable;

public interface IUserEntityProvider {
    Observable<UserEntity> getUserEntity();

    Observable<UserEntity> updateUserEntity(UserEntity user);

    /**
     * @param type: 1.账号 2.手机号 3.身份证 4.百度人脸id
     */
    Observable<Object> isAccountExist(String account, int type);

    /**
     * 老版本需要的接口, 合版暂时没用
     */
    Observable<UserEntity> fetchUser();


}
