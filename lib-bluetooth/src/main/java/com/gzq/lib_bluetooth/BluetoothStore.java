package com.gzq.lib_bluetooth;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;

import com.gzq.lib_core.base.delegate.AppLifecycle;
import com.gzq.lib_core.base.ui.IEvents;
import com.inuker.bluetooth.library.BluetoothClient;

public class BluetoothStore implements AppLifecycle {
    private static BluetoothClient client;
    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        client=new BluetoothClient(application);
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

    }

    @Override
    public IEvents provideEvents() {
        return null;
    }

    public static BluetoothClient getClient(){
        return client;
    }
}
