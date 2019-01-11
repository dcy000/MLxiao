package com.gcml.module_health_profile.fragments;

import android.os.Bundle;
import android.view.View;

import com.gcml.common.utils.base.RecycleBaseFragment;
import com.gcml.module_health_profile.R;

public class BloodpressureFollowupFragment extends RecycleBaseFragment{
    public static BloodpressureFollowupFragment instance(){
        return new BloodpressureFollowupFragment();
    }
    @Override
    protected int initLayout() {
        return R.layout.fragment_bloodpressure_followup;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

    }
}
