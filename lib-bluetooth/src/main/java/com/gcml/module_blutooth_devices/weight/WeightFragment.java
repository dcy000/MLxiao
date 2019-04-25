package com.gcml.module_blutooth_devices.weight;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.BaseBluetooth;

public class WeightFragment extends BluetoothBaseFragment implements View.OnClickListener {
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    private TextView mTvTizhong;
    protected TextView mTvTizhi;

    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_weight;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mBtnHealthHistory = view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvTizhong = view.findViewById(R.id.tv_tizhong);
        mTvTizhi = view.findViewById(R.id.tv_tizhi);
        mTvTizhong.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mTvTizhi.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
    }

    @Override
    protected BaseBluetooth obtainPresenter() {
        return new WeightPresenter(this);
    }

    @Override
    public void updateData(String... datas) {
        if (datas.length == 2) {
            if (mTvTizhong != null) {
                mTvTizhong.setText("0.00");
            }
            isMeasureFinishedOfThisTime = false;
        } else if (datas.length == 1) {

            if (mTvTizhong != null) {
                mTvTizhong.setText(datas[0]);
            }
        } else if (datas.length == 3) {
            if (!isMeasureFinishedOfThisTime && Float.parseFloat(datas[2]) != 0) {
                isMeasureFinishedOfThisTime = true;
                onMeasureFinished(datas[2]);
            }
            if (mTvTizhong != null) {
                mTvTizhong.setText(datas[2]);
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
                dealVoiceAndJump.jump2HealthHistory(IPresenter.MEASURE_WEIGHT);
            }
            clickHealthHistory(v);
        } else if (i == R.id.btn_video_demo) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2DemoVideo(IPresenter.MEASURE_WEIGHT);
            }
            clickVideoDemo(v);
        }
    }
}
