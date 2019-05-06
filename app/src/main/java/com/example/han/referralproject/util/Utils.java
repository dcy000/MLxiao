package com.example.han.referralproject.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import com.example.han.referralproject.application.MyApplication;

public class Utils {
    @SuppressLint("MissingPermission")
    public static String getDeviceId(){
        return Settings.System.getString(MyApplication.getInstance().getContentResolver(), Settings.System.ANDROID_ID);
    }

    public static String getLocalVersionName(Context context) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }
}
