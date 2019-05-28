package com.gcml.module_auth_hospital.model;

import com.gcml.common.data.PostUserEntity;
import com.gcml.common.data.UserEntity;
import com.gcml.common.http.ApiResult;
import com.gcml.common.user.UserPostBody;
import com.gcml.common.user.UserToken;
import com.gcml.module_auth_hospital.postinputbean.SignUpBean;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    /**
     * 患者账号登陆
     * 必填字段
     * "category"
     * "username"
     * "password"
     */
    @POST("/open/common/sys/login/appLogin")
    Observable<ApiResult<UserToken>> signIn(@Body UserPostBody body);

    /**
     * 患者身份证信息登录
     * 必填字段
     * "category"
     * "sfz"
     */
    @POST("/open/common/sys/login/sfzLogin")
    Observable<ApiResult<UserToken>> signInByIdCard(@Body UserPostBody body);

    /**
     * 注册
     */
    @POST("/open/common/br/appadd")
    Observable<ApiResult<UserEntity>> signUp(@Body SignUpBean bean, @Query("pwd") String pwd);

    /**
     * 根据token获取 用户信息
     */
    @GET("/open/common/api/user/info/byToken/")
    Observable<ApiResult<UserEntity>> getUserInfoByToken();

    @PUT("/open/common/api/user/info/{patientId}/")
    Observable<UserEntity> updateUserInfo(@Path("patientId") String path,@Body PostUserEntity entity);
}
