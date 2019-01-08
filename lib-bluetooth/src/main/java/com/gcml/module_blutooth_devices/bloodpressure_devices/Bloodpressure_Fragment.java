package com.gcml.module_blutooth_devices.bloodpressure_devices;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.common.utils.data.SPUtil;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.utils.ui.UiUtils;
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


public class Bloodpressure_Fragment extends BluetoothBaseFragment implements IView, View.OnClickListener, SearchWithDeviceGroupHelper.DeviceType {
    private TextView mTitle3;
    private BaseBluetoothPresenter baseBluetoothPresenter;
    private SearchWithDeviceGroupHelper helper;
    private Bundle bundle;
    private BloodPressureSurfaceView mPressureSvValue;
    private View mPressureBgValue1;
    private TextView mHighPressure1;
    private TextView mHighPressureUnit1;
    private TextView mHighPressure;
    private TextView mLowPressure1;
    private TextView mLowPressureUnit1;
    private TextView mLowPressure;
    private TextView mPulse1;
    private TextView mPulseUnit1;
    private TextView mPulse;
    private ImageView mPressureIvHighLow;
    private ImageView mPressureIvHighToLow;
    private ImageView mPressureIvHighNormal;
    private ImageView mPressureIvHighToHigh;
    private ImageView mPressureIvHighHigh;
    private ImageView mPressureIvLowLow;
    private ImageView mPressureIvLowToLow;
    private ImageView mPressureIvLowNormal;
    private ImageView mPressureIvLowToHigh;
    private ImageView mPressureIvLowHigh;

    private TextView mPressureTvHighLow;

    private TextView mPressureTvHighToLow;

    private TextView mPressureTvHighNormal;

    private TextView mPressureTvHighToHigh;

    private TextView mPressureTvHighHigh;

    private TextView mPressureTvLowLow;

    private TextView mPressureTvLowToLow;

    private TextView mPressureTvLowNormal;

    private TextView mPressureTvLowToHigh;

    private TextView mPressureTvLowHigh;
    private ImageView mPressureIvHighIndicator;
    private ImageView mPressureIvLowIndicator;

    protected Button mHistory1;

    protected Button mXueyaVideo;

