package com.gcml.module_blutooth_devices.bloodoxygen_devices;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.lib_utils.data.SPUtil;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BaseFragment;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.utils.SearchWithDeviceGroupHelper;


public class Bloodoxygen_Fragment extends BaseFragment implements IView, View.OnClickListener {
    private TextView mBtnHealthHistory;
    private TextView mBtnVideoDemo;
    private TextView mTvResult;
    private BaseBluetoothPresenter bluetoothPresenter;
    private SearchWithDeviceGroupHelper helper;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_bloodoxygen;
    }

    @Override
    protected void initView(View view, final Bundle bundle) {
        mBtnHealthHistory = (TextView) view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = (TextView) view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvResult = (TextView) view.findViewById(R.id.tv_result);
        dealLogic(bundle);
    }

    private void dealLogic(Bundle bundle) {
        String address;
        String brand;
        if (bundle != null) {
            address = bundle.getString("address");
            brand = bundle.getString(IPresenter.BRAND);
            chooseConnectType(address, brand);
        } else {
            String sp_bloodoxygen = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODOXYGEN, "");
            if (TextUtils.isEmpty(sp_bloodoxygen)) {
                helper = new SearchWithDeviceGroupHelper(this, IPresenter.MEASURE_BLOOD_OXYGEN);
                helper.start();
            } else {
                String[] split = sp_bloodoxygen.split(",");
                if (split.length == 2) {
                    brand = split[0];
                    address = split[1];
                    chooseConnectType(address,brand);
                }

            }
        }
    }

    private void chooseConnectType(String address, String brand) {
        if (TextUtils.isEmpty(address)) {
            helper = new SearchWithDeviceGroupHelper(this, IPresenter.MEASURE_BLOOD_OXYGEN);
            helper.start();
        } else {
            switch (brand) {
                case "iChoice":
                    bluetoothPresenter = new Bloodoxygen_Chaosi_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "iChoice"));
                    break;
                case "SpO2080971":
                    bluetoothPresenter = new Bloodoxygen_Kangtai_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "SpO2080971"));
                    break;
            }
        }
    }

    @Override
    public void updateData(String... datas) {
        if (datas.length == 2) {
            mTvResult.setText(datas[0]);
        }
    }

    @Override
    public void updateState(String state) {
        ToastUtils.showShort(state);
        if (dealVoiceAndJump != null) {
            dealVoiceAndJump.updateVoice(state);
        }
    }

    @Override
    public Context getThisContext() {
        return this.getContext();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (bluetoothPresenter != null) {
            bluetoothPresenter.onDestroy();
        }
        if (helper != null) {
            helper.destroy();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_health_history) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2HealthHistory(IPresenter.MEASURE_BLOOD_OXYGEN);
            }
        } else if (i == R.id.btn_video_demo) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2DemoVideo(IPresenter.MEASURE_BLOOD_OXYGEN);
            }
        }
    }
}