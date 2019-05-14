package com.gcml.module_detection;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;

import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_detection.wrap.WaveView;

public class ConnectAnimFragment extends BluetoothBaseFragment {

    private WaveView mWaveView;

    @Override
    protected int initLayout() {
        return R.layout.fragment_connect_anim;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mWaveView = view.findViewById(R.id.wave_view);
        mWaveView.setDuration(5000);
        mWaveView.setSpeed(600);
        mWaveView.setStyle(Paint.Style.FILL);
        mWaveView.setColor(Color.parseColor("#CC3F86FC"));
        mWaveView.setInterpolator(new LinearOutSlowInInterpolator());
    }

    @Override
    public void onResume() {
        super.onResume();
        mWaveView.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mWaveView.stop();
    }
}
