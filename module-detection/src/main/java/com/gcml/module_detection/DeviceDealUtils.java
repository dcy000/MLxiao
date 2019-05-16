package com.gcml.module_detection;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;

import com.gcml.module_blutooth_devices.base.DeviceBrand;

import java.util.Map;

public class DeviceDealUtils {
    public synchronized static String parseBluetoothName(BluetoothDevice device) {
        String result = null;
        for (Map.Entry<String, String> entry : DeviceBrand.All_DEVICES.entrySet()) {
            if (device == null || TextUtils.isEmpty(device.getName())) break;
            if (device.getName().contains(entry.getKey())) {
                result = device.getName() + "(" + entry.getValue() + ")";
                break;
            } else {
                result = device.getName();
            }
        }
        return result;
    }

    public synchronized static boolean isSelfDevice(BluetoothDevice device) {
        for (Map.Entry<String, String> entry : DeviceBrand.All_DEVICES.entrySet()) {
            if (device == null || TextUtils.isEmpty(device.getName())) break;
            if (device.getName().contains(entry.getKey())) {
                return true;
            }
        }
        return false;
    }
}
