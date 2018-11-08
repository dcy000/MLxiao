package com.gcml.common.gzq;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.demo.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Xien_PresenterImp;
import com.gcml.module_blutooth_devices.others.HandRing_Fragment;
import com.gcml.module_blutooth_devices.others.HandRing_Tongleda_PresenterImp;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Simaide_PresenterImp;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/10/22 14:25
 * created by: gzq
 * description: TODO
 */
public class TestXienActivity extends AppCompatActivity implements IView {

    private Weight_Simaide_PresenterImp dr01;
    private static final String TAG = "TestXienActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_xien);
        BluetoothClientManager.init(this);

        dr01 = new Weight_Simaide_PresenterImp(this,
                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, "ED:67:27:64:6A:20", "dr01"));
    }

    @Override
    public void updateData(String... datas) {
        Log.e(TAG, "updateData: "+datas[0] );
    }

    @Override
    public void updateState(String state) {
        Log.e(TAG, "updateState: "+state );
    }

    @Override
    public Context getThisContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dr01.onDestroy();
    }
}
