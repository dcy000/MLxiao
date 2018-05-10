package com.medlink.danbogh.cache;

import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.bean.UserInfoBean;

import java.util.List;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.DynamicKeyGroup;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.EvictDynamicKeyGroup;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.ProviderKey;
import io.rx_cache2.Reply;

/**
 * Created by lenovo on 2018/5/9.
 */

public interface Providers {

    @ProviderKey("users")
    Observable<UserInfoBean> getUser(
            Observable<UserInfoBean> rxUserSrc,
            DynamicKey current,
            EvictDynamicKey evictDynamicKey
    );

    @ProviderKey("users")
    Observable<List<UserInfoBean>> getUsers(
            Observable<List<UserInfoBean>> rxUsersSrc,
            DynamicKeyGroup family,
            EvictDynamicKeyGroup evictDynamicKeyGroup
    );

    @ProviderKey("users")
    Observable<List<UserInfoBean>> getUsers(
            Observable<List<UserInfoBean>> rxUsersSrc,
            EvictProvider evictProvider
    );

    @ProviderKey("measure-data")
    Observable<List<DataInfoBean>> measureData(
            Observable<List<DataInfoBean>> rxMeasureDataSrc,
            DynamicKey idCard,
            EvictDynamicKey evictDynamicKey
    );
}
