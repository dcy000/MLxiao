package com.gzq.lib_bluetooth;

import android.app.Application;

import com.inuker.bluetooth.library.BluetoothClient;

public class BluetoothStore {

    private static volatile BluetoothStore singleton;
    private BluetoothClient client;
    private Application application;

    private BluetoothStore() {
    }

    public static BluetoothStore getInstance() {
        if (singleton == null) {
            synchronized (BluetoothStore.class) {
                singleton = new BluetoothStore();
            }
        }
        return singleton;
    }

    public void init(Application application) {
        this.application = application;
    }

    public BluetoothClient getClient() {
        if (client == null) {
            client = new BluetoothClient(application);
        }
        return client;
    }
}
