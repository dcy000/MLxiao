package com.gcml.module_blutooth_devices.ecg;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.data.DataUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.IBleConstants;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;
import com.inuker.bluetooth.library.utils.ByteUtils;

import timber.log.Timber;

public class ECGFragment extends BluetoothBaseFragment implements View.OnClickListener {
    private ECGSingleGuideView mEcgView;
    private TextView mMeasureTip;
    protected TextView mBtnChangeDevice;
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    protected TextView mTvNext;
    private ECGPresenter ecgPresenter;

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

    }

    @Override
    protected BaseBluetooth obtainPresenter() {
        ecgPresenter = new ECGPresenter(this);
        ecgPresenter.ecgBrand.observe(this, observer);
        return ecgPresenter;
    }

    private Observer<String> observer = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String s) {
            if (s.startsWith("WeCardio")) {
                mEcgView.setBaseLineValue(3600f);
                mEcgView.setBrand("BoSheng");
                return;
            }
            if (s.startsWith("A12-B")) {
                mEcgView.setBrand("ChaoSi");
                mEcgView.setReverse(true);
                mEcgView.setSampling(250);
                mEcgView.setDefaultBaseLinewValue(600);
                mEcgView.setGain(70);
                return;
            }
        }
    };

    @Override
    public void updateData(DetectionData detectionData) {
        Timber.i(">>>>String:%s,byte[]:%s", detectionData.getEcgDataString(), detectionData.getEcgData());
        if (detectionData.getEcgDataString() != null || detectionData.getEcgData() != null) {
            //其中超思有的数据获取实在子线程 ，此处展示应在UI线程
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mEcgView.addData(detectionData.getEcgData() == null ?
                            ByteUtils.stringToBytes(detectionData.getEcgDataString())
                            : detectionData.getEcgData());
                }
            });
        }
        if (!TextUtils.isEmpty(detectionData.getEcgTips())) {
            mMeasureTip.setText(detectionData.getEcgTips());
        }

        if (!TextUtils.isEmpty(detectionData.getResultUrl())) {
            onMeasureFinished(detectionData);
            if (analysisData != null) {
                if (DataUtils.isNullString(detectionData.getResult()) || DataUtils.isNullString(detectionData.getResultUrl())) {
                    analysisData.onError();
                } else {
                    //pdf地址，异常标识，结论,心率
                    analysisData.onSuccess(detectionData.getResultUrl(), detectionData.getEcgFlag(),
                            detectionData.getResult(), detectionData.getHeartRate());
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
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_change_device) {
            if (fragmentChanged != null) {
                fragmentChanged.onFragmentChanged(this, null);
            }
        } else if (i == R.id.btn_health_history) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2HealthHistory(IBleConstants.MEASURE_ECG);
            }
            clickHealthHistory(v);
        } else if (i == R.id.btn_video_demo) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2DemoVideo(IBleConstants.MEASURE_ECG);
            }
            clickVideoDemo(v);
        } else if (i == R.id.tv_next) {
            clickBtnNext();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (ecgPresenter != null) {
            ecgPresenter.ecgBrand.removeObservers(this);
        }
        ecgPresenter = null;
    }

    protected void clickBtnNext() {
    }

    private AnalysisData analysisData;

    public void setOnAnalysisDataListener(AnalysisData analysisData) {
        this.analysisData = analysisData;
    }

    public interface AnalysisData {
        void onSuccess(String fileAddress, int flag, String result, int heartRate);

        void onError();
    }
}
