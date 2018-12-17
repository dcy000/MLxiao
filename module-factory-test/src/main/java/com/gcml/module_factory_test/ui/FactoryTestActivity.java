package com.gcml.module_factory_test.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.gcml.module_factory_test.AA;
import com.gcml.module_factory_test.R;
import com.gcml.module_factory_test.R2;
import com.gcml.module_factory_test.video.KSYPlayer;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.config.PlayerLibrary;
import com.kk.taurus.playerbase.entity.DecoderPlan;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FactoryTestActivity extends ToolbarBaseActivity {

    @BindView(R2.id.tv_wifi)
    TextView tvWifi;
    @BindView(R2.id.tv_bluetooth)
    TextView tvBluetooth;
    @BindView(R2.id.tv_camera)
    TextView tvCamera;
    @BindView(R2.id.tv_micro)
    TextView tvMicro;
    @BindView(R2.id.tv_screen_point)
    TextView tvScreenPoint;
    @BindView(R2.id.tv_screen_touch)
    TextView tvScreenTouch;
    @BindView(R2.id.tv_screen_video)
    TextView tvScreenVideo;
    @BindView(R2.id.tv_system_reset)
    TextView tvReset;


    public static AA factoryTestListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.factory_activity_factory_test);
        ButterKnife.bind(this);
        initTitle();
        initVideo();
    }

    @Override
    protected void backLastActivity() {
        finish();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("工厂检测");
    }

    @OnClick({R2.id.tv_wifi, R2.id.tv_bluetooth, R2.id.tv_camera, R2.id.tv_micro, R2.id.tv_screen_point, R2.id.tv_screen_touch, R2.id.tv_screen_video, R2.id.tv_system_reset})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.tv_wifi) {//WIFI检测
            if (factoryTestListener != null) {
                factoryTestListener.clickConnectWifi();
            }
        } else if (i == R.id.tv_bluetooth) {//蓝牙检测
            if (factoryTestListener != null) {
                factoryTestListener.clickMeasureOxygen();
            }
        } else if (i == R.id.tv_camera) {//摄像头检测
            startActivity(new Intent(this, CameraActivity.class));

        } else if (i == R.id.tv_micro) {//麦克风检测
            if (factoryTestListener != null) {
                factoryTestListener.clickChatWithRobot();
            }

        } else if (i == R.id.tv_screen_point) {//屏幕坏点检测
            startActivity(new Intent(this, ScreenPointActivity.class));

        } else if (i == R.id.tv_screen_touch) {//屏幕触摸检测
            startActivity(new Intent(this, ScreenTouchActivity.class));

        } else if (i == R.id.tv_screen_video) {//屏幕触摸检测
            NormalVideoPlayActivity.startActivity(this, Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_test), null, "测试");
        } else if (i == R.id.tv_system_reset) {//恢复出厂设置
            reset();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        factoryTestListener = null;
    }

    /**
     * 内核金山云
     */
    public static final int PLAN_ID_KSY = 1;

    private void initVideo() {
        PlayerConfig.addDecoderPlan(new DecoderPlan(PLAN_ID_KSY, KSYPlayer.class.getName(), "Ksyplayer"));
        PlayerConfig.setDefaultPlanId(PLAN_ID_KSY);
        PlayerConfig.setUseDefaultNetworkEventProducer(true);
        PlayerLibrary.init(this.getApplication());
    }

    public void reset() {
        Intent intent = new Intent("com.gcml.hos.reset");
        sendBroadcast(intent);
    }

}
