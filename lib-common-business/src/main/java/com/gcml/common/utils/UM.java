package com.gcml.common.utils;

import android.app.Application;
import android.support.annotation.StringRes;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;


public class UM {
    private static Application mApplication;

    private UM() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(Application application) {
        if (mApplication == null) {
            mApplication = application;
        }
    }

    /**
     * Return the context of Application object.
     *
     * @return the context of Application object
     */
    public static Application getApp() {
        if (mApplication != null) {
            return mApplication;
        }
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object at = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApp").invoke(at);
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

    /**
     * 为了解决在fragment中使用{@link android.support.v4.app.Fragment#getString(int)}
     * 偶尔会报java.lang.IllegalStateException-->Fragment xxxxx{xxx} not attached to a context的错误
     *
     * @param id 字符串资源id
     * @return
     */
    public static String getString(@StringRes int id) {
        return getApp().getResources().getString(id);
    }

    public static String getString(@StringRes int id, Object... formatArms) {
        return  String.format(getApp().getString(id),formatArms);
    }
}
