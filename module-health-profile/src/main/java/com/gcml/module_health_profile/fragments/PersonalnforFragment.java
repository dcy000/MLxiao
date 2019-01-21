package com.gcml.module_health_profile.fragments;

import android.os.Bundle;
import android.view.View;

import com.gcml.common.utils.base.RecycleBaseFragment;
import com.gcml.module_health_profile.R;

public class PersonalnforFragment extends RecycleBaseFragment {
    public static PersonalnforFragment instance() {
        return new PersonalnforFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_personal_info;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

    }
}
