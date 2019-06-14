package com.gcml.health.measure.first_diagnosis.fragment;

import android.support.v4.app.Fragment;

import com.gcml.common.service.IChooseDevicesFragmentProvider;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/health/measure/choose/devices/provider")
public class ChooseDevicesFragmentProviderImp implements IChooseDevicesFragmentProvider {
    @Override
    public Fragment getChooseDevicesFragment() {
        return new HealthChooseDevicesFragment();
    }
}
