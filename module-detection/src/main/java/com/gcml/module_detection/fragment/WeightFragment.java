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
import com.gcml.module_detection.net.DetectionRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.observers.DefaultObserver;
import timber.log.Timber;

public class WeightFragment extends BluetoothBaseFragment implements View.OnClickListener {
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    private TextView mTvTizhong;
    protected TextView mTvTizhi;

    @Override
    protected int initLayout() {
        return com.gcml.module_blutooth_devices.R.layout.bluetooth_fragment_weight;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mBtnHealthHistory = view.findViewById(com.gcml.module_blutooth_devices.R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = view.findViewById(com.gcml.module_blutooth_devices.R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvTizhong = view.findViewById(com.gcml.module_blutooth_devices.R.id.tv_tizhong);
        mTvTizhi = view.findViewById(com.gcml.module_blutooth_devices.R.id.tv_tizhi);
        mTvTizhong.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvTizhi.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        obserData();
    }

    private void obserData() {
        BluetoothStore.instance.detection.observe(this, new Observer<DetectionData>() {
            @Override
            public void onChanged(@Nullable DetectionData detectionData) {
                if (detectionData == null) return;
                if (detectionData.isInit()) {
                    if (mTvTizhong != null) {
                        mTvTizhong.setText("0.00");
                    }
                    isMeasureFinishedOfThisTime = false;
                } else {
                    Float weight = detectionData.getWeight();
                    if (detectionData.isWeightOver()) {
                        if (!isMeasureFinishedOfThisTime && weight != null && weight != 0) {
                            isMeasureFinishedOfThisTime = true;
                            onMeasureFinished(detectionData);
                            robotSpeak(detectionData);
                            postData(detectionData);
                        }
                        if (mTvTizhong != null) {
                            mTvTizhong.setText(String.format(Locale.getDefault(), "%.2f", weight));
                        }
                    } else {
                        if (mTvTizhong != null) {
                            mTvTizhong.setText(String.format(Locale.getDefault(), "%.2f", weight));
                        }
                    }
                }
            }
        });
    }

    private void robotSpeak(DetectionData detectionData) {
        MLVoiceSynthetize.startSynthesize(UM.getApp(), "您本次测量体重" + detectionData.getWeight() + "公斤", false);
    }

    private void postData(DetectionData detectionData) {
        ArrayList<DetectionData> datas = new ArrayList<>();
        //detectionType (string, optional): 检测数据类型 0血压 1血糖 2心电 3体重 4体温 6血氧 7胆固醇 8血尿酸 9脉搏 ,
        DetectionData weightData = new DetectionData();
        weightData.setDetectionType("3");
        weightData.setWeight(detectionData.getWeight());
        datas.add(weightData);
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
