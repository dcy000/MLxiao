package com.gcml.module_blutooth_devices.bloodpressure_devices;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.lib_utils.data.SPUtil;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.ui.UiUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.utils.BloodPressureSurfaceView;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.utils.SearchWithDeviceGroupHelper;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/10/22 15:32
 * created by: gzq
 * description: TODO
 */
public class Bloodpressure_Xien_Fragment extends BluetoothBaseFragment implements IView, View.OnClickListener {
    private BloodPressureSurfaceView mPressureSvValue;
    /**
     * 0
     */
    private TextView mHighPressure;
    /**
     * 0
     */
    private TextView mLowPressure;
    /**
     * 0
     */
    private TextView mPulse;
    /**
     * 健康数据
     */
    protected TextView mBtnHealthHistory;
    /**
     * 使用演示
     */
    protected TextView mBtnVideoDemo;
    private Bundle bundle;
    private BaseBluetoothPresenter baseBluetoothPresenter;
    private SearchWithDeviceGroupHelper helper;
    private int highId;
    private int lowId;
    private ConstraintSet highSet;
    private ConstraintSet lowSet;
    private ConstraintLayout clPressure;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_bloodpressure_lude;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mPressureSvValue = (BloodPressureSurfaceView) view.findViewById(R.id.pressure_sv_value);
        mHighPressure = (TextView) view.findViewById(R.id.high_pressure);
        mLowPressure = (TextView) view.findViewById(R.id.low_pressure);
        mPulse = (TextView) view.findViewById(R.id.pulse);
        mBtnHealthHistory = (TextView) view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = (TextView) view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        clPressure = (ConstraintLayout) view.findViewById(R.id.device_cl_pressure);
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
            if (helper == null) {
                Logg.e(Bloodpressure_Fragment.class, "helper==null");
                helper = new SearchWithDeviceGroupHelper(this, IPresenter.MEASURE_BLOOD_PRESSURE);
            }
            helper.start();
        } else {
            if (baseBluetoothPresenter != null) {
                Logg.e(Bloodpressure_Fragment.class, "baseBluetoothPresenter!=null");
                baseBluetoothPresenter.checkBlueboothOpened();
                return;
            }
            switch (brand) {
                case "Dual-SPP":
                    baseBluetoothPresenter = new Bloodpressure_Xien_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "Dual-SPP"));
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void updateData(String... datas) {
        if (datas.length == 1) {
            float value = Float.parseFloat(datas[0]);
            applyIndicatorAnimation(true, (int) value);
            mPressureSvValue.setTargetValue(value);
            mHighPressure.setText(datas[0]);
            mLowPressure.setText("0");
            mPulse.setText("0");
            isMeasureFinishedOfThisTime = false;
        } else if (datas.length == 3) {
            float h = Float.parseFloat(datas[0]);
            float l = Float.parseFloat(datas[1]);
            applyIndicatorAnimation(true, (int) h);
            applyIndicatorAnimation(false, (int) l);
            mPressureSvValue.setTargetValue(h);
            mHighPressure.setText(datas[0]);
            mLowPressure.setText(datas[1]);
            mPulse.setText(datas[2]);
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
        Logg.e(Bloodpressure_Fragment.class, "onStop()");
        if (baseBluetoothPresenter != null) {
            baseBluetoothPresenter.onDestroy();
            baseBluetoothPresenter = null;
        }
        if (helper != null) {
            helper.destroy();
            helper = null;
        }
    }

    private void applyIndicatorAnimation(boolean high, int value) {
        int targetId;
        int descId;
        ConstraintSet constraintSet;
        if (high) {
            targetId = R.id.pressure_iv_high_indicator;
            if (value < 90) {
                descId = R.id.pressure_iv_high_low;
            } else if (value < 95) {
                descId = R.id.pressure_iv_high_to_low;
            } else if (value < 135) {
                descId = R.id.pressure_iv_high_normal;
            } else if (value < 140) {
                descId = R.id.pressure_iv_high_to_high;
            } else {
                descId = R.id.pressure_iv_high_high;
            }
            if (descId == highId) {
                return;
            }
            highId = descId;
            if (highSet == null) {
                highSet = new ConstraintSet();
            }
            constraintSet = highSet;
        } else {
            targetId = R.id.pressure_iv_low_indicator;
            if (value < 60) {
                descId = R.id.pressure_iv_low_low;
            } else if (value < 65) {
                descId = R.id.pressure_iv_low_to_low;
            } else if (value < 85) {
                descId = R.id.pressure_iv_low_normal;
            } else if (value < 90) {
                descId = R.id.pressure_iv_low_to_high;
            } else {
                descId = R.id.pressure_iv_low_high;
            }
            if (descId == lowId) {
                return;
            }
            lowId = descId;
            if (lowSet == null) {
                lowSet = new ConstraintSet();
            }
            constraintSet = lowSet;
        }
        if (clPressure == null) {
            return;
        }
        constraintSet.clone(clPressure);
        constraintSet.connect(targetId, ConstraintSet.START, descId, ConstraintSet.START);
        constraintSet.connect(targetId, ConstraintSet.END, descId, ConstraintSet.END);
        constraintSet.connect(targetId, ConstraintSet.BOTTOM, descId, ConstraintSet.TOP, UiUtils.pt(16));
        constraintSet.applyTo(clPressure);
    }
}
