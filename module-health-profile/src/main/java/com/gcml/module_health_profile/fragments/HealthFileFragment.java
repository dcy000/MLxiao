package com.gcml.module_health_profile.fragments;

import android.os.Bundle;
import android.view.View;

import com.gcml.common.utils.base.RecycleBaseFragment;
import com.gcml.module_health_profile.R;

public class HealthFileFragment extends RecycleBaseFragment {
    public static HealthFileFragment instance() {
        return new HealthFileFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_health_file;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

    }
}