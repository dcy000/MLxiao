package com.gzq.lib_bluetooth.utils;

import android.bluetooth.BluetoothDevice;
import com.gzq.lib_bluetooth.BluetoothStore;
import com.gzq.lib_bluetooth.DeviceBean;

import java.lang.reflect.Method;
import java.util.List;

import timber.log.Timber;

public class BluetoothUtils {
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
                Timber.e(e.getMessage());
            }
        }
    }

    /**
     * 清除蓝牙缓存
     *
     * @param device
     */
    public static void clearBluetoothCache(DeviceBean device) {
        if (device != null) {
            BluetoothStore.getClient().refreshCache(device.getAddress());
        }
    }
}
