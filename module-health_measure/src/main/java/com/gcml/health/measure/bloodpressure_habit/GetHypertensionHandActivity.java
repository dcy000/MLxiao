package com.gcml.health.measure.bloodpressure_habit;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.gcml.health.measure.R;
import com.gcml.health.measure.bloodpressure_habit.fragment.GetHypertensionHandFragment;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.base.ToolbarBaseActivity;
import com.gcml.lib_utils.data.SPUtil;
import com.gcml.lib_utils.ui.dialog.BaseDialog;
import com.gcml.lib_utils.ui.dialog.DialogClickSureListener;
import com.gcml.lib_utils.ui.dialog.DialogSureCancel;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.DealVoiceAndJump;
import com.gcml.module_blutooth_devices.base.FragmentChanged;
import com.gcml.module_blutooth_devices.bloodpressure_devices.Bloodpressure_Fragment;
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
    private GetHypertensionHandFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_diagnosis);
        initView();
        mTitleText.setText("检 测 惯 用 手");
        initFragment();
    }

    private void initFragment() {
        fragment = new GetHypertensionHandFragment();
        fragment.setOnDealVoiceAndJumpListener(this);
        fragment.setOnFragmentChangedListener(this);
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
        DialogSureCancel sureCancel = new DialogSureCancel(this);
        sureCancel.setContent("您确定解绑之前的设备，重新连接新设备吗？");
        sureCancel.show();
        sureCancel.setOnClickCancelListener(null);
        sureCancel.setOnClickSureListener(new DialogClickSureListener() {
            @Override
            public void clickSure(BaseDialog dialog) {
                sureCancel.dismiss();
                untieDevice();
            }
        });
    }

    private void untieDevice() {
        unpairDevice();
        String nameAddress = null;
        nameAddress = (String) SPUtil.get(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE, "");
        SPUtil.remove(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE);
        ((Bloodpressure_Fragment) fragment).onStop();
        ((Bloodpressure_Fragment) fragment).dealLogic();

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
