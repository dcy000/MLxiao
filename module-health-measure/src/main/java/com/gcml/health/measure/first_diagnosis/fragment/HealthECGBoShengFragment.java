package com.gcml.health.measure.first_diagnosis.fragment;

import android.os.Bundle;
import android.view.View;

import com.gcml.health.measure.R;
import com.gcml.module_blutooth_devices.ecg.ECGFragment;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/11/2 14:28
 * created by: gzq
 * description: 风险评估流程中心电测量
 */
public class HealthECGBoShengFragment extends ECGFragment {
    @Override
    protected void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        mBtnHealthHistory.setVisibility(View.GONE);
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnChangeDevice.setVisibility(View.GONE);
        mTvNext.setVisibility(View.VISIBLE);

        setBtnClickableState(false);
    }

    @Override
    protected void clickBtnNext() {
        if (fragmentChanged != null) {
            fragmentChanged.onFragmentChanged(this, null);
        }
    }

    private void setBtnClickableState(boolean enableClick) {
        if (enableClick) {
            mTvNext.setClickable(true);
            mTvNext.setBackgroundResource(R.drawable.bluetooth_btn_health_history_set);
        } else {
            mTvNext.setBackgroundResource(R.drawable.bluetooth_btn_unclick_set);
            mTvNext.setClickable(false);
        }
    }

    @Override
    protected void onMeasureFinished(String... results) {
        setBtnClickableState(true);
    }
}
