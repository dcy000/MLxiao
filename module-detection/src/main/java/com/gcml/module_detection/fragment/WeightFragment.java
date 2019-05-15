package com.gcml.module_detection.fragment;

import android.arch.lifecycle.Observer;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.BluetoothStore;

import java.util.Locale;

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
                    if (detectionData.isWeightOver()) {
                        if (!isMeasureFinishedOfThisTime && detectionData.getWeight() != 0) {
                            isMeasureFinishedOfThisTime = true;
                            onMeasureFinished(detectionData);
                        }
                        if (mTvTizhong != null) {
                            mTvTizhong.setText(String.format(Locale.getDefault(), "%.2f", detectionData.getWeight()));
                        }
                    } else {
                        if (mTvTizhong != null) {
                            mTvTizhong.setText(String.format(Locale.getDefault(), "%.2f", detectionData.getWeight()));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
