package com.gzq.test_all_devices;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.gcml.module_blutooth_devices.base.BaseFragment;
import com.gcml.module_blutooth_devices.base.DealVoiceAndJump;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.utils.ToastTool;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Fragment;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Fragment;
import com.gcml.module_blutooth_devices.ecg_devices.ECG_Fragment;
import com.gcml.module_blutooth_devices.fingerprint_devices.Fingerpint_Fragment;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Fragment;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Fragment;

public class AllMeasureActivity extends AppCompatActivity {
    private BaseFragment baseFragment;
    private BluetoothBean bluetoothBean;
    private LinearLayout mLlBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_measure);
        initView();
        dealLogic();
    }

    private void dealLogic() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        switch (bluetoothBean.getMeasureType()) {
            case IPresenter.MEASURE_TEMPERATURE://体温测量
                if (baseFragment == null) {
                    baseFragment = new Temperature_Fragment();
//                    bundle.putString(IPresenter.BRAND, bluetoothBean.getBluetoothName());
//                    bundle.putInt(IPresenter.MEASURE_TYPE, IPresenter.SEARCH_ALL);
//                    bundle.putString("address", bluetoothBean.getAddress());
//                    baseFragment.setArguments(bundle);
                }
//                fragmentTransaction.replace(R.id.frame, baseFragment);
                break;
            case IPresenter.MEASURE_BLOOD_PRESSURE://血压
                if (baseFragment == null) {
//                    baseFragment = new Bloodpressure_Fragment();
//                    bundle.putString(IPresenter.BRAND, bluetoothBean.getBluetoothName());
//                    bundle.putInt(IPresenter.MEASURE_TYPE, IPresenter.SEARCH_ALL);
//                    bundle.putString("address", bluetoothBean.getAddress());
//                    baseFragment.setArguments(bundle);
                }
//                fragmentTransaction.replace(R.id.frame, baseFragment);
                break;
            case IPresenter.MEASURE_BLOOD_SUGAR://血糖
                if (baseFragment == null) {
                    baseFragment = new Bloodsugar_Fragment();
//                    bundle.putString(IPresenter.BRAND, bluetoothBean.getBluetoothName());
//                    bundle.putInt(IPresenter.MEASURE_TYPE, IPresenter.SEARCH_ALL);
//                    bundle.putString("address", bluetoothBean.getAddress());
//                    baseFragment.setArguments(bundle);
                }
//                fragmentTransaction.replace(R.id.frame, baseFragment);
                break;
            case IPresenter.MEASURE_BLOOD_OXYGEN://血氧
                if (baseFragment == null) {
                    baseFragment = new Bloodoxygen_Fragment();
//                    bundle.putString(IPresenter.BRAND, bluetoothBean.getBluetoothName());
//                    bundle.putInt(IPresenter.MEASURE_TYPE, IPresenter.SEARCH_ALL);
//                    bundle.putString("address", bluetoothBean.getAddress());
//                    baseFragment.setArguments(bundle);
                }
//                fragmentTransaction.replace(R.id.frame, baseFragment);
                break;
            case IPresenter.MEASURE_WEIGHT://体重
                if (baseFragment == null) {
                    baseFragment = new Weight_Fragment();
//                    bundle.putString(IPresenter.BRAND, bluetoothBean.getBluetoothName());
//                    bundle.putInt(IPresenter.MEASURE_TYPE, IPresenter.SEARCH_ALL);
//                    bundle.putString("address", bluetoothBean.getAddress());
//                    baseFragment.setArguments(bundle);
                }
//                fragmentTransaction.replace(R.id.frame, baseFragment);
                break;
            case IPresenter.MEASURE_ECG:
                if (baseFragment == null) {
                    baseFragment = new ECG_Fragment();
//                    bundle.putString(IPresenter.BRAND, bluetoothBean.getBluetoothName());
//                    bundle.putInt(IPresenter.MEASURE_TYPE, IPresenter.SEARCH_ALL);
//                    bundle.putString("address", bluetoothBean.getAddress());
//                    baseFragment.setArguments(bundle);
                }
//                fragmentTransaction.replace(R.id.frame, baseFragment);
                break;
            case IPresenter.MEASURE_OTHERS://三合一
                break;
            case IPresenter.CONTROL_FINGERPRINT:
                if (baseFragment == null) {
                    baseFragment = new Fingerpint_Fragment();
//                    bundle.putString(IPresenter.BRAND, bluetoothBean.getBluetoothName());
//                    bundle.putInt(IPresenter.MEASURE_TYPE, IPresenter.SEARCH_ALL);
//                    bundle.putString("address", bluetoothBean.getAddress());
//                    baseFragment.setArguments(bundle);
                }
//                fragmentTransaction.replace(R.id.frame, baseFragment);
                break;
        }
        bundle.putString(IPresenter.BRAND, bluetoothBean.getBluetoothName());
        bundle.putInt(IPresenter.MEASURE_TYPE, IPresenter.SEARCH_ALL);
        bundle.putString("address", bluetoothBean.getAddress());
        baseFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame, baseFragment).commit();

        if (baseFragment != null) {
            baseFragment.setOnDealVoiceAndJumpListener(new DealVoiceAndJump() {
                @Override
                public void updateVoice(String voice) {
                }

                @Override
                public void jump2HealthHistory(int type) {
                    ToastTool.showShort("跳转到历史记录");
                }

                @Override
                public void jump2DemoVideo(int type) {
                    ToastTool.showShort("跳转到演示视频");
                }
            });
        }
    }

    private void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            bluetoothBean = intent.getParcelableExtra("bluetoothbean");
        }
        mLlBack = (LinearLayout) findViewById(R.id.ll_back);
        mLlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baseFragment = null;
    }
}
