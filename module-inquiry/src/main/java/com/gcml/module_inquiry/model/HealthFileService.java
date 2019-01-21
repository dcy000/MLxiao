package com.gcml.module_inquiry.model;

import com.gcml.common.http.ApiResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lenovo on 2019/1/21.
 */

public interface HealthFileService {
    @GET("ZZB/docter/seldoctors/")
    Observable<ApiResult<List<Docter>>> getDoctors(
            @Query("start") Integer index,
            @Query("limit") Integer limit);
}
