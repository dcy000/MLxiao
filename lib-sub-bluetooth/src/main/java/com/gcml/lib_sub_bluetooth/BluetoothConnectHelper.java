package com.gcml.lib_sub_bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;

import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.utils.BluetoothUtils;

/**
 * 蓝牙连接帮助类
 */
public class BluetoothConnectHelper {
    private static final String TAG = "BluetoothConnectHelper";
    private MyBleConnectResponse connectResponse;
    private ConnectListener connectListener;
    private boolean isClear = false;
    private MyBleConnectStatusListener connectStatusListener;
    private String address = null;
    private boolean isConnected = false;

    public void connect(String macAddress, ConnectListener listener) {
        if (TextUtils.isEmpty(macAddress)) {
            return;
        }
        address = macAddress;
        connectListener = listener;
        if (connectResponse == null) {
            connectResponse = new MyBleConnectResponse();
        }
        if (connectStatusListener == null) {
            connectStatusListener = new MyBleConnectStatusListener();
        }
        BluetoothStore.getClient().registerConnectStatusListener(macAddress, connectStatusListener);

        BluetoothStore.getClient().connect(macAddress, connectResponse);
    }

    class MyBleConnectResponse implements BleConnectResponse {

        @Override
        public void onResponse(int code, BleGattProfile data) {
            if (connectListener != null && !isClear) {
                if (code != 0) {
                    connectListener.failed();
                }
            } else {
                isConnected = true;
            }
        }
    }

    class MyBleConnectStatusListener extends BleConnectStatusListener {

        @SuppressLint("MissingPermission")
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (connectListener != null && !isClear) {
                if (status == 16) {
                    isConnected = true;
                    BluetoothDevice remoteDevice = BluetoothUtils.getRemoteDevice(mac);
                    connectListener.success(remoteDevice == null ? null : remoteDevice.getName(), mac);
                } else if (status == 32) {
                    isConnected = false;
                    connectListener.disConnect(mac);
                }
            }
        }
    }

    public synchronized void clear() {
        isClear = true;

        connectStatusListener = null;
        connectListener = null;
        if (connectStatusListener != null) {
            BluetoothStore.getClient().unregisterConnectStatusListener(address, connectStatusListener);
        }
        connectStatusListener = null;

        if (address == null) {
            return;
        }
        if (isConnected) {
            isConnected = false;
            BluetoothStore.getClient().disconnect(address);
        }
        //清除队列中缓存
        BluetoothStore.getClient().clearRequest(address, 0);
    }
}
