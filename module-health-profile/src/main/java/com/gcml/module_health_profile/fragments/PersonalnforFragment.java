package com.gcml.module_health_profile.fragments;

import android.os.Bundle;
import android.view.View;

import com.gcml.common.utils.base.RecycleBaseFragment;
import com.gcml.module_health_profile.R;

public class PersonalnforFragment extends RecycleBaseFragment {
    private String recordId;

    public static PersonalnforFragment instance(String recordId) {
        Bundle bundle = new Bundle();
        bundle.putString("recordId", recordId);
        PersonalnforFragment personalnforFragment = new PersonalnforFragment();
        personalnforFragment.setArguments(bundle);
        return personalnforFragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_personal_info;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        recordId=bundle.getString("recordId");
    }
}
