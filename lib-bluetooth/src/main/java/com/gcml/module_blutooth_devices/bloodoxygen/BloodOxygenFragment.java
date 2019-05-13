package com.gcml.module_blutooth_devices.bloodoxygen;

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

import java.util.ArrayList;
import java.util.Locale;

public class BloodOxygenFragment extends BluetoothBaseFragment implements View.OnClickListener {
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    private TextView mTvResult;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_bloodoxygen;
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
        return new BloodOxygenPresenter(this);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_health_history) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2HealthHistory(IPresenter.MEASURE_BLOOD_OXYGEN);
            }
            clickHealthHistory(v);
        } else if (i == R.id.btn_video_demo) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2DemoVideo(IPresenter.MEASURE_BLOOD_OXYGEN);
            }
            clickHealthHistory(v);
        }
    }

    @Override
    public void updateData(DetectionData detectionData) {
        if (detectionData == null) return;
        if (detectionData.isInit()) {
            mTvResult.setText("0");
            isMeasureFinishedOfThisTime = false;
        } else {
            mTvResult.setText(String.format(Locale.getDefault(), "%.2f", detectionData.getBloodOxygen()));
            if (!isMeasureFinishedOfThisTime && detectionData.getBloodOxygen() != 0) {
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
}
