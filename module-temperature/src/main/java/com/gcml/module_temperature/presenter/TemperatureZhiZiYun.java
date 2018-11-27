package com.gcml.module_temperature.presenter;

import android.text.TextUtils;

import com.gcml.module_temperature.R;
import com.gzq.lib_bluetooth.BaseBluetooth;
import com.gzq.lib_bluetooth.BluetoothConstants;
import com.gzq.lib_bluetooth.BluetoothStore;
import com.gzq.lib_bluetooth.BluetoothType;
import com.gzq.lib_bluetooth.DeviceBean;
import com.gzq.lib_bluetooth.DeviceType;
import com.gzq.lib_bluetooth.IBluetoothView;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.IView;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_core.utils.WeakHandler;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;

import java.util.UUID;

/**
 * 小机器人配置的耳温枪
 * 测试机器如下
 * name:FSRKB-EWQ01
 * mac:94:E3:6D:54:1D:2D
 */
public class TemperatureZhiZiYun extends BaseBluetooth {

    private static final UUID targetServiceUUid = UUID.fromString("00001910-0000-1000-8000-00805f9b34fb");
    private static final UUID targetCharacteristicUUid = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    private static final String TARGET_BLUETOOTH_NAME = "FSRKB-EWQ01";

    public TemperatureZhiZiYun(IBluetoothView owner) {
        super(owner);

        String kv = (String) KVUtils.get(BluetoothConstants.KEY_TEMPERATURE, TARGET_BLUETOOTH_NAME);
        if (TextUtils.isEmpty(kv)) {
            start(BluetoothType.BLUETOOTH_TYPE_BLE, null, "");
        } else {
            DeviceBean deviceBean = Box.getGson().fromJson(kv, DeviceBean.class);
            start(BluetoothType.BLUETOOTH_TYPE_BLE, deviceBean.getAddress(), deviceBean.getName());
        }
    }

    @Override
    protected void noneFind() {
        baseView.updateState(Box.getString(R.string.bluetooth_undiscoveried));
    }

    @Override
    protected void connectSuccessed(String name, String address) {
        baseView.updateState(Box.getString(R.string.bluetooth_connected));
        DeviceBean deviceBean = new DeviceBean(name, address, DeviceType.DEVICE_TEMPERATURE);
        String json = Box.getGson().toJson(deviceBean);
        KVUtils.put(BluetoothConstants.KEY_TEMPERATURE, json);

        BluetoothStore.getClient().notify(address, targetServiceUUid, targetCharacteristicUUid, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID uuid, UUID uuid1, byte[] bytes) {
                if (bytes.length > 6) {
                    int data = bytes[6] & 0xff;
                    if (data < 44) {
                        ToastUtils.showShort("测量温度不正常");
                        return;
                    }
                    double v = (data - 44.0) % 10;
                    double result = (30.0 + (data - 44) / 10 + v / 10);
                    baseView.updateData(result + "");
                }
            }

            @Override
            public void onResponse(int i) {

            }
        });
    }

    @Override
    protected void connectFailed() {
        baseView.updateState(Box.getString(R.string.bluetooth_failed));
    }

    @Override
    protected void disConnected(final String address) {
        baseView.updateState(Box.getString(R.string.bluetooth_disconnected));
        //2秒之后进行重连
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(address)) {
                    connect(address);
                } else {
                    start(BluetoothType.BLUETOOTH_TYPE_BLE, null, TARGET_BLUETOOTH_NAME);
                }
            }
        }, 2000);
    }
}
