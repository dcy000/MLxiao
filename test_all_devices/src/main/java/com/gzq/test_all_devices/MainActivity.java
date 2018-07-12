package com.gzq.test_all_devices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.IPresenter;

import senssun.blelib.model.Sleep;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mLlXueya;
    private LinearLayout mLlXueyang;
    private LinearLayout mLlTiwen;
    private LinearLayout mLlXuetang;
    private LinearLayout mLlXindian;
    private LinearLayout mLlTizhong;
    private LinearLayout mLlSan;
    private LinearLayout mLlMore;
    private LinearLayout mLlBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mLlXueya = (LinearLayout) findViewById(R.id.ll_xueya);
        mLlXueya.setOnClickListener(this);
        mLlXueyang = (LinearLayout) findViewById(R.id.ll_xueyang);
        mLlXueyang.setOnClickListener(this);
        mLlTiwen = (LinearLayout) findViewById(R.id.ll_tiwen);
        mLlTiwen.setOnClickListener(this);
        mLlXuetang = (LinearLayout) findViewById(R.id.ll_xuetang);
        mLlXuetang.setOnClickListener(this);
        mLlXindian = (LinearLayout) findViewById(R.id.ll_xindian);
        mLlXindian.setOnClickListener(this);
        mLlTizhong = (LinearLayout) findViewById(R.id.ll_tizhong);
        mLlTizhong.setOnClickListener(this);
        mLlSan = (LinearLayout) findViewById(R.id.ll_san);
        mLlSan.setOnClickListener(this);
        mLlMore = (LinearLayout) findViewById(R.id.ll_more);
        mLlMore.setOnClickListener(this);
        mLlBack = (LinearLayout) findViewById(R.id.ll_back);
        mLlBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        Intent intent = new Intent();
        intent.setClass(this, ChooseBluetoothActivity.class);
        switch (v.getId()) {
            case R.id.ll_xueya:
                intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_BLOOD_PRESSURE);
                startActivity(intent);
                break;
            case R.id.ll_xueyang:
                intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_BLOOD_OXYGEN);
                startActivity(intent);
                break;
            case R.id.ll_tiwen:
                intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_TEMPERATURE);
                startActivity(intent);
                break;
            case R.id.ll_xuetang:
                intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_BLOOD_SUGAR);
                startActivity(intent);
                break;
            case R.id.ll_tizhong:
                intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_WEIGHT);
                startActivity(intent);
                break;
            case R.id.ll_xindian://心电
                intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.MEASURE_ECG);
                startActivity(intent);
                break;
            case R.id.ll_more://指纹
                intent.putExtra(IPresenter.MEASURE_TYPE, IPresenter.CONTROL_FINGERPRINT);
                startActivity(intent);
                break;
        }

        if (v.getId() == R.id.ll_back) {
            finish();
        }
    }
}
