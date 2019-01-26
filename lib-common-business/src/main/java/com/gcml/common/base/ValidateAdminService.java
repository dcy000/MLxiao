package com.gcml.common.base;

import com.gcml.common.http.ApiResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lenovo on 2019/1/21.
 */

public interface ValidateAdminService {

    @FormUrlEncoded
    @POST("ZZB/api/server/valid/")
    Observable<ApiResult<Object>> validateAdmin(@Field("password") String password);
}
