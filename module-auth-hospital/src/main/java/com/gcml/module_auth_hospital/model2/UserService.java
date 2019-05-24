package com.gcml.module_auth_hospital.model2;

import com.gcml.common.http.ApiResult;
import com.gcml.common.user.UserPostBody;
import com.gcml.common.user.UserToken;
import com.gcml.module_auth_hospital.postinputbean.SignUpBean;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
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

    @POST("/open/common/br/appadd")
    Observable<ApiResult<UserBean>> signUp(@Body SignUpBean bean, @Query("pwd") String pwd);


}
