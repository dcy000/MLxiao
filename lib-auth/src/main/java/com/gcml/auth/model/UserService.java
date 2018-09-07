package com.gcml.auth.model;

import com.gcml.common.data.UserEntity;
import com.gcml.common.repository.http.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    @FormUrlEncoded()
    @POST("ZZB/login/applogin")
    Observable<ApiResult<UserEntity>> signIn(
            @Header("equipmentId") String deviceId,
            @Field("username") String account,
            @Field("password") String pwd
    );

    @FormUrlEncoded()
    @POST("ZZB/acc/sel_account")
    Observable<ApiResult<Object>> hasAccount(
            @Field("cate") String cate,
            @Field("account") String account
    );

    @GET("ZZB/br/GainCode")
    Observable<ApiResult<String>> fetchCode(
            @Query("mobile") String phone
    );

    @POST("ZZB/acc/update_account_pwd")
    Observable<ApiResult<Object>> updatePassword(
            @Query("account") String account,
            @Query("pwd") String pwd
    );

    @POST("ZZB/br/appadd")
    Observable<ApiResult<UserEntity>> signUp(
            @Query("eqid") String deviceId,
            @Query("tel") String account,
            @Query("pwd") String pwd
    );

    @PUT("ZZB/api/user/info/{userId}/")
    Observable<ApiResult<Object>> putProfile(
            @Path("userId") String userId,
            @Body UserEntity user
    );

    @GET("ZZB/api/user/info/idCard/{idCard}/")
    Observable<ApiResult<Object>> hasIdCard(
            @Path("idCard") String idCard
    );
}
