package com.gcml.module_detection.fragment;

import android.arch.lifecycle.Observer;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.BluetoothStore;
import com.gcml.module_detection.R;
import com.gcml.module_detection.net.DetectionRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;

import io.reactivex.observers.DefaultObserver;
import timber.log.Timber;

public class BloodpressureFragment extends BluetoothBaseFragment implements View.OnClickListener {
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    private TextView mTvGaoya;
    private TextView mTvDiya;
    private TextView mTvMaibo;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_bloodpressure;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mBtnHealthHistory = view.findViewById(com.gcml.module_blutooth_devices.R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = view.findViewById(com.gcml.module_blutooth_devices.R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvGaoya = view.findViewById(com.gcml.module_blutooth_devices.R.id.tv_gaoya);
        mTvDiya = view.findViewById(com.gcml.module_blutooth_devices.R.id.tv_diya);
        mTvMaibo = view.findViewById(com.gcml.module_blutooth_devices.R.id.tv_maibo);
        mTvGaoya.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvDiya.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvMaibo.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        obserData();
    }

    private void obserData() {
        BluetoothStore.instance.detection.observe(this, new Observer<DetectionData>() {
            @Override
            public void onChanged(@Nullable DetectionData detectionData) {
                if (detectionData == null) return;
                Integer highPressure = detectionData.getHighPressure();
                if (detectionData.isInit()) {
                    mTvGaoya.setText(String.valueOf(highPressure));
                    mTvDiya.setText("0");
                    mTvMaibo.setText("0");
                    isMeasureFinishedOfThisTime = false;
                } else {
                    mTvGaoya.setText(String.valueOf(highPressure));
                    mTvDiya.setText(String.valueOf(detectionData.getLowPressure()));
                    mTvMaibo.setText(String.valueOf(detectionData.getPulse()));
                    if (!isMeasureFinishedOfThisTime && highPressure != null && highPressure != 0) {
                        isMeasureFinishedOfThisTime = true;
                        onMeasureFinished(detectionData);
                        robotSpeak(detectionData);
                        postData(detectionData);
                    }
                }
            }
        });
    }

    private void robotSpeak(DetectionData detectionData) {
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "您本次测量高压" + detectionData.getHighPressure() + ",低压" + detectionData.getLowPressure() + ",脉搏" + detectionData.getPulse(), false);
    }

    private void postData(DetectionData detectionData) {
        ArrayList<DetectionData> datas = new ArrayList<>();
        DetectionData pressureData = new DetectionData();
        DetectionData pulseData = new DetectionData();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        pressureData.setDetectionType("0");
        pressureData.setHighPressure(detectionData.getHighPressure());
        pressureData.setLowPressure(detectionData.getLowPressure());
        pulseData.setDetectionType("9");
        pulseData.setPulse(detectionData.getPulse());
        datas.add(pressureData);
        datas.add(pulseData);

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
}
