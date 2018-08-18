package com.gzq.test_all_devices;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.others.BreathHome_PresenterImp;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/18 14:30
 * created by:gzq
 * description:TODO
 */
public class TestBluetooth extends AppCompatActivity implements IView{

    private BreathHome_PresenterImp presenterImp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_bluetooth);
        presenterImp = new BreathHome_PresenterImp(this, new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC,
                "00:15:87:21:11:D9", "B810229665"));
    }

    @Override
    public void updateData(String... datas) {

    }

    @Override
    public void updateState(String state) {

    }

    @Override
    public Context getThisContext() {
        return this;
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenterImp.onDestroy();
    }
}
