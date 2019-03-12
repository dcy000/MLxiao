package com.gcml.lib_sub_bluetooth;

import android.app.Application;

import com.inuker.bluetooth.library.BluetoothClient;

public class BluetoothStore {
    private static BluetoothClient client;

    public static void init(Application application) {
        if (client == null) {
            client = new BluetoothClient(application);
        }
    }

    public static BluetoothClient getClient() {
        if (client == null) {
            throw new NullPointerException("Please init BluetoothStore in Application");
        }
        return client;
    }
}