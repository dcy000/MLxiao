package com.gzq.lib_bluetooth.service;

import com.gzq.lib_bluetooth.bean.DetectionData;
import com.gzq.lib_bluetooth.bean.DetectionResult;
import com.gzq.lib_bluetooth.bean.DiagnoseInfoBean;
import com.gzq.lib_bluetooth.bean.NewWeeklyOrMonthlyBean;
import com.gzq.lib_core.http.model.HttpResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BluetoothAPI {
    /**
     * 新的上传数据的接口
     *
     * @param userId
     * @param datas
     * @return
     */
    @POST("ZZB/api/healthMonitor/detection/{userId}/")
    Observable<HttpResult<List<DetectionResult>>> postMeasureData(@Path("userId") String userId, @Body ArrayList<DetectionData> datas);

    /**
     * 检查血压和血糖是否是正常范围内的数据
     *
     * @param userId
     * @param datas
     * @return
     */
    @POST("ZZB/api/healthMonitor/detection/{userId}/check/")
    Observable<HttpResult<Object>> checkIsNormalData(@Path("userId") String userId, @Body List<DetectionData> datas);

    /**
     * 获取周报告或者月报告
     * @param userId
     * @param endTimeStamp
     * @param page
     * @return
     */
    @GET("ZZB/api/healthMonitor/report/hypertension/week/")
    Observable<HttpResult<NewWeeklyOrMonthlyBean>> getWeeklyOrMonthlyReport(
            @Query("userId") String userId,
            @Query("endTimeStamp") long endTimeStamp,
            @Query("num") String page);

    /**
     * 获取高血压诊断信息
     * @param userId
     * @return
     */
    @GET("ZZB/api/healthMonitor/hypertension/diagnose/{userId}/")
    Observable<HttpResult<DiagnoseInfoBean>> getDiagnoseInfo(@Path("userId")String userId);

    /**
     * 生成新的健康方案
     * @param userId
     * @return
     */
    @GET("ZZB/api/healthMonitor/hypertension/diagnose/{userId}/new/")
    Observable<HttpResult<DiagnoseInfoBean>> getDiagnoseInfoNew(@Path("userId")String userId);
}
