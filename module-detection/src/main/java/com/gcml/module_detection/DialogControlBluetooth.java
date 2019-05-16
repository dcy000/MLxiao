package com.gcml.module_detection;

import android.bluetooth.BluetoothDevice;

public interface DialogControlBluetooth {
    void search();
    void connect(BluetoothDevice device);
    void dialogDismissed();
}
