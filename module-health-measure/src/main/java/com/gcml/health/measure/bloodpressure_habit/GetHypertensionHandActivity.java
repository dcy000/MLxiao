package com.gcml.health.measure.bloodpressure_habit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.health.measure.R;
import com.gcml.health.measure.bloodpressure_habit.fragment.GetHypertensionHandFragment;
import com.gcml.module_blutooth_devices.base.DealVoiceAndJump;
import com.gcml.module_blutooth_devices.base.FragmentChanged;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/6 11:48
 * created by:gzq
 * description:TODO
 */
public class GetHypertensionHandActivity extends ToolbarBaseActivity implements DealVoiceAndJump, FragmentChanged {
    private FrameLayout mFrameLayout;
    private GetHypertensionHandFragment fragment;

    public static void startActivityForResult(Object host, int requestCode) {
        if (host instanceof Activity) {
            Intent intent = new Intent(((Activity) host), GetHypertensionHandActivity.class);
            ((Activity) host).startActivityForResult(intent, requestCode);
        } else if (host instanceof Fragment) {
            Intent intent = new Intent(((Fragment) host).getContext(), GetHypertensionHandActivity.class);
            ((Fragment) host).startActivityForResult(intent, requestCode);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_diagnosis);
        initView();
        mTitleText.setText("检 测 惯 用 手");
        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
        initFragment();
    }

    private void initFragment() {
        fragment = new GetHypertensionHandFragment();
        fragment.setOnDealVoiceAndJumpListener(this);
        fragment.setOnFragmentChangedListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
    }

    @Override
    protected void backLastActivity() {
        setResult(RESULT_OK);
        super.backLastActivity();
    }

    @Override
    protected void backMainActivity() {
        showRefreshBluetoothDialog();
    }

    private void initView() {
        mFrameLayout = (FrameLayout) findViewById(R.id.frame_layout);
    }

    @Override
    public void updateVoice(String voice) {
        String connected = getResources().getString(R.string.bluetooth_device_connected);
        String disconnected = getResources().getString(R.string.bluetooth_device_disconnected);
        if (connected.equals(voice)) {
            mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_connected);
        } else if (disconnected.equals(voice)) {
            mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
        }
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), voice, false);
    }

    @Override
    public void jump2HealthHistory(int measureType) {

    }

    @Override
    public void jump2DemoVideo(int measureType) {

    }

    @Override
    public void onFragmentChanged(Fragment fragment, Bundle bundle) {
        //TODO:直接返回前一个页面
        finish();
    }

    /**
     * 展示刷新
     */
    private void showRefreshBluetoothDialog() {
        new AlertDialog(this)
                .builder()
                .setMsg("您确定解绑之前的设备，重新连接新设备吗？")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment.autoConnect();
                        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }
}
