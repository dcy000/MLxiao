package com.gcml.module_blutooth_devices.base;

import android.bluetooth.BluetoothDevice;

public interface SearchListener {
    void onSearching(boolean isOn);
    void onNewDeviceFinded(BluetoothDevice newDevice);
    void obtainDevice(BluetoothDevice device);
    void noneFind();
}
