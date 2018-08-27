package com.example.han.referralproject.settting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.cc.CCHealthMeasureActions;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.gcml.module_blutooth_devices.base.IPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FactoryTestActivity extends BaseActivity {

    @BindView(R.id.tv_wifi)
    TextView tvWifi;
    @BindView(R.id.tv_bluetooth)
    TextView tvBluetooth;
    @BindView(R.id.tv_camera)
    TextView tvCamera;
    @BindView(R.id.tv_micro)
    TextView tvMicro;
    @BindView(R.id.tv_screen_point)
    TextView tvScreenPoint;
    @BindView(R.id.tv_screen_touch)
    TextView tvScreenTouch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factory_test);
        ButterKnife.bind(this);
        speak("主人欢迎来到设置页面");
        initTitle();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("工厂检测");
    }

    @OnClick({R.id.tv_wifi, R.id.tv_bluetooth, R.id.tv_camera, R.id.tv_micro, R.id.tv_screen_point, R.id.tv_screen_touch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_wifi:
                //WIFI检测
                startActivity(new Intent(this, WifiConnectActivity.class));
                break;
            case R.id.tv_bluetooth:
                //蓝牙检测
                CCHealthMeasureActions.jump2AllMeasureActivity(IPresenter.MEASURE_BLOOD_OXYGEN);
                break;
            case R.id.tv_camera:
                //摄像头检测
                //TODO：该模块单独抽离出来了，暂时不提供此功能，如遇紧急情况请联系郭志强
//                startActivity(new Intent(this, FaceRecognitionActivity.class));
                break;
            case R.id.tv_micro:
                //麦克风检测
                startActivity(new Intent(this, SpeechSynthesisActivity.class));
                break;
            case R.id.tv_screen_point:
                //屏幕坏点检测
                startActivity(new Intent(this, ScreenPointActivity.class));
                break;
            case R.id.tv_screen_touch:
                //屏幕触摸检测
                startActivity(new Intent(this, ScreenTouchActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
