package com.gcml.module_blutooth_devices.ecg_devices;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.lib_utils.data.DataUtils;
import com.gcml.lib_utils.data.SPUtil;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.utils.SearchWithDeviceGroupHelper;
import com.inuker.bluetooth.library.utils.ByteUtils;

public class ECG_Fragment extends BluetoothBaseFragment implements IView {
    private ECGSingleGuideView mEcgView;
    private BaseBluetoothPresenter baseBluetoothPresenter;
    private SearchWithDeviceGroupHelper helper;
    private TextView mMeasureTip;
    private Bundle bundle;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_ecg;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mEcgView = view.findViewById(R.id.ecgView);
        mMeasureTip = view.findViewById(R.id.measure_tip);
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
        if (!TextUtils.isEmpty(brand)){
            switch (brand) {
                case "WeCardio STD":
                    //3200
                    mEcgView.setBaseLineValue(3400f);
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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mEcgView.addData(ByteUtils.stringToBytes(datas[0]));
                }
            });
        } else if (datas.length == 2) {
            mMeasureTip.setText(datas[1]);
        } else if (datas.length == 3) {
            if (analysisData != null) {
                if (DataUtils.isNullString(datas[1]) || DataUtils.isNullString(datas[2])) {
                    analysisData.onError();
                } else {
                    analysisData.onSuccess(datas[0], datas[1], datas[2]);
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
        }
        if (helper != null) {
            helper.destroy();
        }
    }

    private AnalysisData analysisData;

    public void setOnAnalysisDataListener(AnalysisData analysisData) {
        this.analysisData = analysisData;
    }

    public interface AnalysisData {
        void onSuccess(String fileNum, String fileAddress, String filePDF);

        void onError();
    }
}
