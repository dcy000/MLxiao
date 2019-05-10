package com.gcml.module_yzn.repository;

import com.gcml.common.http.ApiResult;
import com.gcml.module_yzn.bean.OutBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by lenovo on 2019/5/10.
 */

public interface YZNService {
    /**
     * 三方的诊断
     */
    @Headers({"Domain-Name:zenduan"})
    @FormUrlEncoded
    @POST("api/chat")
    public Observable<ApiResult<OutBean>> chat(
            @Field("AppId") String appId,
            @Field("CurTime") String currentTime,
            @Field("Param") String param,
            @Field("Token") String token
    );
}
