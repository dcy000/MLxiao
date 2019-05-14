package com.gcml.module_blutooth_devices.temperature;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.recommend.bean.post.DetectionData;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.IBleConstants;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;

import java.util.Locale;

public class TemperatureFragment extends BluetoothBaseFragment implements View.OnClickListener {
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    private TextView mTvResult;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_temperature;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mBtnHealthHistory = view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvResult = view.findViewById(R.id.tv_result);
        mTvResult.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
    }

    @Override
    protected BaseBluetooth obtainPresenter() {
        return new TemperaturePresenter(this);
    }

    @Override
    public void updateData(DetectionData detectionData) {
        if (detectionData.isInit()) {
            mTvResult.setText("0.00");
            isMeasureFinishedOfThisTime = false;
        } else {
            mTvResult.setText(String.format(Locale.getDefault(), "%.2f", detectionData.getTemperAture()));
            if (!isMeasureFinishedOfThisTime && detectionData.getTemperAture() > 30) {
                isMeasureFinishedOfThisTime = true;
                onMeasureFinished(detectionData);
            }
        }
    }

    @Override
    public void updateState(String state) {
        ToastUtils.showShort(state);
//        ((AllMeasureActivity) getActivity()).speak(state);
        if (dealVoiceAndJump != null) {
            dealVoiceAndJump.updateVoice(state);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_health_history) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2HealthHistory(IBleConstants.MEASURE_TEMPERATURE);
            }
            clickHealthHistory(v);
        } else if (i == R.id.btn_video_demo) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2DemoVideo(IBleConstants.MEASURE_TEMPERATURE);
            }
            clickVideoDemo(v);
        }
    }
}
