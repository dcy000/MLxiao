package com.gzq.test_all_devices;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.gcml.lib_utils.display.LoadingProgressUtils;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Self_PresenterImp;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,IView {
    //增加一行注释
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
//        initView();

        Bloodoxygen_Self_PresenterImp b=new Bloodoxygen_Self_PresenterImp(this,
                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC,"50:65:83:9A:56:DF","POD"));

    }

    private void initView() {
        mLlXueya = findViewById(R.id.ll_xueya);
        mLlXueya.setOnClickListener(this);
        mLlXueyang = findViewById(R.id.ll_xueyang);
        mLlXueyang.setOnClickListener(this);
        mLlTiwen = findViewById(R.id.ll_tiwen);
        mLlTiwen.setOnClickListener(this);
        mLlXuetang = findViewById(R.id.ll_xuetang);
        mLlXuetang.setOnClickListener(this);
        mLlXindian = findViewById(R.id.ll_xindian);
        mLlXindian.setOnClickListener(this);
        mLlTizhong = findViewById(R.id.ll_tizhong);
        mLlTizhong.setOnClickListener(this);
        mLlSan = findViewById(R.id.ll_san);
        mLlSan.setOnClickListener(this);
        mLlMore = findViewById(R.id.ll_more);
        mLlMore.setOnClickListener(this);
        mLlBack = findViewById(R.id.ll_back);
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

    @Override
    public void updateData(String... datas) {

    }

    @Override
    public void updateState(String state) {

    }

    @Override
    public Context getThisContext() {
        return this;
    }
}
