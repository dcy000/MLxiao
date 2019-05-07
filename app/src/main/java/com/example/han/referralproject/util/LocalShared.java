package com.example.han.referralproject.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.constant.ConstantData;
import com.gcml.common.data.UserSpHelper;
import com.iflytek.settting.IatSettings;

import java.util.ArrayList;
import java.util.Random;

public class LocalShared {
    private final String SharedName = "ScopeMediaPrefsFile";
    private static LocalShared mInstance;
    private SharedPreferences mShared;
    private static final String USER_NAME = "user_name";
    public static final String IS_FIRST_IN = "isFirstIn";

    private Context context;

    private LocalShared(Context context) {
        this.context = context.getApplicationContext();
        mShared = context.getSharedPreferences(SharedName, Context.MODE_PRIVATE);
    }

    public static LocalShared getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LocalShared(context);
        }
        return mInstance;
    }

    public boolean getIsFirstIn() {
        return mShared.getBoolean(IS_FIRST_IN, true);
    }

    public void setIsFirstIn(boolean isFirstIn) {
        mShared.edit().putBoolean(IS_FIRST_IN, isFirstIn).apply();
    }

    public String getUserName() {
        return mShared.getString(USER_NAME, "");
    }

}
