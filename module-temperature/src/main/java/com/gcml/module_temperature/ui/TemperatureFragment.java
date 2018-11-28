package com.gcml.module_temperature.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gcml.module_temperature.R;
import com.gcml.module_temperature.presenter.TemperaturePresenter;
import com.gzq.lib_bluetooth.common.BaseBluetoothFragment;
import com.gzq.lib_bluetooth.IBluetoothView;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;


public class TemperatureFragment extends BaseBluetoothFragment implements IBluetoothView, View.OnClickListener {
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    private TextView mTvResult;


    @Override
    public void updateData(String... datas) {
        if (datas.length == 2) {
            mTvResult.setText("0.00");
        } else if (datas.length == 1) {
            mTvResult.setText(datas[0]);
            MLVoiceSynthetize.startSynthesize("主人，您本次测量体温" + datas[0]);
        }
    }


    @Override
    public void updateState(String state) {
        if (stateUpdate2Activity != null) {
            stateUpdate2Activity.onStateChanged(state);
        }
        ToastUtils.showShort(state);
        MLVoiceSynthetize.startSynthesize(state);
    }


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.bluetooth_fragment_temperature;
    }

    @Override
    public void initParams(Bundle bundle) {

    }

    @Override
    public void initView(View view) {
        mBtnHealthHistory = view.findViewById(R.id.btn_health_history);
        mBtnHealthHistory.setOnClickListener(this);
        mBtnVideoDemo = view.findViewById(R.id.btn_video_demo);
        mBtnVideoDemo.setOnClickListener(this);
        mTvResult = view.findViewById(R.id.tv_result);
        mTvResult.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
    }

    @Override
    public IPresenter obtainPresenter() {
        return new TemperaturePresenter(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_health_history) {
            createEvent("HealthRecord>Temperature");
        } else if (i == R.id.btn_video_demo) {
            createEvent("Video>Temperature");
        }
    }

    @Override
    public boolean isUploadData() {
        return true;
    }
}
