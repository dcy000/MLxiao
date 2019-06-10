package com.gcml.module_auth_hospital.ui.findPassWord;


import com.gcml.common.http.ApiResult;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.gcml.common.constant.Global.URI;

public interface CodeService {
    @POST(URI + "/br/GainCode")
    Observable<ApiResult<Code>> fetchCode(
            @Query("mobile") String phone
    );

    @POST(URI + "/acc/updateAccountPwd")
    Observable<ApiResult<Object>> updatePassword(
            @Query("account") String account,
            @Query("pwd") String pwd
    );

}