    private ConstraintLayout mDeviceClPressure;
    private int highId;
    private int lowId;
    private ConstraintSet highSet;
    private ConstraintSet lowSet;
    private ConstraintLayout clPressure;
    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_bloodpressure_xien;
    }

    @CallSuper
    @Override
    protected void initView(View view, final Bundle bundle) {
//        mTitle3 = view.findViewById(R.id.title3);
//        mBtnHealthHistory = view.findViewById(R.id.btn_health_history);
//        mBtnHealthHistory.setOnClickListener(this);
//        mBtnVideoDemo = view.findViewById(R.id.btn_video_demo);
//        mBtnVideoDemo.setOnClickListener(this);
//        mTvGaoya = view.findViewById(R.id.tv_gaoya);
//        mTvDiya = view.findViewById(R.id.tv_diya);
//        mTvMaibo = view.findViewById(R.id.tv_maibo);
//        mTvGaoya.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
//        mTvDiya.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
//        mTvMaibo.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));

        this.bundle = bundle;

        mPressureSvValue = (BloodPressureSurfaceView) view.findViewById(R.id.pressure_sv_value);
        mPressureBgValue1 = (View) view.findViewById(R.id.pressure_bg_value_1);
        mHighPressure1 = (TextView) view.findViewById(R.id.high_pressure1);
        mHighPressureUnit1 = (TextView) view.findViewById(R.id.high_pressure_unit_1);
        mHighPressure = (TextView) view.findViewById(R.id.high_pressure);
        mLowPressure1 = (TextView) view.findViewById(R.id.low_pressure1);
        mLowPressureUnit1 = (TextView) view.findViewById(R.id.low_pressure_unit_1);
        mLowPressure = (TextView) view.findViewById(R.id.low_pressure);
        mPulse1 = (TextView) view.findViewById(R.id.pulse1);
        mPulseUnit1 = (TextView) view.findViewById(R.id.pulse_unit_1);
        mPulse = (TextView) view.findViewById(R.id.pulse);
        mPressureIvHighLow = (ImageView) view.findViewById(R.id.pressure_iv_high_low);
        mPressureIvHighToLow = (ImageView) view.findViewById(R.id.pressure_iv_high_to_low);
        mPressureIvHighNormal = (ImageView) view.findViewById(R.id.pressure_iv_high_normal);
        mPressureIvHighToHigh = (ImageView) view.findViewById(R.id.pressure_iv_high_to_high);
        mPressureIvHighHigh = (ImageView) view.findViewById(R.id.pressure_iv_high_high);
        mPressureIvLowLow = (ImageView) view.findViewById(R.id.pressure_iv_low_low);
        mPressureIvLowToLow = (ImageView) view.findViewById(R.id.pressure_iv_low_to_low);
        mPressureIvLowNormal = (ImageView) view.findViewById(R.id.pressure_iv_low_normal);
        mPressureIvLowToHigh = (ImageView) view.findViewById(R.id.pressure_iv_low_to_high);
        mPressureIvLowHigh = (ImageView) view.findViewById(R.id.pressure_iv_low_high);
        mPressureTvHighLow = (TextView) view.findViewById(R.id.pressure_tv_high_low);
        mPressureTvHighToLow = (TextView) view.findViewById(R.id.pressure_tv_high_to_low);
        mPressureTvHighNormal = (TextView) view.findViewById(R.id.pressure_tv_high_normal);
        mPressureTvHighToHigh = (TextView) view.findViewById(R.id.pressure_tv_high_to_high);
        mPressureTvHighHigh = (TextView) view.findViewById(R.id.pressure_tv_high_high);
        mPressureTvLowLow = (TextView) view.findViewById(R.id.pressure_tv_low_low);
        mPressureTvLowToLow = (TextView) view.findViewById(R.id.pressure_tv_low_to_low);
        mPressureTvLowNormal = (TextView) view.findViewById(R.id.pressure_tv_low_normal);
        mPressureTvLowToHigh = (TextView) view.findViewById(R.id.pressure_tv_low_to_high);
        mPressureTvLowHigh = (TextView) view.findViewById(R.id.pressure_tv_low_high);
        mPressureIvHighIndicator = (ImageView) view.findViewById(R.id.pressure_iv_high_indicator);
        mPressureIvLowIndicator = (ImageView) view.findViewById(R.id.pressure_iv_low_indicator);
        mHistory1 = (Button) view.findViewById(R.id.history1);
        mHistory1.setOnClickListener(this);
        mXueyaVideo = (Button) view.findViewById(R.id.xueya_video);
        mXueyaVideo.setOnClickListener(this);
        mDeviceClPressure = (ConstraintLayout) view.findViewById(R.id.device_cl_pressure);
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
                helper = new SearchWithDeviceGroupHelper(this, IPresenter.MEASURE_BLOOD_PRESSURE,this);
            }
            helper.start();
        } else {
            if (baseBluetoothPresenter != null) {
                Logg.e(Bloodpressure_Fragment.class, "baseBluetoothPresenter!=null");
                baseBluetoothPresenter.checkBlueboothOpened();
                return;
            }
            switch (brand) {
                case "LD":
                    baseBluetoothPresenter = new Bloodpressure_Xien4_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "LD"));
                    break;
                case "Dual-SPP":
                    baseBluetoothPresenter = new Bloodpressure_Xien_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "Dual-SPP"));
                    break;
                case "eBlood-Pressure":
                    baseBluetoothPresenter = new Bloodpressure_Self_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "eBlood-Pressure"));
                    break;
                case "Yuwell":
                    baseBluetoothPresenter = new Bloodpressure_YuWell_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "Yuwell"));
                    break;
                case "iChoice":
                    baseBluetoothPresenter = new Bloodpressure_Chaosi_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "iChoice"));
                    break;
                case "KN-550BT 110":
                    baseBluetoothPresenter = new Bloodpressure_KN550_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "KN-550BT 110"));
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
        if (i == R.id.btn_health_history || i == R.id.history1) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2HealthHistory(IPresenter.MEASURE_BLOOD_PRESSURE);
            }
            clickHealthHistory(v);
        } else if (i == R.id.btn_video_demo) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2DemoVideo(IPresenter.MEASURE_BLOOD_PRESSURE);
            }
            clickVideoDemo(v);
        } else if (i == R.id.xueya_video) {
            startMeasure();
        }
    }

    private void startMeasure() {
        if (baseBluetoothPresenter instanceof Bloodpressure_Xien4_PresenterImp) {
            ((Bloodpressure_Xien4_PresenterImp) baseBluetoothPresenter).startMeasure();
        } else if (baseBluetoothPresenter instanceof Bloodpressure_Xien_PresenterImp) {
            ((Bloodpressure_Xien_PresenterImp) baseBluetoothPresenter).startMeasure();
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

    @Override
    public void type(BaseBluetoothPresenter baseBluetoothPresenter) {
        this.baseBluetoothPresenter=baseBluetoothPresenter;
    }
}
