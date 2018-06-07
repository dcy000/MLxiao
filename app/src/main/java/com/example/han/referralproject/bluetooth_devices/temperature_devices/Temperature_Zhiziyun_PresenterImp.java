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
import com.example.han.referralproject.util.ToastTool;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.List;
import java.util.UUID;

/**
 * 小机器人配置的耳温枪
 * 测试机器如下
 * name:FSRKB-EWQ01
 * mac:94:E3:6D:54:1D:2D
 */
public class Temperature_Zhiziyun_PresenterImp extends BaseBluetoothPresenter {
    private Temperature_Fragment activity;
    private static final String targetServiceUUid = "00001910-0000-1000-8000-00805f9b34fb";
    private static final String targetCharacteristicUUid = "0000fff2-0000-1000-8000-00805f9b34fb";

    public Temperature_Zhiziyun_PresenterImp(IView activity, DiscoverDevicesSetting discoverSetting) {
        super(discoverSetting);
        this.activity = (Temperature_Fragment) activity;
    }

    @Override
    protected void discoveredTargetDevice(BluetoothDevice device) {
        super.discoveredTargetDevice(device);
    }

    @Override
    protected void connectSuccessed(List<BluetoothServiceDetail> serviceDetails) {
        activity.updateState(activity.getString(R.string.bluetooth_device_connected));
        activity.updateData("0.00");
        if (serviceDetails != null && serviceDetails.size() > 0) {
            ClientManager.getClient().notify(serviceDetails.get(0).getMacAddress(), UUID.fromString(targetServiceUUid), UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    Logg.e(Temperature_Zhiziyun_PresenterImp.class, "onNotify: " + ByteUtils.byteToString(bytes));
                    if (bytes.length > 6) {
                        int data = bytes[6] & 0xff;
                        if (data < 44) {
                            ToastTool.showShort("测量温度不正常");
                            return;
                        }
                        double v = (data - 44.0) % 10;
                        double result = (30.0 + (data - 44) / 10 + v / 10);
                        activity.updateData(result + "");
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
        Logg.e(Temperature_Zhiziyun_PresenterImp.class, "disConnected: ");
        if (activity.isAdded()) {
            activity.updateState(activity.getString(R.string.bluetooth_device_disconnected));
        }
    }
}
