package com.gcml.module_blutooth_devices.bloodpressure_devices;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;

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
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.List;
import java.util.UUID;

/**
 * 超思血压计
 * name:iChoice
 * mac:40:4E:EB:04:10:11
 * 备注：测量过程中没有实时数据返回
 */
public class Bloodpressure_Chaosi_PresenterImp extends BaseBluetoothPresenter {
    private String targetServiceUUid;
    private static final String targetCharacteristicUUid = "0000cd04-0000-1000-8000-00805f9b34fb";
    private static final String passwordVerifiersWriteUUid = "0000cd20-0000-1000-8000-00805f9b34fb";
    private static final String passwordVerifiersNotify1UUid = "0000cd01-0000-1000-8000-00805f9b34fb";
    private static final String passwordVerifiersNotify2UUid = "0000cd02-0000-1000-8000-00805f9b34fb";
    private static final String passwordVerifiersNotify3UUid = "0000cd03-0000-1000-8000-00805f9b34fb";
    private static final byte[] password = {(byte) 0xAA, 0x55, 0x04, (byte) 0xB1, 0x00, 0x00, (byte) 0xB5};//密码校验指令
//    private IView fragment;

    public Bloodpressure_Chaosi_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(fragment,discoverSetting);
//        this.fragment = fragment;
    }

    @Override
    protected boolean discoveredTargetDevice(SearchResult device) {
        super.discoveredTargetDevice(device);
        return false;
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void connectSuccessed(String address, List<BluetoothServiceDetail> serviceDetails, boolean isReturn) {
        super.connectSuccessed(address, serviceDetails, isReturn);
        targetServiceUUid = "ba11f08c-5f14-0b0d-10a0-00" + address.toLowerCase().replace(":", "").substring(2);
        Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "connectSuccessed: ；连接成功");
        baseView.updateState(baseView.getThisContext().getString(R.string.bluetooth_device_connected));
        baseView.updateData("0", "0", "0");
        SPUtil.put( Bluetooth_Constants.SP.SP_SAVE_BLOODPRESSURE, targetName +","+ address);
        if (!isReturn) {
            //第一通道
            BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid), UUID.fromString(passwordVerifiersNotify1UUid), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onNotify: " + ByteUtils.byteToString(bytes));
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(int i) {
                    Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onResponse: cd01" + (i == 0 ? "成功" : "失败"));
                }
            });

            //第二通道监听
            BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid), UUID.fromString(passwordVerifiersNotify2UUid), new BleNotifyResponse() {
                @SuppressLint("LongLogTag")
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onNotify2: pass" + ByteUtils.byteToString(bytes));
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(int i) {
                    Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onResponse: cd02" + (i == 0 ? "成功" : "失败"));
                }
            });
            //第三通道
            BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid), UUID.fromString(passwordVerifiersNotify3UUid), new BleNotifyResponse() {
                @SuppressLint("LongLogTag")
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onNotify3: pass" + ByteUtils.byteToString(bytes));
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(int i) {
                    Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onResponse: cd03" + (i == 0 ? "成功" : "失败"));
                }
            });
            //第四通道
            BluetoothClientManager.getClient().notify(address, UUID.fromString(targetServiceUUid), UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                @SuppressLint("LongLogTag")
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onNotify: " + bytes.length);
                    if (bytes.length == 12) {
                        Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onNotify: 收缩压：" + (bytes[4] + bytes[5]));
                        Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onNotify: 舒张压：" + (bytes[6] + bytes[7]));
                        Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onNotify: 脉搏" + (bytes[8] + bytes[9]));
                        baseView.updateData((bytes[4] + bytes[5]) + "", (bytes[6] + bytes[7]) + "", (bytes[8] + bytes[9]) + "");
                    }
                }

                @Override
                public void onResponse(int i) {
                    Logg.e(Bloodpressure_Chaosi_PresenterImp.class, "onResponse: cd04" + (i == 0 ? "成功" : "失败"));
                }
            });

            //写入密码校验
            BluetoothClientManager.getClient().write(address,
                    UUID.fromString(targetServiceUUid),
                    UUID.fromString(passwordVerifiersWriteUUid),
                    password, new BleWriteResponse() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onResponse(int i) {
                            Logg.d(Bloodpressure_Chaosi_PresenterImp.class, "onResponseWrite: " + i);
                        }
                    });
        }

    }
}
