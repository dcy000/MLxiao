package com.gcml.health.measure.cc;

import android.content.Context;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.gcml.health.measure.single_measure.MeasureChooseDeviceActivity;

public class ChooseDevicesComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.health.measure";
    }

    @Override
    public boolean onCall(CC cc) {
        CCResultActions.setCcId(cc.getCallId());
        Context context = cc.getContext();
        Boolean isFaceSkip = cc.getParamItem("isFaceSkip");
        MeasureChooseDeviceActivity.startActivity(context, isFaceSkip);
        return false;
    }
}
