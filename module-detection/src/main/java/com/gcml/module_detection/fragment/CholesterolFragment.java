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

public class CholesterolFragment extends BluetoothBaseFragment implements View.OnClickListener {
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    private TextView mTvResult;
    private boolean isMeasureCholesterolFinished;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_cholesterol;
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
                    isMeasureCholesterolFinished = false;
                } else {
                    Float cholesterol = detectionData.getCholesterol();
                    if (cholesterol != null && cholesterol != 0 && !isMeasureCholesterolFinished) {
                        isMeasureCholesterolFinished = true;
                        mTvResult.setText(String.format(Locale.getDefault(), "%.2f", cholesterol));
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
