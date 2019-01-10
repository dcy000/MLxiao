package com.gcml.module_blutooth_devices.bluetooth;


import android.bluetooth.BluetoothDevice;

public interface IBluetoothView {
    void updateData(String... datas);
    void updateState(String state);
    void discoveryNewDevice(BluetoothDevice device);
}
