package com.gcml.module_factory_test.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.module_factory_test.R;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

@Route(path = "/factory/test/activity")
public class FactoryTestActivity extends ToolbarBaseActivity implements View.OnClickListener {


    /**
     * WIFI检测
     */
    private TextView mTvWifi;
    /**
     * 蓝牙检测
     */
    private TextView mTvBluetooth;
    /**
     * 相机检测
     */
    private TextView mTvCamera;
    /**
     * 麦克风检测
     */
    private TextView mTvMicro;
    /**
     * 屏幕坏点检测
     */
    private TextView mTvScreenPoint;
    /**
     * 屏幕触摸检测
     */
    private TextView mTvScreenTouch;
    /**
     * 视频播放检测
     */
    private TextView mTvScreenVideo;
    /**
     * 恢复出厂设置
     */
    private TextView mTvSystemReset;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.factory_activity_factory_test);
        initView();
        initTitle();
    }

    @Override
    protected void backLastActivity() {
        finish();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("工厂检测");
    }

    private void initView() {
        mTvWifi = (TextView) findViewById(R.id.tv_wifi);
        mTvWifi.setOnClickListener(this);
        mTvBluetooth = (TextView) findViewById(R.id.tv_bluetooth);
        mTvBluetooth.setOnClickListener(this);
        mTvCamera = (TextView) findViewById(R.id.tv_camera);
        mTvCamera.setOnClickListener(this);
        mTvMicro = (TextView) findViewById(R.id.tv_micro);
        mTvMicro.setOnClickListener(this);
        mTvScreenPoint = (TextView) findViewById(R.id.tv_screen_point);
        mTvScreenPoint.setOnClickListener(this);
        mTvScreenTouch = (TextView) findViewById(R.id.tv_screen_touch);
        mTvScreenTouch.setOnClickListener(this);
        mTvScreenVideo = (TextView) findViewById(R.id.tv_screen_video);
        mTvScreenVideo.setOnClickListener(this);
        mTvSystemReset = (TextView) findViewById(R.id.tv_system_reset);
        mTvSystemReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.tv_wifi) {
            Routerfit.register(AppRouter.class).skipWifiConnectActivity(false);
        } else if (i == R.id.tv_bluetooth) {
            Routerfit.register(AppRouter.class).skipAllMeasureActivity(24);
        } else if (i == R.id.tv_camera) {
            startActivity(new Intent(this, CameraActivity.class));

        } else if (i == R.id.tv_micro) {
            Routerfit.register(AppRouter.class).skipSpeechSynthesisActivity();
        } else if (i == R.id.tv_screen_point) {
            startActivity(new Intent(this, ScreenPointActivity.class));

        } else if (i == R.id.tv_screen_touch) {
            startActivity(new Intent(this, ScreenTouchActivity.class));

        } else if (i == R.id.tv_screen_video) {
            //Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_test
            NormalVideoPlayActivity.startActivity(this, null,
                    "http://oyptcv2pb.bkt.clouddn.com/tips_test.mp4", "测试");
        } else if (i == R.id.tv_system_reset) {
            showResetDialog();
        } else {
        }
    }

    private void showResetDialog() {
        new AlertDialog(this).builder()
                .setMsg("确认恢复出厂设置吗？")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //恢复出厂设置
                        UserSpHelper.clear(getApplicationContext());
                        Routerfit.register(AppRouter.class).skipWelcomeActivity();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }
}
