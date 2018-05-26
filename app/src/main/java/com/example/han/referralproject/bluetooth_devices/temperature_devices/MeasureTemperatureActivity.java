package com.example.han.referralproject.bluetooth_devices.temperature_devices;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bluetooth_devices.base.DiscoverDevicesSetting;
import com.example.han.referralproject.bluetooth_devices.base.IPresenter;
import com.example.han.referralproject.bluetooth_devices.base.IView;

public class MeasureTemperatureActivity extends BaseActivity implements IView{
    private Temperature_Zhiziyun_PresenterImp presenterImp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_temperature);
        presenterImp = new Temperature_Zhiziyun_PresenterImp(this, new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, "49:00:00:00:00:00"));
    }

    @Override
    public void updateData(String... datas) {

    }

    @Override
    public void updateState(int state) {

    }
}
