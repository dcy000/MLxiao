package com.gcml.auth.model;

import com.gcml.common.data.UserEntity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.ProviderKey;

public interface UserProvider {

    @ProviderKey("user-current-local")
    Observable<UserEntity> currentUserLocal(
            Observable<UserEntity> rxUserSrc,
            EvictProvider update
    );

    @ProviderKey("user-local")
    Observable<UserEntity> userLocal(
            Observable<UserEntity> rxUserSrc,
            DynamicKey userId,
            EvictDynamicKey evictDynamicKey
    );

    @ProviderKey("users-local")
    Single<List<UserEntity>> usersLocal(
            Observable<List<UserEntity>> rxUserSrc,
            EvictProvider update
    );

    @ProviderKey("user-current")
    Observable<UserEntity> currentUser(
            Observable<UserEntity> rxUserSrc,
            EvictProvider update
    );

    @ProviderKey("user")
    Observable<UserEntity> user(
            Observable<UserEntity> rxUserSrc,
            DynamicKey userId,
            EvictDynamicKey evictDynamicKey
    );

    @ProviderKey("users")
    Single<List<UserEntity>> users(
            Observable<List<UserEntity>> rxUserSrc,
            EvictProvider update
    );
}
