package com.gcml.common.gzq;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gcml.common.demo.R;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Xien_Fragment;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Xien_PresenterImp;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/10/22 14:25
 * created by: gzq
 * description: TODO
 */
public class TestXienActivity extends AppCompatActivity implements IView {
    private static final String TAG = "TestXienActivity";
    private Bloodpressure_Xien_PresenterImp xienPresenterImp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_xien);
        getSupportFragmentManager().beginTransaction().add(R.id.frame,new Bloodpressure_Xien_Fragment()).commit();
        BluetoothClientManager.init(this);
        xienPresenterImp = new Bloodpressure_Xien_PresenterImp(this,
                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, "88:1B:99:09:94:1F", "Dual-SPP"));
    }

    @Override
    public void updateData(String... datas) {
        if (datas.length == 1) {
            Log.e(TAG, "updateData: " + datas[0]);
        } else if (datas.length == 3) {
            Log.e(TAG, "updateData: 高压：" + datas[0] + "低压：" + datas[1] + "脉搏：" + datas[2]);
        }
    }

    @Override
    public void updateState(String state) {
        Log.e(TAG, "updateState: " + state);
    }

    @Override
    public Context getThisContext() {
        return this;
    }
}