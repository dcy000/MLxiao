package com.example.han.referralproject.single_measure.network;

import com.example.han.referralproject.BuildConfig;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.single_measure.bean.ApiResponse;
import com.example.han.referralproject.single_measure.bean.DetectionData;
import com.example.han.referralproject.util.LocalShared;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;

/**
 * Created by gzq on 2018/8/19.
 */

public class HealthMeasureApi {
    public static final String BasicUrl= BuildConfig.SERVER_ADDRESS;
    //血压测量惯用手
    public static final String DETECTION_BLOOD_HAND = BasicUrl + "ZZB/api/healthMonitor/detection/hypertension/hand/";
    //测量数据上传
    public static final String DETECTION_DATA = BasicUrl + "ZZB/api/healthMonitor/detection/";
    //首诊结果获取
    public static final String DETECTION_RESULT = BasicUrl + "ZZB/api/healthMonitor/detection/result/";
    //获取诊断信息
    public static final String GET_DIAGNOSE_INFO = BasicUrl + "ZZB/api/healthMonitor/hypertension/diagnose/";
    //血压周报告、月报告接口
    public static final String WeeklyOrMonthlyReport = BasicUrl + "ZZB/api/healthMonitor/report/hypertension/week/";
    /**
     * 原发性高血压 修改
     */
    public static final String POST_ORIGIN_HYPERTENTION = BasicUrl + "/ZZB/api/healthMonitor/hypertension/diagnose/primary/";

    /**
     * 新的上传测量数据的接口
     */
    public static void postMeasureData(ArrayList<DetectionData> datas, final NetworkCallback callback) {
        OkGo.<String>post(DETECTION_DATA + LocalShared.getInstance(MyApplication.getInstance()).getUserId() + "/")
                .upJson(new Gson().toJson(datas))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (!response.isSuccessful()) {
                            callback.onError();
                            return;
                        }
                        String body = response.body();
                        try {
                            ApiResponse<Object> apiResponse = new Gson().fromJson(body,
                                    new TypeToken<ApiResponse<Object>>() {
                                    }.getType());
                            if (apiResponse.isSuccessful()) {
                                callback.onSuccess(body);
                                return;
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        callback.onError();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        callback.onError();
                    }
                });
    }

    /**
     * 获取血压测量诊断结果信息
     * @param userId
     * @param callback
     */
    public static void getDiagnoseInfo(String userId, StringCallback callback) {
        OkGo.<String>get(GET_DIAGNOSE_INFO + userId + "/")
                .params("userId", userId)
                .execute(callback);
    }

    public static void postOriginHypertension(String hypertensionPrimaryState, String userId, StringCallback callback) {
        OkGo.<String>post(POST_ORIGIN_HYPERTENTION + userId + "/")
                .params("userId", userId)
                .params("hypertensionPrimaryState", hypertensionPrimaryState)
                .execute(callback);
    }

    /**
     * 获取诊断信息-->重新生成方案
     */
    public static void getDiagnoseInfoNew(String userId, StringCallback callback) {
        OkGo.<String>get(GET_DIAGNOSE_INFO + userId + "/new/")
                .params("userId", userId)
                .execute(callback);
    }
}
