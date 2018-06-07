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
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.List;
import java.util.UUID;

/**
 * 美的连耳温枪
 * name:MEDXING-IRT
 * mac:00:13:04:25:A7:65
 */
public class Temperature_Meidilian_PresenterImp extends BaseBluetoothPresenter {
    private Temperature_Fragment fragment;
    private static final String targetServiceUUid = "0000ffb0-0000-1000-8000-00805f9b34fb";
    private static final String targetCharacteristicUUid = "0000ffb2-0000-1000-8000-00805f9b34fb";
    private static String TAG = Temperature_Meidilian_PresenterImp.class.getSimpleName();

    public Temperature_Meidilian_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(discoverSetting);
        this.fragment = (Temperature_Fragment) fragment;
    }

    @Override
    protected void discoveredTargetDevice(BluetoothDevice device) {
        //此处的super.必须书写
        super.discoveredTargetDevice(device);
    }


    @Override
    protected void connectSuccessed(final List<BluetoothServiceDetail> serviceDetails) {
        fragment.updateState(fragment.getString(R.string.bluetooth_device_connected));
        fragment.updateData("0.00");
        if (serviceDetails != null && serviceDetails.size() > 0) {
            ClientManager.getClient().notify(serviceDetails.get(0).getMacAddress(), UUID.fromString(targetServiceUUid), UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    Logg.e(Temperature_Meidilian_PresenterImp.class, "onNotify: " + ByteUtils.byteToString(bytes) + "长度：" + bytes.length);
                    if (bytes.length == 4) {
                        float result = ((float) (bytes[3] << 8) + (float) (bytes[2] & 0xff)) / 10;
                        Logg.e(Temperature_Meidilian_PresenterImp.class, "onNotify: " + result);
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
        Logg.e(Temperature_Meidilian_PresenterImp.class, "disConnected: ");
        if (fragment.isAdded()) {
            fragment.updateState(fragment.getString(R.string.bluetooth_device_disconnected));
        }
    }
}
