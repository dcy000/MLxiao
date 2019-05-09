package com.gcml.alarm;

import com.gcml.common.http.ApiResult;
import com.gcml.common.recommend.bean.get.Doctor;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AlarmApiService {

    @GET("ZZB/br/selOneUserClueAll")
    Observable<ApiResult<List<ClueInfoBean>>> getClue(@Query("pid") String userId);

    @POST("ZZB/bl/selReserveStart_time")
    Observable<ApiResult<List<AlreadyYuyue>>> contractAlready(@Query("docterid") String doctorId);

    @POST("ZZB/bl/app_update_reserve_state")
    Observable<ApiResult<String>> updateStatus(
            @Query("rid") String rid,
            @Query("state") String state
    );

    @POST("ZZB/br/addeatmod")
    Observable<ApiResult<Object>> addEatMedicalRecord(
            @Query("username") String userName,
            @Query("jl") String content,
            @Query("time") String time,
            @Query("state") String state
    );

    @GET("ZZB/docter/search_OneDocter")
    Observable<ApiResult<Doctor>> doctor(@Query("bid") String bid);
}
