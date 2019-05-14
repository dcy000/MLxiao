package com.gcml.module_blutooth_devices.base;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;

import com.gcml.common.api.AppLifecycleCallbacks;
import com.gcml.common.recommend.bean.post.DetectionBean;
import com.gcml.common.recommend.bean.post.DetectionData;
import com.google.auto.service.AutoService;
import com.inuker.bluetooth.library.BluetoothClient;

import java.util.List;

@AutoService(AppLifecycleCallbacks.class)
public class BluetoothStore implements AppLifecycleCallbacks {
    private static BluetoothClient client;
    public MutableLiveData<BindDeviceBean> bindDevice = new MutableLiveData<>();
    public MutableLiveData<DetectionData> detection = new MutableLiveData<>();
    public static BluetoothStore instance;

    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        instance = this;
        client = new BluetoothClient(application);
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }

    public static BluetoothClient getClient() {
        return client;
    }

}
