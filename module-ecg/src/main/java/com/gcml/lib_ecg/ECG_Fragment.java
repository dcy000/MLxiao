package com.gcml.lib_ecg;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.lib_ecg.base.BluetoothBaseFragment;
import com.gcml.lib_ecg.base.ECGSingleGuideView;
import com.gcml.lib_ecg.base.IPresenter;
import com.gcml.lib_ecg.base.IView;
import com.gcml.lib_ecg.base.BoShengUserInfoBean;
import com.gcml.lib_ecg.ecg.Bluetooth_Constants;
import com.gzq.lib_core.utils.ByteUtils;
import com.gzq.lib_core.utils.DataUtils;
import com.gzq.lib_core.utils.SPUtil;
import com.gzq.lib_core.utils.ToastUtils;


public class ECG_Fragment extends BluetoothBaseFragment implements IView, View.OnClickListener {
    private ECGSingleGuideView mEcgView;
    private ECG_BoSheng_PresenterImp baseBluetoothPresenter;
    private TextView mMeasureTip;
    protected TextView mBtnChangeDevice;
    private Bundle bundle;
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    protected TextView mTvNext;
    protected BoShengUserInfoBean user;
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
        mTvNext=view.findViewById(R.id.tv_next);
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
//                brand = split[0];
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

        baseBluetoothPresenter = new ECG_BoSheng_PresenterImp(this,address,user);
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
        } else if (datas.length == 3) {
            onMeasureFinished(datas[0],datas[1],datas[2]);
            if (analysisData != null) {
                if (DataUtils.isNullString(datas[1]) || DataUtils.isNullString(datas[2])) {
                    analysisData.onError();
                } else {
                    //pdf文件编号，结论json字符串，pdf地址
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
            baseBluetoothPresenter = null;
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
        }else if (i==R.id.tv_next){
            clickBtnNext();
        }
    }
    protected void clickBtnNext(){}
    public interface AnalysisData {
        void onSuccess(String fileNum, String fileAddress, String filePDF);

        void onError();
    }
}
