package com.gcml.module_blutooth_devices.bluetooth;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.gcml.common.api.AppLifecycleCallbacks;
import com.google.auto.service.AutoService;
import com.inuker.bluetooth.library.BluetoothClient;

@AutoService(AppLifecycleCallbacks.class)
public class BluetoothStore implements AppLifecycleCallbacks {
    private static BluetoothClient client;
    public static MutableLiveData<BindDeviceBean> bindDevice = new MutableLiveData<>();

    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        client=new BluetoothClient(application);
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }
    public static BluetoothClient getClient(){
        return client;
    }

}
