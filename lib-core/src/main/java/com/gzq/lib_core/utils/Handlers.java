package com.gzq.lib_core.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

/**
 * Created by lenovo on 2017/10/11.
 */

public class Handlers {

    public static void runOnUiThread(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            ui().post(runnable);
        }
    }

    public static Handler ui() {
        if (sUiHandler == null) {
            synchronized (Handlers.class) {
                if (sUiHandler == null) {
                    sUiHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return sUiHandler;
    }

    public static Handler bg() {
        if (sBgHandler == null) {
            synchronized (Handlers.class) {
                HandlerThread sBgThread = new HandlerThread(
                        "Handler-Work", Process.THREAD_PRIORITY_BACKGROUND);
                sBgThread.start();
                sBgHandler = new Handler(sBgThread.getLooper());
            }
        }
        return sBgHandler;
    }

    private static volatile Handler sBgHandler;

    private static volatile Handler sUiHandler;

    public static void clear() {
        if (sBgHandler != null) {
            sBgHandler.removeCallbacksAndMessages(null);
        }
        if (sUiHandler != null) {
            sUiHandler.removeCallbacksAndMessages(null);
        }
    }
}
