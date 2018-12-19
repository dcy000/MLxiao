package com.gcml.module_health_record.network;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.RxUtils;
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

import java.util.List;

import io.reactivex.Observable;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/20 10:32
 * created by:gzq
 * description:TODO
 */
public class HealthRecordRepository {
    private static HealthRecordServer healthRecordServer = RetrofitHelper.service(HealthRecordServer.class);

    public static Observable<List<TemperatureHistory>> getTemperatureHistory(String start, String end, String temp) {
        return healthRecordServer.getTemperatureHistory(UserSpHelper.getUserId(), start, end, temp).compose(RxUtils.apiResultTransformer());
    }

    public static Observable<List<BloodPressureHistory>> getBloodpressureHistory(String start, String end, String temp) {
        return healthRecordServer.getBloodpressureHistory(UserSpHelper.getUserId(), start, end, temp).compose(RxUtils.apiResultTransformer());
    }

    public static Observable<List<BloodSugarHistory>> getBloodSugarHistory(String start, String end, String temp) {
        return healthRecordServer.getBloodSugarHistory(UserSpHelper.getUserId(), start, end, temp).compose(RxUtils.apiResultTransformer());
    }

    public static Observable<List<BloodOxygenHistory>> getBloodOxygenHistory(String start, String end, String temp) {
        return healthRecordServer.getBloodOxygenHistory(UserSpHelper.getUserId(), start, end, temp).compose(RxUtils.apiResultTransformer());
    }

    public static Observable<List<HeartRateHistory>> getHeartRateHistory(String start, String end, String temp) {
        return healthRecordServer.getHeartRateHistory(UserSpHelper.getUserId(), start, end, temp).compose(RxUtils.apiResultTransformer());
    }


    public static Observable<List<PulseHistory>> getPulseHistory(String start, String end, String temp) {
        return healthRecordServer.getPulseHistory(UserSpHelper.getUserId(), start, end, temp).compose(RxUtils.apiResultTransformer());
    }

    public static Observable<List<CholesterolHistory>> getCholesterolHistory(String start, String end, String temp) {
        return healthRecordServer.getCholesterolHistory(UserSpHelper.getUserId(), start, end, temp).compose(RxUtils.apiResultTransformer());
    }

    public static Observable<List<BUA>> getBUAHistory(String start, String end, String temp) {
        return healthRecordServer.getBUAHistory(UserSpHelper.getUserId(), start, end, temp).compose(RxUtils.apiResultTransformer());
    }

    public static Observable<List<ECGHistory>> getECGHistory(String start, String end, String temp) {
        return healthRecordServer.getECGHistory(UserSpHelper.getUserId(), start, end, temp).compose(RxUtils.apiResultTransformer());
    }

    public static Observable<List<WeightHistory>> getWeight(String start, String end, String temp) {
        return healthRecordServer.getWeight(UserSpHelper.getUserId(), start, end, temp).compose(RxUtils.apiResultTransformer());
    }
}
