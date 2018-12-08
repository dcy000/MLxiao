package com.example.module_call.service;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CallAPI {
    /**
     * 视频通话扣费
     *
     * @param docterid
     * @param eqid
     * @param time
     * @param bid
     * @return
     */
    @POST("ZZB/eq/koufei")
    Observable<String> charge(
            @Query("docterid") String docterid,
            @Query("eqid") String eqid,
            @Query("time") int time,
            @Query("bid") String bid
    );
}
