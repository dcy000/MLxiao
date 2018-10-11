package com.gcml.health.measure.hypertension_management;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.gcml.common.widget.dialog.AlertDialog;
import com.gcml.health.measure.R;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.base.ToolbarBaseActivity;
import com.gcml.lib_utils.data.SPUtil;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.DealVoiceAndJump;
import com.gcml.module_blutooth_devices.base.FragmentChanged;
import com.gcml.module_blutooth_devices.base.IPresenter;
import com.gcml.module_blutooth_devices.bloodoxygen_devices.Bloodoxygen_Fragment;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
import com.gcml.module_blutooth_devices.bloodsugar_devices.Bloodsugar_Fragment;
import com.gcml.module_blutooth_devices.ecg_devices.ECG_Fragment;
import com.gcml.module_blutooth_devices.fingerprint_devices.Fingerpint_Fragment;
import com.gcml.module_blutooth_devices.others.ThreeInOne_Fragment;
import com.gcml.module_blutooth_devices.temperature_devices.Temperature_Fragment;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Fragment;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.inuker.bluetooth.library.utils.BluetoothUtils;

import java.lang.reflect.Method;
import java.util.List;

import timber.log.Timber;

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

    @CallSuper
    protected void untieDevice() {
        //先清除已经绑定的设备
        unpairDevice();
    }

    protected void clearBluetoothCache(String nameAddress) {
        if (!TextUtils.isEmpty(nameAddress)) {
            String[] split = nameAddress.split(",");
            if (split.length == 2 && !TextUtils.isEmpty(split[1])) {
                BluetoothClientManager.getClient().refreshCache(split[1]);
            }
        }
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
