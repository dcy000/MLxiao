package com.gcml.module_blutooth_devices.base;

import android.content.Context;

import com.gcml.module_blutooth_devices.utils.ToastTool;
import com.inuker.bluetooth.library.BluetoothClient;

/**
 * Created by dingjikerbo on 2016/8/27.
 */
public class BluetoothClientManager {

    private static BluetoothClient mClient;
    private static Context baseContext;

    public static void init(Context context) {
        baseContext = context.getApplicationContext();
        ToastTool.init(context);
    }

    public static BluetoothClient getClient() {
        if (baseContext == null) {
            new UnsupportedOperationException("未初始化");
            return null;
        }

        if (mClient == null) {
            synchronized (BluetoothClientManager.class) {
                if (mClient == null) {
                    mClient = new BluetoothClient(baseContext);
                }
            }
        }
        return mClient;
    }
}
