package com.example.han.referralproject.events;

import android.app.Activity;
import android.content.Intent;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.activity.DetectActivity;
import com.example.han.referralproject.activity.SelectXuetangTimeActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.ecg.ECGCompatActivity;
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
                intent.setClass(activity, DetectActivity.class);
                intent.putExtra("type", "xueya");
                activity.startActivity(intent);
                break;
            case "HealthRecord>Temperature":
                //体温
                intent.setClass(activity, DetectActivity.class);
                intent.putExtra("type", "wendu");
                activity.startActivity(intent);
                break;
            case "HealthRecord>Weight":
                //体重
                intent.setClass(activity, DetectActivity.class);
                intent.putExtra("type", "tizhong");
                activity.startActivity(intent);
                break;
            case "HealthRecord>Bloodoxygen":
                //血氧
                intent.setClass(activity, DetectActivity.class);
                intent.putExtra("type", "xueyang");
                activity.startActivity(intent);

                break;
            case "HealthRecord>Ecg":
                //心电
                ECGCompatActivity.startActivity(activity);
                break;
            case "HealthRecord>ThreeInOne":
                //三合一
                intent.setClass(activity, SelectXuetangTimeActivity.class);
                intent.putExtra("type", "sanheyi");
                activity.startActivity(intent);
                break;
            case "HealthRecord>BloodSugar":
                //血糖
                intent.setClass(activity, SelectXuetangTimeActivity.class);
                intent.putExtra("type", "xuetang");
                activity.startActivity(intent);

                break;
            case "HealthRecord>skip2MainActivity":
                //跳转到主界面
                ActivityUtils.skipActivity(MainActivity.class);
                break;
            case "AuthFace>Skip2Wifi":
                //跳转到Wifi界面
                ActivityUtils.skipActivity(WifiConnectActivity.class);
                break;
        }
    }
}
