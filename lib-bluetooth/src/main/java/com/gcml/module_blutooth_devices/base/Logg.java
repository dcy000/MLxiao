package com.gcml.module_blutooth_devices.base;

import android.util.Log;

public class Logg {
    private static boolean debug=true;
    public static void enableDebug(boolean enable){
        debug=enable;
    }
    public static void e(Class clazz,String message){
        if (debug)
            Log.e(clazz.getSimpleName(), message );
    }
    public static void d(Class clazz,String message){
        if (debug)
            Log.e(clazz.getSimpleName(), message );
    }
}
