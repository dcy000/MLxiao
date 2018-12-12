package com.gcml.module_blutooth_devices.ecg_devices;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.utils.data.DataUtils;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.utils.SearchWithDeviceGroupHelper;
import com.inuker.bluetooth.library.utils.ByteUtils;

public class ECG_Fragment extends BluetoothBaseFragment implements IView, View.OnClickListener {
    private ECGSingleGuideView mEcgView;
    private BaseBluetoothPresenter baseBluetoothPresenter;
    private SearchWithDeviceGroupHelper helper;
    private TextView mMeasureTip;
    protected TextView mBtnChangeDevice;
    private Bundle bundle;
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    protected TextView mTvNext;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_ecg;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mEcgView = view.findViewById(R.id.ecgView);
        mMeasureTip = view.findViewById(R.id.measure_tip);
        mBtnChangeDevice = view.findViewById(R.id.tv_change_device);
        mBtnChangeDevice.setOnClickListener(this);
        mBtnHealthHistory = view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvNext = view.findViewById(R.id.tv_next);
        mTvNext.setOnClickListener(this);
        this.bundle = bundle;
    }

    @Override
    public void onResume() {
        super.onResume();
        dealLogic();
    }

    public void dealLogic() {
        String address = null;
        //默认心电
        String brand = "WeCardio STD";
        String sp_ecg = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_ECG, "");
        if (!TextUtils.isEmpty(sp_ecg)) {
            String[] split = sp_ecg.split(",");
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
        if (!TextUtils.isEmpty(brand)) {
            switch (brand) {
                case "WeCardio STD":
                    //3200
                    mEcgView.setBaseLineValue(3600f);
                    mEcgView.setBrand("BoSheng");
                    break;
                case "A12-B":
                    mEcgView.setBrand("ChaoSi");
                    mEcgView.setReverse(true);
                    mEcgView.setSampling(250);
                    mEcgView.setDefaultBaseLinewValue(600);
                    mEcgView.setGain(70);
                    break;
                default:
                    break;
            }
        }
        if (TextUtils.isEmpty(address)) {
            helper = new SearchWithDeviceGroupHelper(this, IPresenter.MEASURE_ECG);
            helper.start();
        } else {
            switch (brand) {
                case "WeCardio STD":
                    baseBluetoothPresenter = new ECG_BoSheng_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "WeCardio STD"));
                    break;
                case "A12-B":
                    baseBluetoothPresenter = new ECG_Chaosi_PresenterImp(this,
                            new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, address, "A12-B"));
                default:
                    break;
            }
        }
    }

    @Override
    public void updateData(final String... datas) {
        if (DataUtils.isEmpty(datas)) {
            return;
        }
        if (datas.length == 1) {
            //其中超思有的数据获取实在子线程 ，此处展示应在UI线程
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mEcgView.addData(ByteUtils.stringToBytes(datas[0]));
                }
            });
        } else if (datas.length == 2) {
            mMeasureTip.setText(datas[1]);
        } else if (datas.length == 5) {
            //pdf编号，pdf地址，异常标识，结论,心率
            onMeasureFinished(datas[0], datas[1], datas[2], datas[3],datas[4]);
            if (analysisData != null) {
                if (DataUtils.isNullString(datas[1]) || DataUtils.isNullString(datas[2])) {
                    analysisData.onError();
                } else {
                    analysisData.onSuccess(datas[0], datas[1], datas[2],datas[3],datas[4]);
                }
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
    public void onStop() {
        super.onStop();
        if (baseBluetoothPresenter != null) {
            baseBluetoothPresenter.onDestroy();
            baseBluetoothPresenter = null;
        }
        if (helper != null) {
            helper.destroy();
            helper = null;
        }
    }

    private AnalysisData analysisData;

    public void setOnAnalysisDataListener(AnalysisData analysisData) {
        this.analysisData = analysisData;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_change_device) {
            if (fragmentChanged != null) {
                fragmentChanged.onFragmentChanged(this, null);
            }
        } else if (i == R.id.btn_health_history) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2HealthHistory(IPresenter.MEASURE_ECG);
            }
            clickHealthHistory(v);
        } else if (i == R.id.btn_video_demo) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2DemoVideo(IPresenter.MEASURE_ECG);
            }
            clickVideoDemo(v);
        } else if (i == R.id.tv_next) {
            clickBtnNext();
        }
    }

    protected void clickBtnNext() {
    }

    public interface AnalysisData {
        void onSuccess(String fileNum, String fileAddress, String flag,String result,String heartRate);

        void onError();
    }
}
