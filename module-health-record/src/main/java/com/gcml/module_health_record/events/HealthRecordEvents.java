package com.gcml.module_health_record.events;

import com.gcml.module_health_record.HealthRecordActivity;
import com.gzq.lib_core.base.ui.IEvents;
import com.gzq.lib_core.utils.ActivityUtils;

public class HealthRecordEvents implements IEvents {
    @Override
    public void onEvent(String tag, Object... objects) {
        switch (tag){
            case "HealthRecord>BloodPressure":
                //血压历史记录
                HealthRecordActivity.startActivity(ActivityUtils.currentActivity(), 1);
                break;
            case "HealthRecord>Temperature":
                //体温历史记录
                HealthRecordActivity.startActivity(ActivityUtils.currentActivity(), 0);
                break;
            case "HealthRecord>Weight":
                //体重历史记录
                HealthRecordActivity.startActivity(ActivityUtils.currentActivity(), 8);
                break;
            case "HealthRecord>Bloodoxygen":
                //血氧历史记录
                HealthRecordActivity.startActivity(ActivityUtils.currentActivity(), 3);
                break;
            case "HealthRecord>Ecg":
                //心电历史记录
                HealthRecordActivity.startActivity(ActivityUtils.currentActivity(), 7);
                break;
            case "HealthRecord>ThreeInOne":
                //三合一历史记录>胆固醇
                HealthRecordActivity.startActivity(ActivityUtils.currentActivity(), 5);
                break;
            case "HealthRecord>BloodSugar":
                //血糖历史记录
                HealthRecordActivity.startActivity(ActivityUtils.currentActivity(), 2);
                break;
        }
    }
}
