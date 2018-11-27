package com.gcml.module_temperature.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gcml.module_temperature.R;
import com.gcml.module_temperature.presenter.TemperatureZhiZiYun;
import com.gzq.lib_bluetooth.IBluetoothView;
import com.gzq.lib_core.base.ui.BaseFragment;
import com.gzq.lib_core.base.ui.IPresenter;


public class TemperatureFragment extends BaseFragment implements IBluetoothView, View.OnClickListener {
    protected TextView mBtnHealthHistory;
    protected TextView mBtnVideoDemo;
    private TextView mTvResult;


    @Override
    public void updateData(String... datas) {

    }

    @Override
    public void updateState(String state) {

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
        return new TemperatureZhiZiYun(this);
    }

    @Override
    public void onClick(View v) {

    }
}
