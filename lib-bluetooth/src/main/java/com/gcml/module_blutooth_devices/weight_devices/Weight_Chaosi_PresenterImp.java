package com.gcml.module_blutooth_devices.weight_devices;

import android.app.Activity;

import com.gcml.lib_utils.data.SPUtil;
import com.gcml.module_blutooth_devices.R;
import com.gcml.module_blutooth_devices.base.BaseBluetoothPresenter;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.gcml.module_blutooth_devices.base.BluetoothServiceDetail;
import com.gcml.module_blutooth_devices.base.DiscoverDevicesSetting;
import com.gcml.module_blutooth_devices.base.IView;
import com.gcml.module_blutooth_devices.base.Logg;
import com.gcml.module_blutooth_devices.utils.Bluetooth_Constants;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.utils.ByteUtils;

import android.support.v4.app.Fragment;

import java.util.List;
import java.util.UUID;

/**
 * 超思体重秤(必须打开三个通道的监听才有数据)
 * name:iChoice
 * mac:08:7C:BE:21:F5:0D
 */
public class Weight_Chaosi_PresenterImp extends BaseBluetoothPresenter {
    private String targetServiceUUid;
    private static final String targetCharacteristicUUid = "0000cd04-0000-1000-8000-00805f9b34fb";
    private static final String passwordVerifiersWriteUUid = "0000cd20-0000-1000-8000-00805f9b34fb";
    private static final String passwordVerifiersNotify1UUid = "0000cd01-0000-1000-8000-00805f9b34fb";
    private static final String passwordVerifiersNotify2UUid = "0000cd02-0000-1000-8000-00805f9b34fb";
    private static final String passwordVerifiersNotify3UUid = "0000cd03-0000-1000-8000-00805f9b34fb";
    private static final byte[] password = {(byte) 0xAA, 0x55, 0x04, (byte) 0xB1, 0x00, 0x00, (byte) 0xB5};//密码校验指令

    public Weight_Chaosi_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment,discoverSetting);
    }

    @Override
    protected void connectSuccessed(String address, List<BluetoothServiceDetail> serviceDetails, boolean isReturn) {
        super.connectSuccessed(address, serviceDetails, isReturn);
        targetServiceUUid = "ba11f08c-5f14-0b0d-1070-00" + address.toLowerCase().replace(":", "").substring(2);
        baseView.updateState(baseContext.getString(R.string.bluetooth_device_connected));
        baseView.updateData("initialization","0.00");
        SPUtil.put(Bluetooth_Constants.SP.SP_SAVE_WEIGHT,targetName+","+address);
        if (!isReturn) {
            //第一通道监听
            BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid), UUID.fromString(passwordVerifiersNotify1UUid), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    Logg.e(Weight_Chaosi_PresenterImp.class, "onNotify1: pass" + ByteUtils.byteToString(bytes));
                }

                @Override
                public void onResponse(int i) {
                    Logg.e(Weight_Chaosi_PresenterImp.class, "onResponse: cd01" + (i == 0 ? "成功" : "失败"));
                }
            });
            //第二通道监听
            BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid), UUID.fromString(passwordVerifiersNotify2UUid), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    Logg.e(Weight_Chaosi_PresenterImp.class, "onNotify2: pass" + bytes);
                }

                @Override
                public void onResponse(int i) {
                    Logg.e(Weight_Chaosi_PresenterImp.class, "onResponse: cd02" + (i == 0 ? "成功" : "失败"));
                }
            });
            //第三通道
            BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid), UUID.fromString(passwordVerifiersNotify3UUid), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    Logg.e(Weight_Chaosi_PresenterImp.class, "onNotify3: pass" + bytes);
                }

                @Override
                public void onResponse(int i) {
                    Logg.e(Weight_Chaosi_PresenterImp.class, "onResponse: cd03" + (i == 0 ? "成功" : "失败"));
                }
            });
            //写入密码校验
            BluetoothClientManager.getClient().write(address,
                    UUID.fromString(targetServiceUUid),
                    UUID.fromString(passwordVerifiersWriteUUid),
                    password, new BleWriteResponse() {
                        @Override
                        public void onResponse(int i) {
                            Logg.e(Weight_Chaosi_PresenterImp.class, "onResponseWrite: " + i);
                        }
                    });

            BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid), UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    Logg.e(Weight_Chaosi_PresenterImp.class, "onNotify: " + ByteUtils.byteToString(bytes));
                    if (bytes.length == 8) {
                        int h = bytes[6] & 0xff;
                        int l = bytes[5] & 0xff;
                        float weight = ((h << 8) + l) * 0.1f;
                        Logg.e(Weight_Chaosi_PresenterImp.class, "onNotify: 体重:" + weight);
                        baseView.updateData(String.format("%.2f", weight));
                    }
                }

                @Override
                public void onResponse(int i) {

                }
            });
        }
    }
}
