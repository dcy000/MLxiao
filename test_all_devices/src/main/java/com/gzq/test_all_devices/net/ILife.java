package com.gzq.test_all_devices.net;

import android.arch.lifecycle.LifecycleOwner;
import android.util.Log;

import com.gzq.test_all_devices.Life;

/**
 * Created by gzq on 2018/8/19.
 */

public class ILife implements Life{
    private static final String TAG = "ILife";
    @Override
    public void onCreate(LifecycleOwner owner) {
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onStart(LifecycleOwner owner) {
        Log.d(TAG, "onStart: ");
    }
}
