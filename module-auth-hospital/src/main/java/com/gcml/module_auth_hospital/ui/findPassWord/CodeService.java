package com.gcml.module_auth_hospital.ui.findPassWord;


import com.gcml.common.http.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CodeService {
    @GET("/open/common/br/GainCode")
    Observable<ApiResult<Code>> fetchCode(
            @Query("mobile") String phone
    );

    @POST("/open/common/acc/updateAccountPwd")
    Observable<ApiResult<Object>> updatePassword(
            @Query("account") String account,
            @Query("pwd") String pwd
    );

}
