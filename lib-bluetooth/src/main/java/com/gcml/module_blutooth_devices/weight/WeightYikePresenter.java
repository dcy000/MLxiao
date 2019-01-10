package com.gcml.module_blutooth_devices.weight;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.bluetooth.BluetoothDevice;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.text.TextUtils;

import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.data.SPUtil;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.bluetooth.IBluetoothView;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.gcml.module_blutooth_devices.weight_devices.Weight_Yike_PresenterImp;
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
            Logg.e(Weight_Yike_PresenterImp.class, "unableBleModule: ");
        }

        @Override
        public void openBleSettingSuccess() {
            Logg.e(Weight_Yike_PresenterImp.class, "openBleSettingSuccess: ");
        }

        @Override
        public void openBleSettingCancel() {
            Logg.e(Weight_Yike_PresenterImp.class, "openBleSettingCancel: ");
        }

        @SuppressLint("MissingPermission")
        @Override
        public void scanBleScanFound(BluetoothDevice bluetoothDevice) {

        }

        @Override
        public void scanBleFinish() {
            Logg.e(Weight_Yike_PresenterImp.class, "scanBleFinish: ");
        }

        @SuppressLint("MissingPermission")
        @Override
        public void BleConnectSuccess(BluetoothDevice bluetoothDevice) {
            baseView.updateState(UtilsManager.getApplication().getString(R.string.bluetooth_device_connected));
            baseView.updateData("initialization", "0.00");
            SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_WEIGHT, bluetoothDevice.getName() + "," + bluetoothDevice.getAddress());
        }

        @Override
        public void BleConnectFail() {
            Logg.e(Weight_Yike_PresenterImp.class, "BleConnectFail: ");
        }

        @Override
        public void BleConnectLost() {
            Logg.e(Weight_Yike_PresenterImp.class, "BleConnectLost: ");
            if (((Fragment) baseView).isAdded()) {
                baseView.updateState(UtilsManager.getApplication().getString(R.string.bluetooth_device_disconnected));
            }
        }

        @Override
        public void BleWeight() {
            Logg.e(Weight_Yike_PresenterImp.class, "BleWeight: ");
        }

        @Override
        public void BleReadRS(float v, List<Float> list, List<Float> list1) {
            Logg.e(Weight_Yike_PresenterImp.class, "BleReadRS: " + v);
            baseView.updateData(v + "");
        }

        @Override
        public void BleFail(int arg0) {
            if (YKScalesManager.ERROR_CODE_NOT_SHHC_MACHINE == arg0) {
                ykScalesManager.disconnectBluetooth();
                Logg.e(Weight_Yike_PresenterImp.class, "断开连接，请连接怡可设备");
            } else if (YKScalesManager.ERROR_CODE_NOT_HAS_INFO == arg0) {
                ykScalesManager.disconnectBluetooth();
                Logg.e(Weight_Yike_PresenterImp.class, "断开连接，请出入基本信息");
            } else {
                Logg.e(Weight_Yike_PresenterImp.class, "BleFail: ");
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
