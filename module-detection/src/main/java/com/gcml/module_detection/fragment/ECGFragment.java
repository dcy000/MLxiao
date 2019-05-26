package com.gcml.module_detection.fragment;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.data.DataUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_blutooth_devices.base.IBleConstants;
import com.gcml.module_blutooth_devices.ecg.ECGPresenter;
import com.gcml.module_blutooth_devices.ecg.ECGSingleGuideView;
import com.gcml.module_detection.ConnectActivity;
import com.gcml.module_detection.net.DetectionRepository;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

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
                            postData(detectionData);
                            //pdf地址，异常标识，结论,心率
                            analysisData.onSuccess(detectionData.getResultUrl(), detectionData.getEcgFlag(),
                                    detectionData.getResult(), detectionData.getHeartRate());
                        }
                    }
                }
            }
        });
        BaseBluetooth presenter = ((ConnectActivity) getActivity()).getPresenter();
        if (presenter instanceof ECGPresenter) {
            ((ECGPresenter) presenter).ecgBrand.observe(this, new Observer<String>() {
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
            });
        }
    }

    private void postData(DetectionData detectionData) {
        ArrayList<DetectionData> datas = new ArrayList<>();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        DetectionData ecgData = new DetectionData();
        ecgData.setDetectionType("2");
        ecgData.setEcg(detectionData.getEcgFlag() == 2 ? "1" : String.valueOf(detectionData.getEcgFlag()));
        ecgData.setResult(detectionData.getResult());
        ecgData.setHeartRate(detectionData.getHeartRate());
        ecgData.setResultUrl(detectionData.getResultUrl());
        datas.add(ecgData);
        DetectionRepository.postMeasureData(datas)
                .compose(RxUtils.io2Main())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        Timber.i(">>>>" + o.toString());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onClick(View v) {

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
