package com.gcml.common.app;

public class Singleton {

    private static Singleton sInstance;

    private Object mObject;

    private Singleton(Object object) {
        mObject = object;
    }

    public static Singleton getInstance(Object object) {
        if (sInstance == null) {
            synchronized (Singleton.class) {
                if (sInstance == null) {
                    sInstance = new Singleton(object);
                }
            }
        }
        return sInstance;
    }
}
