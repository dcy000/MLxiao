package com.ml.edu.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by afirez on 18-1-31.
 */
@Singleton
public class PreferencesHelper {

    private static volatile PreferencesHelper sInstance;

    public static PreferencesHelper getInstance() {
        if (sInstance == null) {
            synchronized (PreferencesHelper.class) {
                if (sInstance == null) {
                    sInstance = new PreferencesHelper();
                }
            }
        }
        return sInstance;
    }

    @Inject
    public PreferencesHelper(){

    }

    public void put(
            Context context,
            String name,
            String key,
            long value) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        preferences.edit().putLong(key, value).apply();
    }

    public long get(
            Context context,
            String name,
            String key) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return preferences.getLong(key, 0);
    }

    public void remove(
            Context context,
            String name,
            String key) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        preferences.edit().remove(key).apply();
    }

    public void clear(
            Context context,
            String name) {
        SharedPreferences preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
    }
}
