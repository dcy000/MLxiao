package com.gcml.health.measure.first_diagnosis.fragment;

import android.support.v4.app.Fragment;

import com.gcml.common.service.IBloodsugarTimeFragmentProvider;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/health/measure/bloodsugar/time/fragment")
public class BloodsugarTimeFragmentProviderImp implements IBloodsugarTimeFragmentProvider {
    @Override
    public Fragment getHealthSelectSugarDetectionTimeFragment() {
        return new HealthSelectSugarDetectionTimeFragment();
    }
}
