package com.gcml.module_health_record.network;


import com.gcml.common.data.UserSpHelper;
import com.gcml.module_health_record.BuildConfig;
import com.gcml.module_health_record.bean.BUA;
import com.gcml.module_health_record.bean.BloodOxygenHistory;
import com.gcml.module_health_record.bean.BloodPressureHistory;
import com.gcml.module_health_record.bean.BloodSugarHistory;
import com.gcml.module_health_record.bean.CholesterolHistory;
import com.gcml.module_health_record.bean.ECGHistory;
import com.gcml.module_health_record.bean.HeartRateHistory;
import com.gcml.module_health_record.bean.PulseHistory;
import com.gcml.module_health_record.bean.TemperatureHistory;
import com.gcml.module_health_record.bean.WeightHistory;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

public class HealthRecordNetworkApi {
    public static final String BasicUrl = BuildConfig.SERVER_ADDRESS;
    public static final String Get_HealthRecord = BasicUrl + "ZZB/br/cl_data";//正式服务器


    /**
     * 获取体温历史数据
     *
     * @param successCallback
     */


    public static void getTemperatureHistory(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<TemperatureHistory>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();

        params.put("bid", UserSpHelper.getUserId());
//        params.put("bid","100001");
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<TemperatureHistory>>() {
                }.getType(),
                successCallback, failedCallback);
    }

    /**
     * 获取血压的历史数据
     *
     * @param temp
     * @param successCallback
     */
    public static void getBloodpressureHistory(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<BloodPressureHistory>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", UserSpHelper.getUserId());
//        params.put("bid","100001");
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<BloodPressureHistory>>() {
                }.getType(),
                successCallback, failedCallback);
    }

    /**
     * 血糖
     *
     * @param temp
     * @param successCallback
     */

    public static void getBloodSugarHistory(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<BloodSugarHistory>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", UserSpHelper.getUserId());
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<BloodSugarHistory>>() {
                }.getType(),
                successCallback, failedCallback);
    }

    /**
     * 血氧
     *
     * @param temp
     * @param successCallback
     */
    public static void getBloodOxygenHistory(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<BloodOxygenHistory>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", UserSpHelper.getUserId());
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<BloodOxygenHistory>>() {
                }.getType(),
                successCallback, failedCallback);
    }

    /**
     * 心率
     *
     * @param temp
     * @param successCallback
     */
    public static void getHeartRateHistory(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<HeartRateHistory>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", UserSpHelper.getUserId());
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<HeartRateHistory>>() {
                }.getType(),
                successCallback, failedCallback);
    }

    /**
     * 脉搏
     *
     * @param temp
     * @param successCallback
     */
    public static void getPulseHistory(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<PulseHistory>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", UserSpHelper.getUserId());
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<PulseHistory>>() {
                }.getType(),
                successCallback, failedCallback);
    }

    /**
     * 胆固醇
     *
     * @param temp
     * @param successCallback
     */
    public static void getCholesterolHistory(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<CholesterolHistory>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", UserSpHelper.getUserId());
//        params.put("bid","100001");
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<CholesterolHistory>>() {
                }.getType(),
                successCallback, failedCallback);
    }

    /**
     * 血尿酸
     *
     * @param temp
     * @param successCallback
     */
    public static void getBUAHistory(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<BUA>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid",UserSpHelper.getUserId());
//        params.put("bid","100001");
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<BUA>>() {
                }.getType(),
                successCallback, failedCallback);
    }

    /**
     * 心电
     *
     * @param temp
     * @param successCallback
     */
    public static void getECGHistory(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<ECGHistory>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bid", UserSpHelper.getUserId());
//        params.put("bid", "100001");
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<ECGHistory>>() {
                }.getType(),
                successCallback, failedCallback);
    }

    /**
     * 体重
     *
     * @param start
     * @param end
     * @param temp
     * @param successCallback
     * @param failedCallback
     */
    public static void getWeight(String start, String end, String temp, NetworkManager.SuccessCallback<ArrayList<WeightHistory>> successCallback, NetworkManager.FailedCallback failedCallback
    ) {
        HashMap<String, String> params = new HashMap<>();

        params.put("bid",UserSpHelper.getUserId());
//        params.put("bid","100001");
        params.put("temp", temp);
        params.put("starttime", start);
        params.put("endtime", end);
        NetworkManager.getInstance().getResultClass(Get_HealthRecord, params, new TypeToken<ArrayList<WeightHistory>>() {
                }.getType(),
                successCallback, failedCallback);
    }

}
