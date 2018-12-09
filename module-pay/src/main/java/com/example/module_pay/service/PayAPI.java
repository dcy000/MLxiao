package com.example.module_pay.service;

import com.gzq.lib_core.http.model.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PayAPI {
    /**
     * 充值
     *
     * @param eqid
     * @param bba
     * @param time
     * @param userId
     * @return
     */
    @POST("ZZB/br/chongzhi")
    Observable<HttpResult<String>> recharge(
            @Query("eqid") String eqid,
            @Query("bba") double bba,
            @Query("time") long time,
            @Query("bid") String userId
    );
}
