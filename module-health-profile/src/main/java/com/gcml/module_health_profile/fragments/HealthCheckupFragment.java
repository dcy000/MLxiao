package com.gcml.module_health_profile.fragments;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.divider.LinearLayoutDividerItemDecoration;
import com.gcml.common.utils.base.RecycleBaseFragment;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_health_profile.R;

import java.util.ArrayList;
import java.util.List;

public class HealthCheckupFragment extends RecycleBaseFragment implements View.OnClickListener {
    /**
     * 立即开始
     */
    private TextView mBtnNewRecord;
    private RecyclerView mRv;
    private List<String> mData;

    public static HealthCheckupFragment instance() {
        return new HealthCheckupFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_health_checkup;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mBtnNewRecord = (TextView) view.findViewById(R.id.btn_new_record);
        mBtnNewRecord.setOnClickListener(this);
        mRv = (RecyclerView) view.findViewById(R.id.rv);
        mData = new ArrayList<>();
        initRV();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        mData.add("2018年12月3日");
        mData.add("2018年8月23日");
        mData.add("2017年10月2日");
        mData.add("2016年5月28日");
    }

    private void initRV() {
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mRv.addItemDecoration(new LinearLayoutDividerItemDecoration(0, 40));
        mRv.setNestedScrollingEnabled(false);
        mRv.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.health_profile_item_checkup_record, mData) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_time, item);
                helper.getView(R.id.btn_see_detail).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showShort("点我干啥");
                    }
                });
            }
        });

    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_new_record) {
            CC.obtainBuilder("health_measure")
                    .setActionName("health.profile.addhealthcheckup")
                    .build()
                    .call();
        } else {
        }
    }
}
