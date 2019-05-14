package com.example.han.referralproject.network.heguiserver;

import com.gcml.common.http.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface HeGuiServer {
    @Headers({"Domain-Name:hegui"})
    @GET("third/getProduct/")
    Observable<ApiResult<Object>> getProduct(
            @Query("eqid") String eqid,
            @Query("timestamp") String timestamp,
            @Query("goodsType") String goodsType,
            @Query("sign") String sign);
}
