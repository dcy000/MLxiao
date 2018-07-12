package com.gcml.module_blutooth_devices.temperature_devices;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BaseFragment;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.utils.SearchWithDeviceGroupHelper;
import com.gcml.module_blutooth_devices.utils.ToastTool;
import com.gcml.module_blutooth_devices.utils.SPUtil;


public class Temperature_Fragment extends BaseFragment implements IView, View.OnClickListener {
    private TextView mBtnHealthHistory;
    private TextView mBtnVideoDemo;
    private TextView mTvResult;
    private BaseBluetoothPresenter bluetoothPresenter;
    private SearchWithDeviceGroupHelper helper;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_temperature;
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
            String sp_bloodoxygen = (String) SPUtil.get(getContext(), SPUtil.SP_SAVE_TEMPERATURE, "");
            if (TextUtils.isEmpty(sp_bloodoxygen)) {
                helper = new SearchWithDeviceGroupHelper(this, IPresenter.MEASURE_TEMPERATURE);
                helper.start();
            } else {
                String[] split = sp_bloodoxygen.split(",");
                if (split.length == 2) {
                    brand = split[0];
                    address = split[1];
                    chooseConnectType(address, brand);
                }

            }
        }
    }

    private void chooseConnectType(String address, String brand) {
        if (TextUtils.isEmpty(address)) {
            helper = new SearchWithDeviceGroupHelper(this, IPresenter.MEASURE_TEMPERATURE);
            helper.start();
        } else {
            switch (brand) {
                case "AET-WD":
                    bluetoothPresenter = new Temperature_Ailikang_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "AET-WD"));
                    break;
                case "ClinkBlood":
                    Log.e("初始化福达康", "dealLogic: ");
                    bluetoothPresenter = new Temperature_Fudakang_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "ClinkBlood"));
                    break;
                case "MEDXING-IRT":
                    bluetoothPresenter = new Temperature_Meidilian_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "MEDXING-IRT"));
                    break;
                case "FSRKB-EWQ01":
                    bluetoothPresenter = new Temperature_Zhiziyun_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "FSRKB-EWQ01"));
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_health_history) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2HealthHistory(IPresenter.MEASURE_TEMPERATURE);
            }
        } else if (i == R.id.btn_video_demo) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2DemoVideo(IPresenter.MEASURE_TEMPERATURE);
            }
        }
    }

    @Override
    public void updateData(String... datas) {
        if (datas.length == 1) {
            mTvResult.setText(datas[0]);
        }
    }

    @Override
    public void updateState(String state) {
        ToastTool.showShort(state);
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
    public void onStop() {
        super.onStop();
        if (bluetoothPresenter != null) {
            Logg.e(Temperature_Fragment.class, "presenter有没有走");
            bluetoothPresenter.onDestroy();
        }
        if (helper != null) {
            helper.destroy();
        }
    }
}
