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
import com.gcml.module_blutooth_devices.base.BaseFragment;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.utils.SearchWithDeviceGroupHelper;
import com.inuker.bluetooth.library.utils.ByteUtils;

public class ECG_Fragment extends BaseFragment implements IView {
    private ECGSingleGuideView mEcgView;
    private BaseBluetoothPresenter baseBluetoothPresenter;
    private SearchWithDeviceGroupHelper helper;
    private TextView mMeasureTip;
    private String brand;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_ecg;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mEcgView = view.findViewById(R.id.ecgView);
        if (bundle != null) {
            brand = bundle.getString(IPresenter.BRAND);
            if (!TextUtils.isEmpty(brand)) {
                switch (brand) {
                    case "WeCardio STD":
                        mEcgView.setBaseLineValue(3200);
                        mEcgView.setBrand("BoSheng");
                        break;
                    case "A12-B":
                        mEcgView.setBrand("ChaoSi");
                        mEcgView.setReverse(true);
                        mEcgView.setSampling(250);
                        mEcgView.setDefaultBaseLinewValue(600);
                        mEcgView.setGain(70);
                        break;
                }
            }
        }
        mMeasureTip = (TextView) view.findViewById(R.id.measure_tip);
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
            String sp_bloodoxygen = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_ECG, "");
            Logg.e(ECG_Fragment.class,"获取的SP中的数据"+sp_bloodoxygen);
            if (TextUtils.isEmpty(sp_bloodoxygen)) {
                helper = new SearchWithDeviceGroupHelper(this, IPresenter.MEASURE_ECG);
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
            }
        }
    }

    @Override
    public void updateData(final String... datas) {
        if (DataUtils.isEmpty(datas))
            return;
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
        }else if (datas.length==3){
            if (analysisData!=null){
                if (DataUtils.isNullString(datas[1])||DataUtils.isNullString(datas[2])){
                    analysisData.onError();
                }else{
                    analysisData.onSuccess(datas[0],datas[1],datas[2]);
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
    public void setOnAnalysisDataListener(AnalysisData analysisData){
        this.analysisData=analysisData;
    }
    public interface AnalysisData{
        void onSuccess(String fileNum,String fileAddress,String filePDF);
        void onError();
    }
}
