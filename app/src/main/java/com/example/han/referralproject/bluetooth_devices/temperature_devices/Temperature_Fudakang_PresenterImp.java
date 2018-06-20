package com.example.han.referralproject.bluetooth_devices.temperature_devices;

import android.util.Log;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bluetooth_devices.base.BaseBluetoothPresenter;
import com.example.han.referralproject.bluetooth_devices.base.BluetoothDevice;
import com.example.han.referralproject.bluetooth_devices.base.BluetoothServiceDetail;
import com.example.han.referralproject.bluetooth_devices.base.ClientManager;
import com.example.han.referralproject.bluetooth_devices.base.DiscoverDevicesSetting;
import com.example.han.referralproject.bluetooth_devices.base.IView;
import com.example.han.referralproject.bluetooth_devices.base.Logg;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;

import java.util.List;
import java.util.UUID;

/**
 * 福达康耳温枪
 * name:ClinkBlood
 * mac:C6:05:04:03:52:9B
 */
public class
Temperature_Fudakang_PresenterImp extends BaseBluetoothPresenter {
    private Temperature_Fragment fragment;
    private String TAG = Temperature_Fudakang_PresenterImp.class.getSimpleName();
    private static final String targetServiceUUid = "0000fc00-0000-1000-8000-00805f9b34fb";
    private static final String targetCharacteristicUUid = "0000fca1-0000-1000-8000-00805f9b34fb";

    public Temperature_Fudakang_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(discoverSetting);
        this.fragment = (Temperature_Fragment) fragment;
    }

    @Override
    protected void discoveredTargetDevice(BluetoothDevice device) {
        super.discoveredTargetDevice(device);
        Logg.e(Temperature_Fudakang_PresenterImp.class, "discoveredTargetDevice: 发现目标设备");
    }

    @Override
    protected void connectSuccessed(List<BluetoothServiceDetail> serviceDetails) {
        super.connectSuccessed(serviceDetails);
        fragment.updateState(fragment.getString(R.string.bluetooth_device_connected));
        fragment.updateData("0.00");
        if (serviceDetails != null && serviceDetails.size() > 0) {
            ClientManager.getClient().notify(serviceDetails.get(0).getMacAddress(), UUID.fromString(targetServiceUUid), UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    if (bytes.length == 8 && bytes[4] != 85) {
                        byte[] bytes1 = new byte[8];
                        System.arraycopy(bytes, 0, bytes1, 0, 8);
                        float result = ((int) (((bytes1[4] << 8) + (float) (bytes1[5] & 0xff)) / 10)) / 10.0f;
                        Logg.e(Temperature_Fudakang_PresenterImp.class, "onNotify: 结果" + result);
                        fragment.updateData(result + "");
                    }
                }

                @Override
                public void onResponse(int i) {

                }
            });
        }
    }

    @Override
    protected void disConnected() {
        super.disConnected();
        if (fragment.isAdded()) {
            fragment.updateState(fragment.getString(R.string.bluetooth_device_disconnected));
        }
    }
}
