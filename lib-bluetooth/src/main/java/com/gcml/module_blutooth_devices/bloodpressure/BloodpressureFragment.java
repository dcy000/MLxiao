package com.gcml.module_blutooth_devices.bloodpressure;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;

public class BloodpressureFragment extends BluetoothBaseFragment implements View.OnClickListener {
    private TextView mTitle3;
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
        mTitle3 = view.findViewById(R.id.title3);
        mBtnHealthHistory = view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvGaoya = view.findViewById(R.id.tv_gaoya);
        mTvDiya = view.findViewById(R.id.tv_diya);
        mTvMaibo = view.findViewById(R.id.tv_maibo);
        mTvGaoya.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvDiya.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvMaibo.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
    }

    @Override
    protected BaseBluetooth obtainPresenter() {
        return new BloodPressurePresenter(this);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void updateData(DetectionData detectionData) {
        if (detectionData.isInit()) {
            mTvGaoya.setText(String.valueOf(detectionData.getHighPressure()));
            mTvDiya.setText("0");
            mTvMaibo.setText("0");
            isMeasureFinishedOfThisTime = false;
        } else {
            mTvGaoya.setText(String.format("%d", detectionData.getHighPressure()));
            mTvDiya.setText(String.format("%d", detectionData.getLowPressure()));
            mTvMaibo.setText(String.format("%d", detectionData.getPulse()));
            if (!isMeasureFinishedOfThisTime && detectionData.getHighPressure() != 0) {
                isMeasureFinishedOfThisTime = true;
                onMeasureFinished(detectionData);
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
        if (i == R.id.btn_health_history) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2HealthHistory(IPresenter.MEASURE_BLOOD_PRESSURE);
            }
            clickHealthHistory(v);
        } else if (i == R.id.btn_video_demo) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2DemoVideo(IPresenter.MEASURE_BLOOD_PRESSURE);
            }
            clickVideoDemo(v);
        }
    }
}
