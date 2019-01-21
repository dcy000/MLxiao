package com.gcml.module_health_profile.data;

import com.gcml.common.http.ApiResult;
import com.gcml.module_health_profile.bean.HealthProfileMenuBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HealthProfileAPI {
    @GET("ZZB/api/health/record/records/")
    Observable<ApiResult<List<HealthProfileMenuBean>>> getHealthProfileMenu(
//            @Query("serverId") String serverId,
//            @Query("followUpDoctor") String followUpDoctor
    );
}
