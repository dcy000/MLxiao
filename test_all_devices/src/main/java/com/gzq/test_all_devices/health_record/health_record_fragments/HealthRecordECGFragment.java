package com.gzq.test_all_devices.health_record.health_record_fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.base.BaseFragment;
import com.gzq.test_all_devices.R;
import com.gzq.test_all_devices.formatte.XindianAdapter;
import com.gzq.test_all_devices.health_record_bean.ECGHistory;

import java.util.ArrayList;

public class HealthRecordECGFragment extends BaseFragment {

    private RecyclerView mXindiantu;

    @Override
    protected int initLayout() {
        return R.layout.fragment_health_record_ecg;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mXindiantu = (RecyclerView) view.findViewById(R.id.xindiantu);
    }


    public void refreshData(ArrayList<ECGHistory> response, String temp) {
        mXindiantu.setLayoutManager(new LinearLayoutManager(getContext()));
        mXindiantu.setAdapter(new XindianAdapter(R.layout.item_message, response));
    }

    public void refreshErrorData(String message) {
        ToastUtils.showShort(message);
    }
}
