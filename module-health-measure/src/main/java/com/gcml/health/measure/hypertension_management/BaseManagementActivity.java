package com.gcml.health.measure.hypertension_management;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.health.measure.R;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.DealVoiceAndJump;
import com.gcml.module_blutooth_devices.base.FragmentChanged;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/2 19:34
 * created by:gzq
 * description:TODO
 */
public class BaseManagementActivity extends ToolbarBaseActivity implements DealVoiceAndJump, FragmentChanged {
    protected int measure_type;
    protected BluetoothBaseFragment baseFragment;
    protected FrameLayout mFrame;

    interface ResultAction {
        String MEASURE_SUCCESS = "measure_success";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_measure_activity_all_measure);
        initView();
        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
        dealLogic();
    }

    @Override
    protected void backMainActivity() {
        showRefreshBluetoothDialog();
    }

    /**
     * 展示刷新
     */
    private void showRefreshBluetoothDialog() {
        new AlertDialog(BaseManagementActivity.this)
                .builder()
                .setMsg("您确定解绑之前的设备，重新连接新设备吗？")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        untieDevice();
                    }
                }).show();
    }

    protected void untieDevice() {

    }

    @CallSuper
    protected void dealLogic() {
        if (baseFragment != null) {
            baseFragment.setOnDealVoiceAndJumpListener(this);
            baseFragment.setOnFragmentChangedListener(this);
        }
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baseFragment = null;
        MLVoiceSynthetize.destory();
    }

    private void initView() {
        mFrame = (FrameLayout) findViewById(R.id.frame);
    }
}
