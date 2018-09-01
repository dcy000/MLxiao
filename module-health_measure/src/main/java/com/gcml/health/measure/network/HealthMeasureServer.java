package com.gcml.health.measure.network;

import com.gcml.common.repository.Api;
import com.gcml.common.repository.http.ApiResult;
import com.gcml.health.measure.first_diagnosis.bean.DeviceBean;
import com.gcml.health.measure.first_diagnosis.bean.FirstReportBean;
import com.gcml.health.measure.first_diagnosis.bean.PostDeviceBean;
import com.gcml.health.measure.health_inquiry.bean.HealthInquiryBean;
import com.gcml.health.measure.health_inquiry.bean.HealthInquiryPostBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/28 17:01
 * created by:gzq
 * description:TODO
 */
public interface HealthMeasureServer {
    @GET("ZZB/api/healthMonitor/questionnaire/health/survey/")
    Observable<ApiResult<HealthInquiryBean>> getHealthInquiryQuestions();

    @POST("ZZB/api/healthMonitor/questionnaire/health/survey/{userId}/")
    Observable<ApiResult<Object>> postHealthInquiryAnswers(@Path("userId") String userId, @Body HealthInquiryPostBean postBean);

    @GET("ZZB/api/health/device/user/{userId}/")
    Observable<ApiResult<List<DeviceBean>>> getUserHasedDevices(@Path("userId") String userId);
    @POST("ZZB/api/health/device/user/{userId}/")
    Observable<ApiResult<Object>> postUserHasedDevices(@Path("userId") String userId, @Body List<PostDeviceBean> beans);
    @GET("ZZB/api/healthMonitor/report/risk/{userId}/")
    Observable<ApiResult<FirstReportBean>> getFirstReport(@Path("userId") String userId);
}
