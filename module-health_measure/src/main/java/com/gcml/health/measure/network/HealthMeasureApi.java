package com.gcml.health.measure.network;

import com.gcml.health.measure.BuildConfig;
import com.gcml.health.measure.first_diagnosis.bean.ApiResponse;
import com.gcml.health.measure.first_diagnosis.bean.DetectionData;
import com.gcml.health.measure.manifest.HealthMeasureSPManifest;
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
    public static final String DETECTION_BLOOD_HAND = BasicUrl + "/ZZB/api/healthMonitor/detection/hypertension/hand/";
    //测量数据上传
    public static final String DETECTION_DATA = BasicUrl + "/ZZB/api/healthMonitor/detection/";
    //首诊结果获取
    public static final String DETECTION_RESULT = BasicUrl + "/ZZB/api/healthMonitor/detection/result/";

    /**
     * 新的上传测量数据的接口
     */
    public static void postMeasureData(ArrayList<DetectionData> datas, final NetworkCallback callback) {
        OkGo.<String>post(DETECTION_DATA + HealthMeasureSPManifest.getUserId() + "/")
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
}
