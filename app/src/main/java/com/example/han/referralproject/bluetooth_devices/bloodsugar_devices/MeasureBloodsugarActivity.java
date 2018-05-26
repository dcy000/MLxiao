package com.example.han.referralproject.bluetooth_devices.bloodsugar_devices;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bluetooth_devices.base.IView;

public class MeasureBloodsugarActivity extends BaseActivity implements IView{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_bloodsugar);
    }

    @Override
    public void updateData(String... datas) {

    }

    @Override
    public void updateState(int state) {

    }
}
