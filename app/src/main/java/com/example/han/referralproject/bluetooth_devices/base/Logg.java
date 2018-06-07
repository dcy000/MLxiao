package com.example.han.referralproject.bluetooth_devices.base;

import android.util.Log;

public class Logg {
    private static final boolean debug=true;
    public static void e(Class clazz,String message){
        if (debug)
            android.util.Log.e(clazz.getSimpleName(), message );
    }
    public static void d(Class clazz,String message){
        if (debug)
            Log.e(clazz.getSimpleName(), message );
    }
}
