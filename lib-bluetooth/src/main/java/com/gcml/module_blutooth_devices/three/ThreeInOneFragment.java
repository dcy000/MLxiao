package com.gcml.module_blutooth_devices.three;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.bluetooth.BaseBluetooth;
import com.gcml.module_blutooth_devices.others.ThreeInOne_Fragment;

public class ThreeInOneFragment extends BluetoothBaseFragment implements View.OnClickListener {
    /**
     * 历史记录
     */
    protected TextView mBtnHealthHistory;
    /**
     * 使用演示
     */
    protected TextView mBtnVideoDemo;
    /**
     * 0
     */
    private TextView mTvGaoya;
    /**
     * 0
     */
    private TextView mTvDiya;
    /**
     * 0
     */
    private TextView mTvMaibo;
    private boolean isMeasureBloodsugarFinished;
    private boolean isMeasureBUAFinished;
    private boolean isMeasureCholesterolFinished;
    /**
     * &lt;3.9
     */
    protected TextView mTitle11;
    /**
     * 3.9~6.1
     */
    protected TextView mTitle12;
    /**
     * &gt;6.1
     */
    protected TextView mTitle13;
    protected TextView mTitle1;
    @Override
    protected int initLayout() {
        return R.layout.bluetooth_fragment_three_in_one;
    }

    @Override
    protected void initView(View view, Bundle bundle) {
        mBtnHealthHistory = view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvGaoya = view.findViewById(R.id.tv_gaoya);
        mTvDiya = view.findViewById(R.id.tv_diya);
        mTvMaibo = view.findViewById(R.id.tv_maibo);
        mTitle1 = (TextView) view.findViewById(R.id.title1);
        mTitle11 = (TextView) view.findViewById(R.id.title1_1);
        mTitle12 = (TextView) view.findViewById(R.id.title1_2);
        mTitle13 = (TextView) view.findViewById(R.id.title1_3);
    }

    @Override
    protected BaseBluetooth obtainPresenter() {
        return null;
    }

    @Override
    public void updateData(String... datas) {
        if (datas.length == 1) {
//            mTvGaoya.setText("0.00");
//            mTvDiya.setText("0.00");
//            mTvMaibo.setText("0.00");
            isMeasureBloodsugarFinished = false;
            isMeasureBUAFinished = false;
            isMeasureCholesterolFinished = false;
            return;
        }
        if (datas.length == 2) {
            if (datas[0].equals("bloodsugar") && !isMeasureBloodsugarFinished) {
                isMeasureBloodsugarFinished = true;
                mTvGaoya.setText(datas[1]);
                onMeasureFinished(datas[0], datas[1]);
            } else if (datas[0].equals("bua") && !isMeasureBUAFinished) {
                isMeasureBUAFinished = true;
                mTvDiya.setText(datas[1]);
                onMeasureFinished(datas[0], datas[1]);
            } else if (datas[0].equals("cholesterol") && !isMeasureCholesterolFinished) {
                isMeasureCholesterolFinished = true;
                mTvMaibo.setText(datas[1]);
                onMeasureFinished(datas[0], datas[1]);
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
                dealVoiceAndJump.jump2HealthHistory(IPresenter.MEASURE_OTHERS);
            }
            clickHealthHistory(v);
        } else if (i == R.id.btn_video_demo) {
            if (dealVoiceAndJump != null) {
                dealVoiceAndJump.jump2DemoVideo(IPresenter.MEASURE_OTHERS);
            }
            clickVideoDemo(v);
        }
    }

    public interface MeasureItemChanged {
        void onChanged(int position);
    }

    protected ThreeInOne_Fragment.MeasureItemChanged measureItemChanged;

    public void setOnMeasureItemChanged(ThreeInOne_Fragment.MeasureItemChanged measureItemChanged) {
        this.measureItemChanged = measureItemChanged;
    }
}
