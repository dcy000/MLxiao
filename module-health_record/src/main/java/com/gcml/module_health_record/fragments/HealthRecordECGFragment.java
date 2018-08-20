package com.gcml.module_health_record.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gcml.lib_utils.base.RecycleBaseFragment;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_health_record.R;
import com.gcml.module_health_record.bean.ECGHistory;
import com.gcml.module_health_record.others.XindianAdapter;

import java.util.ArrayList;


public class HealthRecordECGFragment extends RecycleBaseFragment {

    private RecyclerView mXindiantu;

    @Override
    protected int initLayout() {
        return R.layout.health_record_fragment_health_record_ecg;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mXindiantu = view.findViewById(R.id.xindiantu);
    }


    public void refreshData(ArrayList<ECGHistory> response, String temp) {
        mXindiantu.setLayoutManager(new LinearLayoutManager(getContext()));
        mXindiantu.setAdapter(new XindianAdapter(R.layout.health_record_item_message, response));
    }

    public void refreshErrorData(String message) {
        ToastUtils.showShort(message);
    }
}
