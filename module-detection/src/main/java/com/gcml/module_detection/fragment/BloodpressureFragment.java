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
import com.gcml.module_detection.R;

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
                if (detectionData.isInit()) {
                    mTvGaoya.setText(String.valueOf(detectionData.getHighPressure()));
                    mTvDiya.setText("0");
                    mTvMaibo.setText("0");
                    isMeasureFinishedOfThisTime = false;
                } else {
                    mTvGaoya.setText(String.valueOf(detectionData.getHighPressure()));
                    mTvDiya.setText(String.valueOf(detectionData.getLowPressure()));
                    mTvMaibo.setText(String.valueOf(detectionData.getPulse()));
                    if (!isMeasureFinishedOfThisTime && detectionData.getHighPressure() != 0) {
                        isMeasureFinishedOfThisTime = true;
                        onMeasureFinished(detectionData);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
