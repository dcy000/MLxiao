package com.gcml.module_health_record.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gcml.module_health_record.R;
import com.gcml.module_health_record.bean.ECGHistory;
import com.gcml.module_health_record.others.XindianAdapter;
import com.gzq.lib_core.base.ui.BaseFragment;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.utils.ToastUtils;

import java.util.List;

public class HealthRecordECGFragment extends BaseFragment implements View.OnClickListener {

    private RecyclerView mXindiantu;
    private TextView mTvEmptyDataTips;
    private TextView mBtnGo;


    public void refreshData(List<ECGHistory> response, String temp) {
        mView.findViewById(R.id.view_empty_data).setVisibility(View.GONE);
        mXindiantu.setLayoutManager(new LinearLayoutManager(getContext()));
        if (isAdded()) {
            mXindiantu.setAdapter(new XindianAdapter(R.layout.health_record_item_message, response,
                    getResources().getStringArray(R.array.ecg_measureres)));
        }
    }

    public void refreshErrorData(String message) {
        ToastUtils.showShort(message);
        mTvEmptyDataTips.setText("啊哦!你还没有测量数据");
        mView.findViewById(R.id.view_empty_data).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_go) {
            emitEvent("skip2Ecg");
        } else {
        }
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.health_record_fragment_health_record_ecg;
    }

    @Override
    public void initParams(Bundle bundle) {

    }

    @Override
    public void initView(View view) {
        mXindiantu = view.findViewById(R.id.xindiantu);
        mTvEmptyDataTips = (TextView) view.findViewById(R.id.tv_empty_data_tips);
        mBtnGo = (TextView) view.findViewById(R.id.btn_go);
        mBtnGo.setOnClickListener(this);
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }
}
