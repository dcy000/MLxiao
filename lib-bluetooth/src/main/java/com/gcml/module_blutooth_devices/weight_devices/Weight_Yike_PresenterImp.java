package com.gcml.module_blutooth_devices.weight_devices;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import com.gcml.lib_utils.data.SPUtil;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.shhc.bluetoothle.yike.BleStateListener;
import com.shhc.bluetoothle.yike.YKScalesManager;

import java.util.List;

/**
 * 怡可体重秤（鹿得）
 * name:SHHC-60F1
 * mac:8C:8B:83:56:60:F1
 * 备注：该设备需要脱鞋测量，并且在看见自己体重的时候还不能下秤，
 * 还需要等秤上显示屏的横条“----”显示满才能同步数据到上位机
 */
public class Weight_Yike_PresenterImp extends BaseBluetoothPresenter {
    private YKScalesManager ykScalesManager;

    public Weight_Yike_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
        ykScalesManager = new YKScalesManager();
        ykScalesManager.init(baseContext);
        ykScalesManager.setBleStateListener(bleStateListener);
        searchDevices();
    }

    @Override
    protected boolean isSelfDefined() {
        return true;
    }

    @Override
    public void searchDevices() {
        ykScalesManager.openBluetoothSetting();
        ykScalesManager.startDiscoveryBluetooth();
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
            Logg.e(Weight_Yike_PresenterImp.class, "scanBleScanFound: " + bluetoothDevice.getName());
            if (discoverSetting == null) {
                return;
            }
            lockedDevice=bluetoothDevice;
            String targetMac = discoverSetting.getTargetMac();
            String targetName = discoverSetting.getTargetName();
            String address = bluetoothDevice.getAddress();
            String name = bluetoothDevice.getName();
            if (!TextUtils.isEmpty(targetMac) && address.equals(targetMac)) {
                ykScalesManager.cancelDiscoveryBluetooth();
                //身高、性别、年龄
                ykScalesManager.setUserBaseInfo(170, 1, 25);
                ykScalesManager.connectBluetooth(bluetoothDevice);
            }
        }

        @Override
        public void scanBleFinish() {
            Logg.e(Weight_Yike_PresenterImp.class, "scanBleFinish: ");
        }

        @SuppressLint("MissingPermission")
        @Override
        public void BleConnectSuccess(BluetoothDevice bluetoothDevice) {
            Logg.e(Weight_Yike_PresenterImp.class, "BleConnectSuccess: ");
            baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
            baseView.updateData("0.00");
            SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_WEIGHT,bluetoothDevice.getName()+","+bluetoothDevice.getAddress());
        }

        @Override
        public void BleConnectFail() {
            Logg.e(Weight_Yike_PresenterImp.class, "BleConnectFail: ");
        }

        @Override
        public void BleConnectLost() {
            Logg.e(Weight_Yike_PresenterImp.class, "BleConnectLost: ");
            if (((Fragment) baseView).isAdded()) {
                baseView.updateState(baseContext.getString(R.string.bluetooth_device_disconnected));
            }

            if (!isDestroy){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ykScalesManager.connectBluetooth(lockedDevice);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        ykScalesManager.cancelDiscoveryBluetooth();
        ykScalesManager.disconnectBluetooth();
        ykScalesManager.close();
    }
}
