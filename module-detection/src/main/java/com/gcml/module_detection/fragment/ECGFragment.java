package com.gcml.module_detection.fragment;

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
import com.gcml.module_blutooth_devices.base.BaseBluetooth;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_blutooth_devices.base.IBleConstants;
import com.gcml.module_blutooth_devices.ecg.ECGPresenter;
import com.gcml.module_blutooth_devices.ecg.ECGSingleGuideView;

public class ECGFragment extends BluetoothBaseFragment implements View.OnClickListener {
    private ECGSingleGuideView mEcgView;
    private TextView mMeasureTip;
    protected TextView mBtnChangeDevice;
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
        obserData();
    }

    private void obserData() {
        BluetoothStore.instance.detection.observe(this, new Observer<DetectionData>() {
            @Override
            public void onChanged(@Nullable DetectionData detectionData) {
                if (detectionData == null) return;
                if (detectionData.getEcgData() != null) {
                    //其中超思有的数据获取实在子线程 ，此处展示应在UI线程
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mEcgView.addData(detectionData.getEcgData());
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
        });
    }

    @Override
    public void onClick(View v) {

    }

    private com.gcml.module_blutooth_devices.ecg.ECGFragment.AnalysisData analysisData;

    public void setOnAnalysisDataListener(com.gcml.module_blutooth_devices.ecg.ECGFragment.AnalysisData analysisData) {
        this.analysisData = analysisData;
    }

    public interface AnalysisData {
        void onSuccess(String fileAddress, int flag, String result, int heartRate);

        void onError();
    }
}
