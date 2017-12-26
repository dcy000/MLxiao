package com.medlink.crash;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by afirez on 2017/12/26.
 */

public class CrashHelper {

    private static volatile boolean sIsInstalled = false;

    private static OnCrashListener sOnCrashListener;

    private static Thread.UncaughtExceptionHandler sDefaultUncaughtExceptionHandler;

    public static synchronized void install(OnCrashListener onCrashListener) {
        if (sIsInstalled) {
            return;
        }
        sIsInstalled = true;
        sOnCrashListener = onCrashListener;
        catchMainThreadCrash();
        catchOtherCrash();
    }

    private static void catchOtherCrash() {
        sDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                if (sOnCrashListener != null) {
                    sOnCrashListener.onCrash(thread, throwable);
                }
            }
        });
    }

    private static void catchMainThreadCrash() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for (;;) {
                    try {
                        Looper.loop();
                    } catch (Throwable throwable) {
                        if (throwable instanceof ExitException) {
                            return;
                        }
                        if (sOnCrashListener != null) {
                            sOnCrashListener.onCrash(Looper.getMainLooper().getThread(), throwable);
                        }
                    }
                }
            }
        });
    }

    public static synchronized void uninstall() {
        if (!sIsInstalled) {
            return;
        }
        sIsInstalled = false;
        sOnCrashListener = null;
        Thread.setDefaultUncaughtExceptionHandler(sDefaultUncaughtExceptionHandler);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                throw new ExitException("Exit catching main thread crash!!!");
            }
        });
    }

    public static Thread.UncaughtExceptionHandler getDefaultUncaughtExceptionHandler() {
        return sDefaultUncaughtExceptionHandler;
    }

    public interface OnCrashListener {
        void onCrash(Thread thread, Throwable throwable);
    }

    private static class ExitException extends RuntimeException {
        ExitException(String message) {
            super(message);
        }
    }
}
