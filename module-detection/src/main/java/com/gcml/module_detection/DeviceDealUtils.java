package com.gcml.module_detection;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;

import com.gcml.module_blutooth_devices.base.DeviceBrand;

import java.util.Map;

public class DeviceDealUtils {
    public static String parseBluetoothName(BluetoothDevice device) {
        String result = null;
        for (Map.Entry<String, String> entry : DeviceBrand.All_DEVICES.entrySet()) {
            if (TextUtils.isEmpty(device.getName())) break;
            if (device.getName().contains(entry.getKey())) {
                return device.getName() + "(" + entry.getValue() + ")";
            } else {
                result = device.getName();
            }
        }
        return result;
    }
}
