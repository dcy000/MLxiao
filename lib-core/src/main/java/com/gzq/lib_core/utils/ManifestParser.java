package com.gzq.lib_core.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * 用于解析 AndroidManifest 中的 Meta 属性
 */
public final class ManifestParser<T> {
    private static final String TAG = "ManifestParser";

    private final Context context;
    private final String mMetaDataValue;

    public ManifestParser(Context context, String metaDataValue) {
        this.context = context;
        mMetaDataValue = metaDataValue;
    }

    public List<T> parse() {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Timber.tag(TAG).d("Loading metaDataValue");
        }
        List<T> modules = new ArrayList<>();
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData == null) {
                if (Log.isLoggable(TAG, Log.DEBUG)) {
                    Timber.tag(TAG).d("Got null app info metadata");
                }
                return modules;
            }
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Timber.tag(TAG).v("Got app info metadata: %s", appInfo.metaData);
            }
            for (String key : appInfo.metaData.keySet()) {
                if (!TextUtils.isEmpty(mMetaDataValue) && mMetaDataValue.equals(appInfo.metaData.get(key))) {
                    modules.add(parseModule(key));
                    if (Log.isLoggable(TAG, Log.DEBUG)) {
                        Timber.d("Loaded : %s", key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Unable to find metadata to parse " + mMetaDataValue, e);
        }
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Timber.d("Finished loading %s", mMetaDataValue);
        }

        return modules;
    }


    private T parseModule(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to find implementation " + className, e);
        }

        Object obj = null;
        try {
            obj = clazz.getDeclaredConstructor().newInstance();
            // These can't be combined until API minimum is 19.
        } catch (InstantiationException e) {
            throwInstantiateException(clazz, e);
        } catch (IllegalAccessException e) {
            throwInstantiateException(clazz, e);
        } catch (NoSuchMethodException e) {
            throwInstantiateException(clazz, e);
        } catch (InvocationTargetException e) {
            throwInstantiateException(clazz, e);
        }

        try {
            return (T) obj;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Unexpected implementation: " + className + " for " + mMetaDataValue);
        }
    }

    private static void throwInstantiateException(Class<?> clazz, Exception e) {
        throw new RuntimeException("Unable to instantiate implementation: " + clazz, e);
    }
}
