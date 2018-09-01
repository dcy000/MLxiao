package com.gcml.auth.model;

import com.gcml.common.data.UserEntity;
import com.gcml.common.repository.http.ApiResult;


import java.util.List;

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
    @POST("/ZZB/login/applogin")
    Observable<ApiResult<UserEntity>> signIn(
            @Header("equipmentId") String deviceId,
            @Field("username") String account,
            @Field("password") String pwd
    );

    @FormUrlEncoded()
    @POST("/ZZB/acc/sel_account")
    Observable<ApiResult<Object>> hasAccount(
            @Field("cate") String cate,
            @Field("account") String account
    );

    @GET("/ZZB/br/GainCode")
    Observable<ApiResult<String>> fetchCode(
            @Query("mobile") String phone
    );

    @FormUrlEncoded()
    @POST("/ZZB/acc/update_account_pwd")
    Observable<ApiResult<Object>> updatePassword(
            @Field("account") String account,
            @Field("pwd") String pwd
    );

    @FormUrlEncoded()
    @POST("/ZZB/br/appadd")
    Observable<ApiResult<UserEntity>> signUp(
            @Field("eqid") String deviceId,
            @Field("tel") String account,
            @Field("pwd") String pwd
    );

    @POST("/ZZB/api/user/info/{userId}/")
    Observable<ApiResult<Object>> updateProfile(
            @Path("userId") String userId,
            @Body() UserEntity user
    );
}
