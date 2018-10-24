package com.gcml.health.measure.bloodpressure_habit;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.gcml.common.utils.ChannelManagementUtil;
import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.health.measure.R;
import com.gcml.health.measure.bloodpressure_habit.fragment.GetHypertensionHandFragment;
import com.gcml.health.measure.bloodpressure_habit.fragment.GetHypertensionHandXienFragment;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.base.ToolbarBaseActivity;
import com.gcml.lib_utils.data.SPUtil;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.DealVoiceAndJump;
import com.gcml.module_blutooth_devices.base.FragmentChanged;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Xien_Fragment;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.inuker.bluetooth.library.utils.BluetoothUtils;

import java.lang.reflect.Method;
import java.util.List;

import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/9/6 11:48
 * created by:gzq
 * description:TODO
 */
public class GetHypertensionHandActivity extends ToolbarBaseActivity implements DealVoiceAndJump, FragmentChanged {
    private FrameLayout mFrameLayout;
    private BluetoothBaseFragment fragment;

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
        if (ChannelManagementUtil.isXien()){
            fragment=new GetHypertensionHandXienFragment();
        }else{
            fragment = new GetHypertensionHandFragment();
        }
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
                        untieDevice();
                    }
                })
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    private void untieDevice() {
        mRightView.setImageResource(R.drawable.health_measure_ic_bluetooth_disconnected);
        unpairDevice();
        String nameAddress = null;
        nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE, "");
        SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE);
        if (ChannelManagementUtil.isXien()){
            ((Bloodpressure_Xien_Fragment) fragment).onStop();
            ((Bloodpressure_Xien_Fragment) fragment).dealLogic();
        }else{
            ((Bloodpressure_Fragment) fragment).onStop();
            ((Bloodpressure_Fragment) fragment).dealLogic();
        }


        clearBluetoothCache(nameAddress);
    }

    /**
     * 解除已配对设备
     */
    private void unpairDevice() {
        List<BluetoothDevice> devices = BluetoothUtils.getBondedBluetoothClassicDevices();
        for (BluetoothDevice device : devices) {
            try {
                Method m = device.getClass()
                        .getMethod("removeBond", (Class[]) null);
                m.invoke(device, (Object[]) null);
            } catch (Exception e) {
                Timber.e(e.getMessage());
            }
        }

    }

    private void clearBluetoothCache(String nameAddress) {
        if (!TextUtils.isEmpty(nameAddress)) {
            String[] split = nameAddress.split(",");
            if (split.length == 2 && !TextUtils.isEmpty(split[1])) {
                BluetoothClientManager.getClient().refreshCache(split[1]);
            }
        }
    }
}
