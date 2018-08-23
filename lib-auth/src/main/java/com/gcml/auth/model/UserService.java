package com.gcml.auth.model;

import com.gcml.common.data.UserEntity;
import com.gcml.common.repository.http.ApiResult;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {

    @FormUrlEncoded()
    @POST("/ZZB/login/applogin")
    Observable<ApiResult<UserEntity>> signIn(
            @Field("username") String account,
            @Field("password") String pwd
    );
}
