package com.example.han.referralproject.bluetooth_devices.weight_devices;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;
import android.util.Log;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bluetooth_devices.base.BaseBluetoothPresenter;
import com.example.han.referralproject.bluetooth_devices.base.DiscoverDevicesSetting;
import com.example.han.referralproject.bluetooth_devices.base.IView;
import com.example.han.referralproject.bluetooth_devices.base.Logg;
import com.shhc.bluetoothle.yike.BleStateListener;
import com.shhc.bluetoothle.yike.YKScalesManager;

import java.util.List;

/**
 * 怡可体重秤
 * name:SHHC-60F1
 * mac:8C:8B:83:56:60:F1
 */
public class Weight_Yike_PresenterImp extends BaseBluetoothPresenter {
    private Weight_Fragment fragment;
    private YKScalesManager ykScalesManager;

    public Weight_Yike_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(discoverSetting);
        this.fragment = (Weight_Fragment) fragment;
        ykScalesManager = new YKScalesManager();
        ykScalesManager.init(this.fragment.getContext());
        ykScalesManager.setBleStateListener(bleStateListener);
        ykScalesManager.openBluetoothSetting();
        ykScalesManager.startDiscoveryBluetooth();
    }

    @Override
    public void searchDevices() {
        Logg.e(Weight_Yike_PresenterImp.class, "searchDevices: ");
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

        @Override
        public void scanBleScanFound(BluetoothDevice bluetoothDevice) {
            Logg.e(Weight_Yike_PresenterImp.class, "scanBleScanFound: " + bluetoothDevice.getName());
            if (discoverSetting == null) {
                return;
            }
            String targetMac = discoverSetting.getTargetMac();
            String targetName = discoverSetting.getTargetName();
            String address = bluetoothDevice.getAddress();
            String name = bluetoothDevice.getName();
            if (!TextUtils.isEmpty(targetMac) && address.equals(targetMac)) {
                ykScalesManager.cancelDiscoveryBluetooth();
                ykScalesManager.setUserBaseInfo(170, 1, 25);
                ykScalesManager.connectBluetooth(bluetoothDevice);
            }
        }

        @Override
        public void scanBleFinish() {
            Logg.e(Weight_Yike_PresenterImp.class, "scanBleFinish: ");
        }

        @Override
        public void BleConnectSuccess(BluetoothDevice bluetoothDevice) {
            Logg.e(Weight_Yike_PresenterImp.class, "BleConnectSuccess: ");
            fragment.updateState(fragment.getString(R.string.bluetooth_device_connected));
            fragment.updateData("0.00");
        }

        @Override
        public void BleConnectFail() {
            Logg.e(Weight_Yike_PresenterImp.class, "BleConnectFail: ");
        }

        @Override
        public void BleConnectLost() {
            Logg.e(Weight_Yike_PresenterImp.class, "BleConnectLost: ");
            if (fragment.isAdded()) {
                fragment.updateState(fragment.getString(R.string.bluetooth_device_disconnected));
            }
        }

        @Override
        public void BleWeight() {
            Logg.e(Weight_Yike_PresenterImp.class, "BleWeight: ");
        }

        @Override
        public void BleReadRS(float v, List<Float> list, List<Float> list1) {
            Logg.e(Weight_Yike_PresenterImp.class, "BleReadRS: " + v);
            fragment.updateData(v+"");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        ykScalesManager.cancelDiscoveryBluetooth();
        ykScalesManager.disconnectBluetooth();
        ykScalesManager.close();
    }
}
