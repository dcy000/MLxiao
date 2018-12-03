package com.example.lib_alarm_clock.service;

import com.example.lib_alarm_clock.bean.ClueInfoBean;
import com.gzq.lib_core.bean.AlreadyYuyue;
import com.gzq.lib_core.http.model.HttpResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AlarmAPI {
    /**
     * 获取添加的所有闹钟
     *
     * @param userId
     * @return
     */
    @GET("ZZB/br/selOneUserClueAll")
    Observable<HttpResult<List<ClueInfoBean>>> getAllAlarmClocks(@Query("bid") String userId);

    /**
     * 添加吃药记录
     *
     * @param username
     * @param jl
     * @param time
     * @param state
     * @return
     */
    @POST("ZZB/br/addeatmod")
    Observable<Object> addEatMedicalRecord(
            @Query("username") String username,
            @Query("jl") String jl,
            @Query("time") long time,
            @Query("state") String state);
    /**
     * 查询该医生被预约的时间段
     *
     * @param docterid
     * @return
     */
    @POST("ZZB/bl/selReserveStart_time")
    Observable<HttpResult<List<AlreadyYuyue>>> queryDoctorReservationList(
            @Query("docterid") String docterid
    );

    /**
     * 更新预约状态
     *
     * @param rid
     * @param state
     * @return
     */
    @POST("ZZB/bl/app_update_reserve_state")
    Observable<Object> updateReservationStatus(@Query("rid") String rid, @Query("state") String state);
}
