package com.example.module_login.service;

import com.gzq.lib_core.bean.SessionBean;
import com.gzq.lib_core.http.model.HttpResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginAPI {
    /**
     * 获取登录token
     *
     * @param userName
     * @param password
     * @return
     */
    @POST("/ZZB/login/applogin")
    Observable<HttpResult<SessionBean>> login(@Query("username") String userName, @Query("password") String password);

    /**
     * 重置密码
     *
     * @param account
     * @param pwd
     * @return
     */
    @POST("ZZB/acc/update_account_pwd")
    Observable<Object> setPassWord(@Query("account") String account, @Query("pwd") String pwd);

    /**
     * 检验该手机账号是否存在
     *
     * @param cate
     * @param account
     * @return
     */
    @GET("ZZB/acc/sel_account")
    Observable<Object> isPhoneUsable(@Query("cate") String cate, @Query("account") String account);
}
