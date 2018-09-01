package com.gcml.health.measure.cc;

import android.content.Context;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.gcml.health.measure.ecg.XinDianDetectActivity;
import com.gcml.health.measure.first_diagnosis.FirstDiagnosisActivity;
import com.gcml.health.measure.first_diagnosis.HealthIntelligentDetectionActivity;
import com.gcml.health.measure.health_inquiry.HealthInquiryActivity;
import com.gcml.health.measure.single_measure.AllMeasureActivity;
import com.gcml.health.measure.single_measure.MeasureChooseDeviceActivity;
import com.gcml.module_blutooth_devices.base.IPresenter;

import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/20 16:45
 * created by:gzq
 * description:TODO
 */
public class HealthMeasureCC implements IComponent {
    @Override
    public String getName() {
        return "health_measure";
    }

    /**
     * 收到的操作Actions
     */
    interface ReceiveActionNames {
        /**
         * 跳转到AllMeasureActivity
         */
        String TO_ALL_MEASURE_ACTIVITY = "ToAllMeasureActivity";
        /**
         * 单测
         */
        String SINGLE_MEASURE = "SingleMeasure";
        /**
         * 首诊
         */
        String FIRST_DIAGNOSIS = "FirstDiagnosis";
        /**
         * 心电
         */
        String TO_ECG = "ToECG";
        /**
         * 健康调查
         */
        String TO_HEALTHINQUIRYACTIVITY="To_HealthInquiryActivity";
    }
    interface ReceiveKeys{
        String KEY_EXTRA_MEASURE_TYPE="measure_type";
    }
    @Override
    public boolean onCall(CC cc) {
        CCResultActions.setCcId(cc.getCallId());
        Context context = cc.getContext();
        String actionName = cc.getActionName();
        switch (actionName) {
            case ReceiveActionNames.SINGLE_MEASURE:
                MeasureChooseDeviceActivity.startActivity(context);
                break;
            case ReceiveActionNames.FIRST_DIAGNOSIS:
//                HealthIntelligentDetectionActivity.startActivity(context);
                FirstDiagnosisActivity.startActivity(context);
                break;
            case ReceiveActionNames.TO_ECG:
                //TODO:此页面的跳转逻辑应该重新梳理
                XinDianDetectActivity.startActivity(context, "cc");
                break;
            case ReceiveActionNames.TO_ALL_MEASURE_ACTIVITY:
                int param = cc.getParamItem(ReceiveKeys.KEY_EXTRA_MEASURE_TYPE);
                AllMeasureActivity.startActivity(context,param);
                break;
            case ReceiveActionNames.TO_HEALTHINQUIRYACTIVITY:
                HealthInquiryActivity.startActivity(context);
                break;
            default:
                Timber.e("未匹配到任何操作Action");
                break;
        }
        return false;
    }
}
