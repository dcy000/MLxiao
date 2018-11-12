package com.gzq.lib_bluetooth;

import android.bluetooth.BluetoothDevice;

public interface SearchListener {
    void onSearching(boolean isOn);
    void obtainDevice(BluetoothDevice device);
    void noneFind();
}
