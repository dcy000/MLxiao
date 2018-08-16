package com.gcml.lib_utils;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;

import timber.log.Timber;

public class UtilsManager {
    private static Application mApplication;

    private UtilsManager() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(Application application) {
        if (mApplication == null) {
            mApplication = application;
        }
        //初始化日志库
        // lib-common-business 統一初始化 Timber
//        initTimber();
    }

    /**
     * Return the context of Application object.
     *
     * @return the context of Application object
     */
    public static Application getApplication() {
        if (mApplication != null) return mApplication;
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object at = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(at);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            init((Application) app);
            return mApplication;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new NullPointerException("u should init first");
    }


    private static void initTimber() {
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
}
