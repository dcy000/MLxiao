package com.gcml.module_blutooth_devices.dialog;

import android.bluetooth.BluetoothDevice;

public interface ChooseBluetoothDevice {
    void choosed(BluetoothDevice device);
    void autoConnect();
    void cancelSearch();
}
