package com.gzq.test_all_devices;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Self_PresenterImp;
import com.gcml.module_blutooth_devices.ecg_devices.ECG_Fragment;

public class TestActivity extends AppCompatActivity implements IView {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ECG_Fragment ecg_fragment = new ECG_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, ecg_fragment).commit();
        new Bloodpressure_Self_PresenterImp(this,
                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_NAME,"eBlood-Pressure"));

    }

    @Override
    public void updateData(String... datas) {

    }

    @Override
    public void updateState(String state) {

    }

    @Override
    public Context getThisContext() {
        return this.getBaseContext();
    }
}
