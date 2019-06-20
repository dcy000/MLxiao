package com.gcml.module_health_profile.utils;

import android.bluetooth.BluetoothDevice;
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
}
