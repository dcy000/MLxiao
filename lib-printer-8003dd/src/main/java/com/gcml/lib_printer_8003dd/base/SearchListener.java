package com.gcml.lib_printer_8003dd.base;

import android.bluetooth.BluetoothDevice;

public interface SearchListener {
    void onSearching(boolean isOn);
    void onNewDeviceFinded(BluetoothDevice newDevice);
    void obtainDevice(BluetoothDevice device);
    void noneFind();
}
