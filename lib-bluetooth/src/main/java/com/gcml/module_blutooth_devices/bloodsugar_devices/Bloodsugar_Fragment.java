package com.gcml.module_blutooth_devices.bloodsugar_devices;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.lib_utils.data.SPUtil;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.utils.SearchWithDeviceGroupHelper;


public class Bloodsugar_Fragment extends BluetoothBaseFragment implements IView, View.OnClickListener {
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    private TextView mTvResult;
    private IPresenter bluetoothPresenter;
    private SearchWithDeviceGroupHelper helper;
    private Bundle bundle;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_bloodsugar;
    }

    @Override
    protected void initView(View view, final Bundle bundle) {
        mBtnHealthHistory = view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvResult = view.findViewById(R.id.tv_result);
        mTvResult.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        this.bundle = bundle;

    }

    @Override
    public void onResume() {
        super.onResume();
        dealLogic();
    }

    public void dealLogic() {
        String address = null;
        String brand = null;
        String sp_bloodsugar = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODSUGAR, "");
        if (!TextUtils.isEmpty(sp_bloodsugar)) {
            String[] split = sp_bloodsugar.split(",");
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
            helper = new SearchWithDeviceGroupHelper(this, IPresenter.MEASURE_BLOOD_SUGAR);
            helper.start();
        } else {
            switch (brand) {
                case "BLE-Glucowell":
                    bluetoothPresenter = new Bloodsugar_GlucWell_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "BLE-Glucowell"));
                    break;
                case "BDE_WEIXIN_TTM":
                    bluetoothPresenter = new Bloodsugar_Sannuo_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "BDE_WEIXIN_TTM"));
                    break;
                case "Bioland-BGM":
                    bluetoothPresenter = new Bloodsugar_Self_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "Bioland-BGM"));
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void updateData(String... datas) {
        if (datas.length == 2) {
            mTvResult.setText("0.00");
            isMeasureFinishedOfThisTime = false;
        } else if (datas.length == 1) {
            mTvResult.setText(datas[0]);
            if (!isMeasureFinishedOfThisTime && Float.parseFloat(datas[0]) != 0) {
                isMeasureFinishedOfThisTime = true;
                onMeasureFinished(datas[0]);
            }
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
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_health_history) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2HealthHistory(IPresenter.MEASURE_BLOOD_SUGAR);
            }
            clickHealthHistory(v);
        } else if (i == R.id.btn_video_demo) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2DemoVideo(IPresenter.MEASURE_BLOOD_SUGAR);
            }
            clickVideoDemo(v);
        }
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
}
