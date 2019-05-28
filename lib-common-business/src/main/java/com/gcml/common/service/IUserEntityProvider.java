package com.gcml.common.service;

import com.gcml.common.data.PostUserEntity;
import com.gcml.common.data.UserEntity;

import io.reactivex.Observable;

public interface IUserEntityProvider {
    Observable<UserEntity> getUserEntity();
    Observable<UserEntity> updateUserEntity(UserEntity user);
    Observable<UserEntity> updateUserEntity(PostUserEntity user);
    Observable<UserEntity> fetchUser();
}
