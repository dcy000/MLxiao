package com.gcml.module_health_profile.data;

import com.gcml.common.http.ApiResult;
import com.gcml.module_health_profile.bean.HealthProfileMenuBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface HealthProfileAPI {
    @GET("dd")
    Observable<ApiResult<List<HealthProfileMenuBean>>> getHealthProfileMenu();
}
