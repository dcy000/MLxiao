package com.gcml.health.measure.test;

import android.arch.lifecycle.LifecycleOwner;
import android.bluetooth.BluetoothAdapter;
import android.text.TextUtils;
import android.util.Log;

import com.gcml.common.utils.display.ToastUtils;
import com.gcml.lib_sub_bluetooth.BaseBluetooth;
import com.gcml.lib_sub_bluetooth.BluetoothStore;
import com.gcml.lib_sub_bluetooth.BluetoothType;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.UUID;


public class SocketPresenter extends BaseBluetooth {
    public static final UUID SERVER = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public static final UUID WRITE = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    public static final UUID NOTIFY = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    public static final byte[] OPEN = {0x01, 0x01, 0x01};
    public static final byte[] CLOSE = {0x01, 0x01, 0x00};
    private String blueAddress;
    private boolean isOpen;
    private ITestView view;
    private boolean isConnected=false;
    public SocketPresenter(LifecycleOwner owner) {
        super(owner);
        if (owner instanceof ITestView) {
            view = (ITestView) owner;
        }
        start(BluetoothType.BLUETOOTH_TYPE_BLE, null,"AxaSmartSocket");
    }

    @Override
    protected void noneFind() {

    }

    private static final String TAG = "SocketPresenter";

    @Override
    protected void connectSuccessed(String name, final String address) {
        isConnected=true;
        ToastUtils.showShort("设备已连接");
        blueAddress = address;
        //打开notify
        BluetoothStore.getClient().notify(address, SERVER, NOTIFY, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                Log.e(TAG, "onNotify: " + value.length);
            }

            @Override
            public void onResponse(int code) {
                Log.e(TAG, "open notify: " + (code == 0 ? "success" : "fail"));
            }
        });

        //写入数据
        BluetoothStore.getClient().write(address, SERVER, WRITE, verification(), new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                Log.e(TAG, "write data: " + (code == 0 ? "success" : "fail"));

            }
        });


    }

    @Override
    protected void connectFailed() {

    }

    @Override
    protected void disConnected(String address) {
        isConnected=false;
        ToastUtils.showShort("设备已断开");
    }

    private byte[] verification() {
        byte[] password = "123456".getBytes();
        byte[] defaultd = {(byte) 0xaa, (byte) 0xbb, (byte) 0xcc, (byte) 0xdd, (byte) 0xee, (byte) 0xff};
//        byte[] defaultd=ByteUtils.stringToBytes("AABBCCDDEEFF");
        byte[] bytes = new byte[defaultd.length + password.length + 1];
        bytes[0] = (byte) 0x2a;
        System.arraycopy(password, 0, bytes, 1, password.length);
        System.arraycopy(defaultd, 0, bytes, password.length + 1, defaultd.length);
        Log.e(TAG, "verification: " + ByteUtils.byteToString(bytes));
        return bytes;
    }

    public void openOrClose() {
        if (isOpen) {
            isOpen = false;
            //写入数据
            BluetoothStore.getClient().write(blueAddress, SERVER, WRITE, CLOSE, new BleWriteResponse() {
                @Override
                public void onResponse(int code) {
                    if (view != null) {
                        view.closeedState();
                    }
                    Log.e(TAG, "write data: " + (code == 0 ? "success" : "fail"));
                }
            });
        } else {
            isOpen = true;
            //写入数据
            BluetoothStore.getClient().write(blueAddress, SERVER, WRITE, OPEN, new BleWriteResponse() {
                @Override
                public void onResponse(int code) {
                    if (view != null) {
                        view.openedState();
                    }
                    Log.e(TAG, "write data: " + (code == 0 ? "success" : "fail"));
                }
            });
        }


    }
    public boolean isConnected(){
        return isConnected;
    }
    @Override
    public void onDestroy(LifecycleOwner owner) {
        super.onDestroy(owner);
        if (!TextUtils.isEmpty(blueAddress) && BluetoothAdapter.checkBluetoothAddress(blueAddress)) {
            BluetoothStore.getClient().disconnect(blueAddress);
        }
    }
}
