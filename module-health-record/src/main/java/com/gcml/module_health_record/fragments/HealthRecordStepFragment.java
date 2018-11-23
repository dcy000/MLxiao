package com.gcml.module_health_record.fragments;

import android.os.Bundle;
import android.view.View;

import com.gcml.module_health_record.R;
import com.gzq.lib_core.base.ui.BaseFragment;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/11/5 17:33
 * created by: gzq
 * description: 记步
 */
public class HealthRecordStepFragment extends BaseFragment {

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.health_record_fragment_health_record;
    }

    @Override
    public void initParams(Bundle bundle) {

    }

    @Override
    public void initView(View view) {

    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }
}
