package com.gcml.health.measure.first_diagnosis.fragment;

import android.view.View;

import com.gcml.health.measure.R;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Fragment;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/27 14:08
 * created by:gzq
 * description:TODO
 */
public class HealthTemperatureDetectionFragment extends Temperature_Fragment {
    @Override
    public void onStart() {
        super.onStart();
        mBtnVideoDemo.setVisibility(View.GONE);
        mBtnHealthHistory.setText("下一步");
        mBtnHealthHistory.setBackgroundResource(R.drawable.bluetooth_btn_unclick_set);
    }


}
