package com.example.han.referralproject.events;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.han.referralproject.AllMeasureActivity;
import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.WelcomeActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.ecg.ECGCompatActivity;
import com.example.han.referralproject.intelligent_diagnosis.MonthlyReportActivity;
import com.example.han.referralproject.intelligent_diagnosis.WeeklyReportActivity;
import com.example.han.referralproject.recharge.PayActivity;
import com.example.module_doctor_advisory.ui.RecoDocActivity;
import com.example.module_call.ui.NimCallActivity;
import com.example.module_doctor_advisory.ui.AppraiseActivity;
import com.example.module_login.ui.ChooseLoginTypeActivity;
import com.example.module_register.ui.normal.ConfirmContractActivity;
import com.example.module_register.ui.normal.SignUp1NameActivity;
import com.example.module_register.ui.sample.SignUp01NameActivity;
import com.gcml.module_health_record.HealthRecordActivity;
import com.gzq.lib_bluetooth.BluetoothConstants;
import com.gzq.lib_core.base.ui.IEvents;
import com.gzq.lib_core.utils.ActivityUtils;

public class AppEvents implements IEvents {
    @Override
    public void onEvent(String tag, Object... objects) {
        Activity activity = ActivityUtils.currentActivity();
        switch (tag) {
            case "skip2BloodPressure":
                //血压
                Bundle bloodpressure = new Bundle();
                bloodpressure.putString(BluetoothConstants.TYPE_OF_MEASURE, BluetoothConstants.KEY_SPHYGMOMANOMETER);
                ActivityUtils.skipActivity(AllMeasureActivity.class, bloodpressure);
                break;
            case "skip2Temperature":
                //体温
                Bundle tiwen = new Bundle();
                tiwen.putString(BluetoothConstants.TYPE_OF_MEASURE, BluetoothConstants.KEY_TEMPERATURE);
                ActivityUtils.skipActivity(AllMeasureActivity.class, tiwen);
                break;
            case "skip2Weight":
                //体重
                Bundle weight = new Bundle();
                weight.putString(BluetoothConstants.TYPE_OF_MEASURE, BluetoothConstants.KEY_WEIGHING_SCALE);
                ActivityUtils.skipActivity(AllMeasureActivity.class, weight);
                break;
            case "skip2Bloodoxygen":
                //血氧
                Bundle bloodoxygen = new Bundle();
                bloodoxygen.putString(BluetoothConstants.TYPE_OF_MEASURE, BluetoothConstants.KEY_OXIMETER);
                ActivityUtils.skipActivity(AllMeasureActivity.class, bloodoxygen);
                break;
            case "skip2Ecg":
                //心电
                ECGCompatActivity.startActivity(activity);
                break;
            case "skip2ThreeInOne":
                //三合一
                Bundle triple = new Bundle();
                triple.putString(BluetoothConstants.TYPE_OF_MEASURE, BluetoothConstants.KEY_TRIPLE);
                ActivityUtils.skipActivity(AllMeasureActivity.class, triple);
                break;
            case "skip2BloodSugar":
                //血糖
                Bundle bloodsugar = new Bundle();
                bloodsugar.putString(BluetoothConstants.TYPE_OF_MEASURE, BluetoothConstants.KEY_BLOOD_GLUCOSE_METER);
                ActivityUtils.skipActivity(AllMeasureActivity.class, bloodsugar);
                break;
            case "skip2MainActivity":
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
            case "Skip2RecoDocActivity":
                ActivityUtils.skipActivity(RecoDocActivity.class);
                break;
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
            case "NimCall":
                if (objects != null) {
                    String callId = objects[0] + "";
                    NimCallActivity.launch(ActivityUtils.currentActivity(), callId);
                }
                break;
            case "skip2ConfirmContractActivity":
                if (objects != null) {
                    String docId = objects[0] + "";
                    ConfirmContractActivity.start(ActivityUtils.currentActivity(), docId);
                }
                break;
            case "skip2PayActivity":
                ActivityUtils.skipActivity(PayActivity.class);
                break;
            case "skip2AppraiseActivity":
                if (objects != null) {
                    Integer daid = (Integer) objects[0];
                    String doid = objects[1] + "";
                    Intent intent = new Intent(ActivityUtils.currentActivity(), AppraiseActivity.class);
                    intent.putExtra("daid", daid);
                    intent.putExtra("doid", doid);
                    ActivityUtils.currentActivity().startActivity(intent);
                }
                break;
            case "skip2WifiConnectActivity":
                ActivityUtils.skipActivity(WifiConnectActivity.class);
                break;
            case "skip2WelcomeActivity-NewTask":
                Intent intent = new Intent(ActivityUtils.currentActivity(), WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityUtils.currentActivity().startActivity(intent);
                break;
            case "skip2ChooseLoginTypeActivity":
                ActivityUtils.skipActivity(ChooseLoginTypeActivity.class);
                break;
            case "skip2SignUp1NameActivity":
                ActivityUtils.skipActivity(SignUp1NameActivity.class);
                break;
            case "skip2SignUp01NameActivity":
                ActivityUtils.skipActivity(SignUp01NameActivity.class);
                break;
        }
    }


}
