package com.gcml.common.user;

import com.gcml.common.data.UserEntity;
import com.gcml.common.http.ApiResult;

import io.reactivex.Observable;

/**
 * 向外界暴露的服务
 * bearer token前缀
 * http://192.168.200.210:5555/doc.html 接口文档地址
 */
public interface IUserService {
    /**
     * 游客账号登录
     */
    Observable<ApiResult<UserToken>> signIn(UserPostBody body);

    /**
     * 根据token获取PatientId
     *
     * @return
     */
    Observable<UserEntity> getUserEntity();

}
