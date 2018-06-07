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

import java.util.List;
import java.util.UUID;

/**
 * 爱立康耳温枪
 * 测试信息：
 * name:AET-WD
 * mac:04:B3:EC:50:6D:D3
 */

public class Temperature_Ailikang_PresenterImp extends BaseBluetoothPresenter {
    private static final String targetServiceUUid = "0000ffb0-0000-1000-8000-00805f9b34fb";
    private static final String targetCharacteristicUUid = "0000ffb2-0000-1000-8000-00805f9b34fb";
    private Temperature_Fragment fragment;
    private static String TAG = Temperature_Ailikang_PresenterImp.class.getSimpleName();

    public Temperature_Ailikang_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(discoverSetting);
        this.fragment = (Temperature_Fragment) fragment;
    }

    @Override
    protected void discoveredTargetDevice(BluetoothDevice device) {
        super.discoveredTargetDevice(device);
        Logg.e(Temperature_Ailikang_PresenterImp.class, "discoveredTargetDevice: 发现目标设备");
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
                    float result;
                    if (bytes.length == 13) {
                        result = bytes[9] + bytes[10] / 10 / 10.0f;
                    } else {
                        result = 0.0f;
                    }
                    Logg.e(Temperature_Ailikang_PresenterImp.class, "onNotify: 体温" + result);
                    fragment.updateData(result + "");
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
        Logg.e(Temperature_Ailikang_PresenterImp.class, "disConnected: ");
        if (fragment.isAdded())
            fragment.updateState(fragment.getString(R.string.bluetooth_device_disconnected));
    }
}
