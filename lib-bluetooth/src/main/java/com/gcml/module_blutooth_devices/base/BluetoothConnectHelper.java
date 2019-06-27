package com.gcml.module_blutooth_devices.base;

import android.text.TextUtils;

import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.utils.BluetoothUtils;

import timber.log.Timber;

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
        Timber.w("bt ---> start connect: address = %s", macAddress);
        connectListener = listener;
        if (TextUtils.isEmpty(macAddress)) {
            if (connectListener != null) {
                connectListener.failed();
            }
            Timber.e("mac address is null");
            return;
        }
        address = macAddress;
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
            Timber.w("bt ---> onConnectResponse: thread = %s", Thread.currentThread().getName());
            if (connectListener != null && !isClear) {
                if (code != 0) {
                    Timber.w("bt ---> onConnectResponse: failed");
                    connectListener.failed();
                }
            } else {
                isConnected = true;
            }
        }
    }

    class MyBleConnectStatusListener extends BleConnectStatusListener {

        @Override
        public void onConnectStatusChanged(String mac, int status) {
            Timber.w("bt ---> onConnectStatusChanged: address = %s, status = %s", mac, status);
            if (connectListener != null && !isClear) {
                if (status == 16) {
                    isConnected = true;
                    Timber.w("bt ---> onConnectStatusChanged: success");
                    connectListener.success(BluetoothUtils.getRemoteDevice(mac));
                } else if (status == 32) {
                    Timber.w("bt ---> onConnectStatusChanged: failed");
                    isConnected = false;
                    connectListener.disConnect(mac);
                }
            }
        }
    }

    public synchronized void disConnect() {
        if (isConnected) {
            isConnected = false;
            if (!TextUtils.isEmpty(address)) {
                BluetoothStore.getClient().disconnect(address);
            }
        }
    }

    public synchronized void clear() {
        isClear = true;
        connectListener = null;
        if (!TextUtils.isEmpty(address) && connectStatusListener != null) {
            BluetoothStore.getClient().unregisterConnectStatusListener(address, connectStatusListener);
        }
        connectStatusListener = null;
        if (address == null) {
            return;
        }
        if (isConnected) {
            isConnected = false;
            BluetoothStore.getClient().disconnect(address);
            Timber.w("bt ---> clear: disconnect address = %s", address);
        }
        //清除队列中缓存
        Timber.w("bt ---> clear: clearRequest address = %s", address);
        BluetoothStore.getClient().clearRequest(address, 0);
    }
}
