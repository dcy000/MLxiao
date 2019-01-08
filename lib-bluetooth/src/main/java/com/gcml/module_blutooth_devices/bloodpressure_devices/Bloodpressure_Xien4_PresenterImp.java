package com.gcml.module_blutooth_devices.bloodpressure_devices;

import android.util.Log;

import com.gcml.common.utils.data.SPUtil;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.BluetoothServiceDetail;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.List;
import java.util.UUID;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/10/26 17:25
 * created by: gzq
 * description: 西恩4.0设备
 * name:LD
 * MAC:FF:FF:5A:00:00:EC
 */
public class Bloodpressure_Xien4_PresenterImp extends BaseBluetoothPresenter {
    private static final String TAG = "Bloodpressure_Xien4_Pre";
    private static final String targetServiceUUid = "01000000-0000-0000-0000-000000000080";
    private static final String targetWriteCharacteristicUUid = "05000000-0000-0000-0000-000000000080";
    private static final String targetCharacteristicUUid = "02000000-0000-0000-0000-000000000080";
    private byte[] commands = {(byte) 0xff, (byte) 0xff, 0x05, 0x01, (byte) 0xfa};
    private boolean isConnect = false;
    private String connectAddress = null;

    public Bloodpressure_Xien4_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment, discoverSetting);
    }

    @Override
    protected void connectSuccessed(String address, List<BluetoothServiceDetail> serviceDetails, boolean isReturnServiceAndCharacteristic) {
        super.connectSuccessed(address, serviceDetails, isReturnServiceAndCharacteristic);
        isConnect = true;
        connectAddress = address;
        baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
        baseView.updateData("0", "0", "0");
        SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE, targetName + "," + address);
        if (!isReturnServiceAndCharacteristic) {
            BluetoothClientManager.getClient().notify(address,
                    UUID.fromString(targetServiceUUid),
                    UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                        @Override
                        public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                            if (bytes.length == 11 && (bytes[3] & 0xff) == 10 && (bytes[4] & 0xff) == 2) {
                                int hight = (bytes[6] & 0xff) | (bytes[7] << 8 & 0xff00);
                                baseView.updateData(String.valueOf(hight));
                            } else if (bytes.length == 9 && (bytes[3] & 0xff) == 73 && (bytes[4] & 0xff) == 3) {
                                int highPress = (bytes[6] & 0xff) + 30;
                                int lowPress = (bytes[7] & 0xff) + 30;
                                int pulse = bytes[5] & 0xff;

                                baseView.updateData(highPress + "", lowPress + "", pulse + "");
                            }
                        }

                        @Override
                        public void onResponse(int i) {
                            Log.e(TAG, "onResponse:打开notify " + i);
                        }
                    });
        }
    }

    @Override
    protected void disConnected() {
        super.disConnected();
        isConnect = false;
    }

    public void startMeasure() {
        if (!isConnect || connectAddress == null) {
            baseView.updateState("设备未连接");
            return;
        }
        BluetoothClientManager.getClient().write(connectAddress,
                UUID.fromString(targetServiceUUid),
                UUID.fromString(targetWriteCharacteristicUUid), commands, new BleWriteResponse() {
                    @Override
                    public void onResponse(int code) {
                        Log.e(TAG, "onResponse:写入指令 " + code);
                    }
                });

    }
}