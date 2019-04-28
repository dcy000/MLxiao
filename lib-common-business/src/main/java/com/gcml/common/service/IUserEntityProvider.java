package com.gcml.common.service;

import com.gcml.common.data.UserEntity;

import io.reactivex.Observable;

public interface IUserEntityProvider {
    Observable<UserEntity> getUserEntity();
}
