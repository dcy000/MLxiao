package com.gcml.module_blutooth_devices.base;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.List;

public class BluetoothUnpairUtils {
    /**
     * 解除经典设备的绑定
     */
    public static void unpairDevice() {
        List<BluetoothDevice> devices = com.inuker.bluetooth.library.utils.BluetoothUtils.getBondedBluetoothClassicDevices();
        for (BluetoothDevice device : devices) {
            try {
                Method m = device.getClass()
                        .getMethod("removeBond", (Class[]) null);
                m.invoke(device, (Object[]) null);
            } catch (Exception e) {
                Log.e("BluetoothUnpairUtils", e.getMessage());
            }
        }
    }

    /**
     * 清除蓝牙缓存
     *
     * @param deviceAddress
     */
    public static void clearBluetoothCache(String deviceAddress) {
        if (!TextUtils.isEmpty(deviceAddress) && BluetoothAdapter.checkBluetoothAddress(deviceAddress)) {
            BluetoothStore.getClient().refreshCache(deviceAddress);
        }
    }
}
