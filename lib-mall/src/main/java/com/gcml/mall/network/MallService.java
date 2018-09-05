package com.gcml.mall.network;

import com.gcml.common.repository.http.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MallService {

    /**
     * 获取用户任务列表.
     */
    @POST("ZZB/br/chongzhi")
    Observable<ApiResult<String>> recharge(
            @Query("eqid") String eqid,
            @Query("bba") String bba,
            @Query("time") String time,
            @Query("bid") String bid
    );
}
