package com.gcml.module_blutooth_devices.weight;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.bluetooth.BluetoothDevice;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;

import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.IBluetoothView;
import com.gcml.module_blutooth_devices.utils.BluetoothConstants;
import com.inuker.bluetooth.library.utils.BluetoothUtils;
import com.shhc.bluetoothle.yike.BleStateListener;
import com.shhc.bluetoothle.yike.YKScalesManager;

import java.util.List;

public class WeightYikePresenter implements LifecycleObserver {
    private SupportActivity activity;
    private IBluetoothView baseView;
    private String name;
    private String address;
    private YKScalesManager ykScalesManager;

    @SuppressLint("RestrictedApi")
    public WeightYikePresenter(SupportActivity activity, IBluetoothView baseView, String name, String address) {
        this.activity = activity;
        this.baseView = baseView;
        this.name = name;
        this.address = address;
        this.activity.getLifecycle().addObserver(this);

        ykScalesManager = new YKScalesManager();
        ykScalesManager.init(activity);
        //身高、性别、年龄
        ykScalesManager.setUserBaseInfo(170, 1, 25);
        ykScalesManager.setBleStateListener(bleStateListener);
        connect(address);
    }

    private void connect(String address) {
        BluetoothDevice bluetoothDevice = BluetoothUtils.getRemoteDevice(address);
        if (bluetoothDevice == null) {
            return;
        }
        ykScalesManager.connectBluetooth(bluetoothDevice);
    }

    private final BleStateListener bleStateListener = new BleStateListener() {
        @Override
        public void unableBleModule() {
        }

        @Override
        public void openBleSettingSuccess() {
        }

        @Override
        public void openBleSettingCancel() {
        }

        @SuppressLint("MissingPermission")
        @Override
        public void scanBleScanFound(BluetoothDevice bluetoothDevice) {

        }

        @Override
        public void scanBleFinish() {
        }

        @SuppressLint("MissingPermission")
        @Override
        public void BleConnectSuccess(BluetoothDevice bluetoothDevice) {
            baseView.updateState(UtilsManager.getApplication().getString(R.string.bluetooth_device_connected));
            baseView.updateData("initialization", "0.00");
            SPUtil.put(BluetoothConstants.SP.SP_SAVE_WEIGHT, bluetoothDevice.getName() + "," + bluetoothDevice.getAddress());
        }

        @Override
        public void BleConnectFail() {
        }

        @Override
        public void BleConnectLost() {
            if (((Fragment) baseView).isAdded()) {
                baseView.updateState(UtilsManager.getApplication().getString(R.string.bluetooth_device_disconnected));
            }
        }

        @Override
        public void BleWeight() {
        }

        @Override
        public void BleReadRS(float v, List<Float> list, List<Float> list1) {
            baseView.updateData(v + "");
        }

        @Override
        public void BleFail(int arg0) {
            if (YKScalesManager.ERROR_CODE_NOT_SHHC_MACHINE == arg0) {
                ykScalesManager.disconnectBluetooth();
            } else if (YKScalesManager.ERROR_CODE_NOT_HAS_INFO == arg0) {
                ykScalesManager.disconnectBluetooth();
            } else {
            }
        }
    };

    @SuppressLint("RestrictedApi")
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        if (ykScalesManager != null) {
            ykScalesManager.cancelDiscoveryBluetooth();
            ykScalesManager.disconnectBluetooth();
            ykScalesManager.close();
        }
        ykScalesManager = null;
        if (activity != null) {
            activity.getLifecycle().removeObserver(this);
        }
        activity = null;
    }
}
