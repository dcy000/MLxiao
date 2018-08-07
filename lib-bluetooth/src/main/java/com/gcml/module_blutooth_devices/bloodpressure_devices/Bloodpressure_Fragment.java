package com.gcml.module_blutooth_devices.bloodpressure_devices;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.lib_utils.data.SPUtil;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.utils.SearchWithDeviceGroupHelper;

import timber.log.Timber;


public class Bloodpressure_Fragment extends BluetoothBaseFragment implements IView, View.OnClickListener {
    private TextView mTitle3;
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    private TextView mTvGaoya;
    private TextView mTvDiya;
    private TextView mTvMaibo;
    private BaseBluetoothPresenter baseBluetoothPresenter;
    private SearchWithDeviceGroupHelper helper;
    private Bundle bundle;
    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_bloodpressure;
    }

    @Override
    protected void initView(View view, final Bundle bundle) {
        mTitle3 = (TextView) view.findViewById(R.id.title3);
        mBtnHealthHistory = (TextView) view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = (TextView) view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvGaoya = (TextView) view.findViewById(R.id.tv_gaoya);
        mTvDiya = (TextView) view.findViewById(R.id.tv_diya);
        mTvMaibo = (TextView) view.findViewById(R.id.tv_maibo);
        this.bundle=bundle;

    }

    @Override
    public void onResume() {
        super.onResume();
        dealLogic();
    }

    public void dealLogic() {
        String address = null;
        String brand = null;
        String sp_bloodpressure = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE, "");
        if (!TextUtils.isEmpty(sp_bloodpressure)) {
            String[] split = sp_bloodpressure.split(",");
            if (split.length == 2) {
                brand = split[0];
                address = split[1];
                chooseConnectType(address, brand);
                return;
            }
        }
        if (bundle != null) {
            address = bundle.getString(IPresenter.DEVICE_BLUETOOTH_ADDRESS);
            brand = bundle.getString(IPresenter.BRAND);
            chooseConnectType(address, brand);
            return;
        }
        chooseConnectType(address, brand);

    }

    private void chooseConnectType(String address, String brand) {
        if (TextUtils.isEmpty(address)) {
            helper = new SearchWithDeviceGroupHelper(this, IPresenter.MEASURE_BLOOD_PRESSURE);
            helper.start();
        } else {
            switch (brand) {
                case "eBlood-Pressure":
                    baseBluetoothPresenter = new Bloodpressure_Self_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "eBlood-Pressure"));
                    break;
                case "iChoice":
                    baseBluetoothPresenter = new Bloodpressure_Chaosi_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "iChoice"));
                    break;
                case "KN-550BT 110":
                    baseBluetoothPresenter = new Bloodpressure_KN550_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "KN-550BT 110"));
                    break;
            }
        }
    }


    @Override
    public void updateData(String... datas) {
        if (datas.length == 1) {
            mTvGaoya.setText(datas[0]);
            isMeasureFinishedOfThisTime = false;
        } else if (datas.length == 3) {
            mTvGaoya.setText(datas[0]);
            mTvDiya.setText(datas[1]);
            mTvMaibo.setText(datas[2]);
            if (!isMeasureFinishedOfThisTime && Float.parseFloat(datas[0]) != 0) {
                isMeasureFinishedOfThisTime = true;
                onMeasureFinished(datas[0], datas[1], datas[2]);
            }
        } else {
            Logg.e(Bloodpressure_Self_PresenterImp.class, "updateData: ");
        }
    }

    @Override
    public void updateState(String state) {
        ToastUtils.showShort(state);
//        ((AllMeasureActivity) getActivity()).speak(state);
        if (dealVoiceAndJump != null) {
            dealVoiceAndJump.updateVoice(state);
        }
    }

    @Override
    public Context getThisContext() {
        return this.getContext();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_health_history) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2HealthHistory(IPresenter.MEASURE_BLOOD_PRESSURE);
            }
            clickHealthHistory(v);
        } else if (i == R.id.btn_video_demo) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2DemoVideo(IPresenter.MEASURE_BLOOD_PRESSURE);
            }
            clickVideoDemo(v);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (baseBluetoothPresenter != null) {

            baseBluetoothPresenter.onDestroy();
        }
        if (helper != null) {
            helper.destroy();
        }
    }

}
