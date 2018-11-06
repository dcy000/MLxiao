package com.gcml.common.gzq;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/10/22 14:25
 * created by: gzq
 * description: TODO
 */
public class TestXienActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_xien);
        BluetoothClientManager.init(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new HandRing_Fragment()).commit();
    }

}
