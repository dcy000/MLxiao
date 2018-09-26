package com.gzq.test_all_devices;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gcml.lib_utils.base.ToolbarBaseActivity;
import com.gcml.lib_utils.device.DeviceUtils;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.others.BreathHome_Fragment;
import com.gcml.module_blutooth_devices.others.HandRing_Tongleda_PresenterImp;
import com.luliang.shapeutils.DevShapeUtils;
import com.luliang.shapeutils.shape.DevShape;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/11 17:18
 * created by:gzq
 * description:TODO
 */
public class TestBreathHomeActivity extends ToolbarBaseActivity implements IView, View.OnClickListener {
    private FrameLayout mFrame;
    private BreathHome_Fragment fragment;
    private BaseBluetoothPresenter handRingTongledaPresenterImp;
    /**
     * 同步步数
     */
    private Button mSynStep;
    /**
     * 同步心率
     */
    private Button mSynRate;
    /**  */
    private TextView mTvInfo;
    /**
     * 测试selector
     */
    private TextView mTvSel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_breath_home);
        initView();
//        fragment=new BreathHome_Fragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame,fragment).commit();
        handRingTongledaPresenterImp = new HandRing_Tongleda_PresenterImp(this,
                new DiscoverDevicesSetting(IPresenter.DISCOVER_WITH_MAC, "78:02:B7:32:D8:58", "RB09_Heart"));
    }

    private void initView() {
        mFrame = (FrameLayout) findViewById(R.id.frame);
        mSynStep = (Button) findViewById(R.id.synStep);
        mSynStep.setOnClickListener(this);
        mSynRate = (Button) findViewById(R.id.synRate);
        mSynRate.setOnClickListener(this);
        mTvInfo = (TextView) findViewById(R.id.tvInfo);
        mFrame.setOnClickListener(this);
        mTvSel = (TextView) findViewById(R.id.tv_sel);
        Drawable pressed = DevShapeUtils.shape(DevShape.RECTANGLE).radius(10).solid(R.color.content_color).build();
        Drawable normal = DevShapeUtils.shape(DevShape.RECTANGLE).radius(10).solid(R.color.config_color_alert_btn_normal).build();
        DevShapeUtils.selectorPressed(pressed,normal).into(mTvSel);
        mTvInfo.setOnClickListener(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handRingTongledaPresenterImp != null) {
            handRingTongledaPresenterImp.onDestroy();
        }
    }

    private static final String TAG = "TestBreathHomeActivity";

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            default:
                break;
            case R.id.synStep:
                OkGo.<String>get("http://47.96.98.60:8080/ZZB/api/healthMonitor/questionnaire/health/survey/")
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                Log.d(TAG, "onSuccess: " + response.body());
                            }
                        });
                break;
            case R.id.synRate:
                break;
            case R.id.frame:
                break;
            case R.id.tvInfo:
                break;
        }
    }
}
