package com.gzq.lib_core.service;

import com.gzq.lib_core.bean.PhoneCode;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.model.HttpResult;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 其他模块都要使用的公共模块
 */
public interface CommonAPI {
    /**
     * 获取验证码
     *
     * @param phone
     * @return
     */
    @GET("ZZB/br/GainCode")
    Observable<HttpResult<PhoneCode>> getPhoneCode(@Query("mobile") String phone);

    /**
     * 判断手机号码是否已经被注册
     *
     * @param tel
     * @param state
     * @return
     */
    @GET("ZZB/login/tel_isClod")
    Observable<Object> isPhoneRegister(@Query("tel") String tel, @Query("state") String state);

    /**
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    @GET("ZZB/br/selOneUserEverything")
    Observable<HttpResult<UserInfoBean>> queryUserInfo(@Query("bid") String userId);

    /**
     * 根据useId查询所有用户信息
     *
     * @param userIds
     * @return
     */
    @GET("ZZB/api/user/info/users/")
    Observable<HttpResult<ArrayList<UserInfoBean>>> queryAllLocalUsers(@Query("users") String userIds);
}
