package com.gzq.lib_core.service;

import com.gzq.lib_core.bean.PhoneCode;
import com.gzq.lib_core.http.model.HttpResult;

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
}
