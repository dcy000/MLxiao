package com.ml.call;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ml.call.common.MainLooperMonitor;
import com.ml.call.utils.T;

import timber.log.Timber;

/**
 * Created by afirez on 2018/5/31.
 */

public enum CallApp {

    @SuppressLint("StaticFieldLeak")
    INSTANCE;

    private Application mApp;

    public Application getApp() {
        return mApp;
    }

    public void attachBaseContext(Application app, Context base) {
        MainLooperMonitor.getInstance().install();
        mApp = app;
    }

    public void onCreate(Application app) {

        T.init(app);
        CallInitHelper.getInstance().init(app, true);
        initTimber();
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, @NonNull String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            FakeCrashLibrary.log(priority, tag, message);

            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t);
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t);
                }
            }
        }
    }

    /** Not a real crash reporting library! */
    public static final class FakeCrashLibrary {
        public static void log(int priority, String tag, String message) {
            // TODO add log entry to circular buffer.
        }

        public static void logWarning(Throwable t) {
            // TODO report non-fatal warning.
        }

        public static void logError(Throwable t) {
            // TODO report non-fatal error.
        }

        private FakeCrashLibrary() {
            throw new AssertionError("No instances.");
        }
    }

    public void onTerminate(Application app) {

    }
}
