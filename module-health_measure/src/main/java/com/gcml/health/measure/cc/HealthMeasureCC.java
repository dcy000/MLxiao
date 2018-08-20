package com.gcml.health.measure.cc;

import android.content.Context;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.gcml.health.measure.ecg.XinDianDetectActivity;
import com.gcml.health.measure.first_diagnosis.HealthIntelligentDetectionActivity;
import com.gcml.health.measure.single_measure.MeasureChooseDeviceActivity;

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

    @Override
    public boolean onCall(CC cc) {
        CCResultActions.setCcId(cc.getCallId());
        Context context = cc.getContext();
        String actionName = cc.getActionName();
        switch (actionName) {
            case "SingleMeasure":
                MeasureChooseDeviceActivity.startActivity(context);
                break;
            case "FirstDiagnosis":
                HealthIntelligentDetectionActivity.startActivity(context);
                break;
            case "ToECG":
                //TODO:此页面的跳转逻辑应该重新梳理
                XinDianDetectActivity.startActivity(context,"cc");
                break;
            default:
                Timber.e("未匹配到任何操作Action");
                break;
        }
        return false;
    }
}
