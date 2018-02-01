package com.ml.edu.common;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by afirez on 18-1-31.
 */
@Singleton
public class PreferencesHelper {
    @Inject
    public PreferencesHelper (){

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
}
