package com.example.han.referralproject.events;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.han.referralproject.AllMeasureActivity;
import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.ecg.ECGCompatActivity;
import com.example.han.referralproject.intelligent_diagnosis.MonthlyReportActivity;
import com.example.han.referralproject.intelligent_diagnosis.WeeklyReportActivity;
import com.gzq.lib_bluetooth.BluetoothConstants;
import com.gzq.lib_core.base.ui.IEvents;
import com.gzq.lib_core.utils.ActivityUtils;

public class HealthRecordEvents implements IEvents {
    @Override
    public void onEvent(String tag, Object... objects) {
        Intent intent = new Intent();
        Activity activity = ActivityUtils.currentActivity();
        switch (tag) {
            case "HealthRecord>BloodPressure":
                //血压
                Bundle bloodpressure = new Bundle();
                bloodpressure.putString(BluetoothConstants.TYPE_OF_MEASURE, BluetoothConstants.KEY_SPHYGMOMANOMETER);
                ActivityUtils.skipActivity(AllMeasureActivity.class, bloodpressure);
                break;
            case "HealthRecord>Temperature":
                //体温
                Bundle tiwen = new Bundle();
                tiwen.putString(BluetoothConstants.TYPE_OF_MEASURE, BluetoothConstants.KEY_TEMPERATURE);
                ActivityUtils.skipActivity(AllMeasureActivity.class, tiwen);
                break;
            case "HealthRecord>Weight":
                //体重
                Bundle weight = new Bundle();
                weight.putString(BluetoothConstants.TYPE_OF_MEASURE, BluetoothConstants.KEY_WEIGHING_SCALE);
                ActivityUtils.skipActivity(AllMeasureActivity.class, weight);
                break;
            case "HealthRecord>Bloodoxygen":
                //血氧
                Bundle bloodoxygen = new Bundle();
                bloodoxygen.putString(BluetoothConstants.TYPE_OF_MEASURE, BluetoothConstants.KEY_OXIMETER);
                ActivityUtils.skipActivity(AllMeasureActivity.class, bloodoxygen);

                break;
            case "HealthRecord>Ecg":
                //心电
                ECGCompatActivity.startActivity(activity);
                break;
            case "HealthRecord>ThreeInOne":
                //三合一
                Bundle triple = new Bundle();
                triple.putString(BluetoothConstants.TYPE_OF_MEASURE, BluetoothConstants.KEY_TRIPLE);
                ActivityUtils.skipActivity(AllMeasureActivity.class, triple);
                break;
            case "HealthRecord>BloodSugar":
                //血糖
                Bundle bloodsugar = new Bundle();
                bloodsugar.putString(BluetoothConstants.TYPE_OF_MEASURE, BluetoothConstants.KEY_BLOOD_GLUCOSE_METER);
                ActivityUtils.skipActivity(AllMeasureActivity.class, bloodsugar);

                break;
            case "HealthRecord>skip2MainActivity":
                //跳转到主界面
                ActivityUtils.skipActivity(MainActivity.class);
                break;
            case "AuthFace>Skip2Wifi":
                //跳转到Wifi界面
                ActivityUtils.skipActivity(WifiConnectActivity.class);
                break;
            case "To_WeeklyReportActivity":
                ActivityUtils.skipActivity(WeeklyReportActivity.class);
                break;
            case "To_MonthlyReportActivity":
                ActivityUtils.skipActivity(MonthlyReportActivity.class);
                break;
        }
    }
}
