package com.gzq.administrator.lib_common;

import android.content.Context;
import android.support.annotation.MainThread;

import java.lang.reflect.Method;

public class ContextFactory {
    private static Context CONTEXT_INSTANCE;

    @MainThread
    public static Context getContext() {
        if (CONTEXT_INSTANCE == null) {
            synchronized (ContextFactory.class) {
                if (CONTEXT_INSTANCE == null) {
                    try {
                        Class<?> ActivityThread = Class.forName("android.app.ActivityThread");
                        Method method_currentActivityThread = ActivityThread.getMethod("currentActivityThread");
                        Object currentActivityThread = method_currentActivityThread.invoke(ActivityThread);//获取currentActivityThread 对象
                        Method method_getApplication = currentActivityThread.getClass().getMethod("getApplication");
                        CONTEXT_INSTANCE = (Context) method_getApplication.invoke(currentActivityThread);//获取 Context对象
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return CONTEXT_INSTANCE;
    }
}
