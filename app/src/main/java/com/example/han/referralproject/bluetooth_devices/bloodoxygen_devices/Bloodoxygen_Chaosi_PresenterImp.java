package com.example.han.referralproject.bluetooth_devices.bloodoxygen_devices;

import android.util.Log;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bluetooth_devices.base.BaseBluetoothPresenter;
import com.example.han.referralproject.bluetooth_devices.base.BluetoothDevice;
import com.example.han.referralproject.bluetooth_devices.base.BluetoothServiceDetail;
import com.example.han.referralproject.bluetooth_devices.base.ClientManager;
import com.example.han.referralproject.bluetooth_devices.base.DiscoverDevicesSetting;
import com.example.han.referralproject.bluetooth_devices.base.IView;
import com.example.han.referralproject.bluetooth_devices.base.Logg;
import com.example.han.referralproject.util.LocalShared;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.search.SearchResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * 超思指夹血氧仪(必须打开三个通道的监听才有数据)
 * name:iChoice
 * mac:08:7C:BE:42:3E:B1
 */
public class Bloodoxygen_Chaosi_PresenterImp extends BaseBluetoothPresenter {
    private static final String targetServiceUUid = "ba11f08c-5f14-0b0d-1080-007cbe423eb1";
    private static final String targetCharacteristicUUid = "0000cd04-0000-1000-8000-00805f9b34fb";
    private static final String passwordVerifiersWriteUUid = "0000cd20-0000-1000-8000-00805f9b34fb";
    private static final String passwordVerifiersNotify1UUid = "0000cd01-0000-1000-8000-00805f9b34fb";
    private static final String passwordVerifiersNotify2UUid = "0000cd02-0000-1000-8000-00805f9b34fb";
    private static final String passwordVerifiersNotify3UUid = "0000cd03-0000-1000-8000-00805f9b34fb";
    private static final byte[] password = {(byte) 0xAA, 0x55, 0x04, (byte) 0xB1, 0x00, 0x00, (byte) 0xB5};//密码校验指令
    private Bloodoxygen_Fragment fragment;


    public Bloodoxygen_Chaosi_PresenterImp(IView fragment, DiscoverDevicesSetting discoverSetting) {
        super(discoverSetting);
        this.fragment = (Bloodoxygen_Fragment) fragment;

    }

    @Override
    protected void discoveredTargetDevice(BluetoothDevice device) {
        super.discoveredTargetDevice(device);
        Logg.e(Bloodoxygen_Chaosi_PresenterImp.class, "discoveredTargetDevice: 发现设备");
    }

    @Override
    protected void connectSuccessed(List<BluetoothServiceDetail> serviceDetails) {
        super.connectSuccessed(serviceDetails);
        fragment.updateState(fragment.getString(R.string.bluetooth_device_connected));
        fragment.updateData("0", "0");
        LocalShared.getInstance(fragment.getContext()).setXueyangMac(lockedDevice.getSearchResult().getAddress());
        if (serviceDetails != null && serviceDetails.size() > 0) {
            //第一通道监听
            ClientManager.getClient().notify(serviceDetails.get(0).getMacAddress(), UUID.fromString(targetServiceUUid), UUID.fromString(passwordVerifiersNotify1UUid), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    Logg.d(Bloodoxygen_Chaosi_PresenterImp.class, "onNotify1: pass" + bytes);
                }

                @Override
                public void onResponse(int i) {
                    Logg.d(Bloodoxygen_Chaosi_PresenterImp.class, "onResponse: cd01" + (i == 0 ? "成功" : "失败"));
                }
            });
            //第二通道监听
            ClientManager.getClient().notify(serviceDetails.get(0).getMacAddress(), UUID.fromString(targetServiceUUid), UUID.fromString(passwordVerifiersNotify2UUid), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    Logg.d(Bloodoxygen_Chaosi_PresenterImp.class, "onNotify2: pass" + bytes);
                }

                @Override
                public void onResponse(int i) {
                    Logg.d(Bloodoxygen_Chaosi_PresenterImp.class, "onResponse: cd02" + (i == 0 ? "成功" : "失败"));
                }
            });
            //第三通道
            ClientManager.getClient().notify(serviceDetails.get(0).getMacAddress(), UUID.fromString(targetServiceUUid), UUID.fromString(passwordVerifiersNotify3UUid), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    Logg.d(Bloodoxygen_Chaosi_PresenterImp.class, "onNotify3: pass" + bytes);
                }

                @Override
                public void onResponse(int i) {
                    Logg.d(Bloodoxygen_Chaosi_PresenterImp.class, "onResponse: cd03" + (i == 0 ? "成功" : "失败"));
                }
            });
            //写入密码校验
            ClientManager.getClient().write(serviceDetails.get(0).getMacAddress(),
                    UUID.fromString(targetServiceUUid),
                    UUID.fromString(passwordVerifiersWriteUUid),
                    password, new BleWriteResponse() {
                        @Override
                        public void onResponse(int i) {
                            Logg.d(Bloodoxygen_Chaosi_PresenterImp.class, "onResponseWrite: " + i);
                        }
                    });

            ClientManager.getClient().notify(serviceDetails.get(0).getMacAddress(), UUID.fromString(targetServiceUUid), UUID.fromString(targetCharacteristicUUid), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                    if (bytes.length == 6) {
                        fragment.updateData(bytes[3] + "", bytes[4] + "");
                        Logg.e(Bloodoxygen_Chaosi_PresenterImp.class, "onNotify: 血氧：" + bytes[3]);
                        Logg.e(Bloodoxygen_Chaosi_PresenterImp.class, "onNotify: 脉搏" + bytes[4]);
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
        Logg.e(Bloodoxygen_Chaosi_PresenterImp.class, "disConnected: ");
        if (fragment.isAdded()) {
            fragment.updateState(fragment.getString(R.string.bluetooth_device_disconnected));
        }
    }

}
