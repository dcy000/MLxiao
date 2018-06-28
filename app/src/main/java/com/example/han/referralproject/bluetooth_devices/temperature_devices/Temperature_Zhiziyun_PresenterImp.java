package com.example.han.referralproject.bluetooth_devices.temperature_devices;

import android.util.Log;

import com.example.han.referralproject.bluetooth_devices.base.BaseBluetoothPresenter;
import com.example.han.referralproject.bluetooth_devices.base.BluetoothDevice;
import com.example.han.referralproject.bluetooth_devices.base.BluetoothServiceDetail;
import com.example.han.referralproject.bluetooth_devices.base.ClientManager;
import com.example.han.referralproject.bluetooth_devices.base.DiscoverDevicesSetting;
import com.example.han.referralproject.bluetooth_devices.base.IView;
import com.example.han.referralproject.util.ToastTool;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.List;
import java.util.UUID;

/**
 * 小机器人配置的耳温枪
 */
public class Temperature_Zhiziyun_PresenterImp extends BaseBluetoothPresenter {
    private MeasureTemperatureActivity activity;
    private static final String targetServiceUUid = "00001910-0000-1000-8000-00805f9b34fb";
    private static final String targetCharacteristicUUid = "0000fff2-0000-1000-8000-00805f9b34fb";

    public Temperature_Zhiziyun_PresenterImp(IView activity, DiscoverDevicesSetting discoverSetting) {
        super(discoverSetting);
        this.activity = (MeasureTemperatureActivity) activity;
    }

    @Override
    public void stateChanged(int state) {
        switch (state) {
            case DEVICE_FOUNDED://发现目标设备
                connectDevice();
                break;
            case DEVICE_UNFOUNDED://未发现目标设备
                activity.updateData("未发现目标设备");
                break;
            case DEVICE_CONNECTED://设备连接成功
                activity.updateData("连接设备成功");
                break;
            case DEVICE_DISCONNECTED://设备连接断开
                activity.updateData("设备已断开");
                break;

        }
    }

    @Override
    protected void discoverTargetDevice(BluetoothDevice device) {
        activity.updateData("发现目标设备");
    }

    @Override
    protected void connectSuccessed(List<BluetoothServiceDetail> serviceDetails) {
        if (serviceDetails != null && serviceDetails.size() > 0) {
            ClientManager.getClient().notify(serviceDetails.get(0).getMacAddress(), UUID.fromString(targetServiceUUid), UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    Log.e("温度情况", "onNotify: " + ByteUtils.byteToString(bytes));
                    if (bytes.length > 6) {
                        int data = bytes[6] & 0xff;
                        if (data < 44) {
                            ToastTool.showShort("测量温度不正常");
                            return;
                        }
                        double v = (data - 44.0) % 10;
                        double result = (30.0 + (data - 44) / 10 + v/10);
                        activity.updateData(result + "℃");
                    }
                }

                @Override
                public void onResponse(int i) {

                }
            });
        }
    }
}
