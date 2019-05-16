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

import java.util.Locale;

public class UricAcidFragment extends BluetoothBaseFragment implements View.OnClickListener {
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    private TextView mTvResult;
    private boolean isMeasureBUAFinished;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_uric_acid;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mBtnHealthHistory = view.findViewById(com.gcml.module_blutooth_devices.R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = view.findViewById(com.gcml.module_blutooth_devices.R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvResult = view.findViewById(com.gcml.module_blutooth_devices.R.id.tv_result);
        mTvResult.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        obserData();
    }

    private void obserData() {
        BluetoothStore.instance.detection.observe(this, new Observer<DetectionData>() {
            @Override
            public void onChanged(@Nullable DetectionData detectionData) {
                if (detectionData == null) return;
                if (detectionData.isInit()) {
                    isMeasureBUAFinished = false;
                } else {
                    if (detectionData.getUricAcid() != 0 && !isMeasureBUAFinished) {
                        isMeasureBUAFinished = true;
                        mTvResult.setText(String.format(Locale.getDefault(), "%.2f", detectionData.getUricAcid()));
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
