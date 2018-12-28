package com.gcml.lib_sub_bluetooth;

import android.bluetooth.BluetoothDevice;

public interface SearchListener {
    void onSearching(boolean isOn);
    void onNewDeviceFinded(BluetoothDevice newDevice);
    void obtainDevice(BluetoothDevice device);
    void noneFind();
}
