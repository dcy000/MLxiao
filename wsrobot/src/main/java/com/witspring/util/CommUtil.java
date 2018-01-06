package com.witspring.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseArray;

import com.witspring.mlrobot.BuildConfig;
import com.witspring.unitbody.ChooseMemberActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Map;

/**
 * 通用工具类
 * @author Goven 2014年9月29日 上午11:33:24
 * @email gxl3999@gmail.com
 */
public class CommUtil {

	/**
	 * 打印默认info级别的日志
	 * @param tag 标签
	 * @param info 信息
	 */
	public static void logI(String tag, String info) {
		if (BuildConfig.LOGGING) {
            Log.i(tag, info);
        }
	}

	/**
	 * 打印debug级别的日志
	 * @param tag 标签
	 * @param info 信息
	 */
	public static void logD(String tag, String info) {
		if (BuildConfig.LOGGING)
			Log.d(tag, info);
	}

	/**
	 * 打印error级别的日志
	 * @param tag 标签
	 * @param info 信息
	 */
	public static void logE(String tag, String info) {
		if (BuildConfig.LOGGING)
			Log.e(tag, info);
	}

    public static boolean isNetworkOk() {
		ConnectivityManager connectivityManager = (ConnectivityManager) ChooseMemberActivity.app.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();
        return activeInfo != null && activeInfo.isConnectedOrConnecting();
	}

    public static boolean canUseSim(Context context) {
        try {
            TelephonyManager mgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            return TelephonyManager.SIM_STATE_READY == mgr.getSimState();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取打开网络设置的Intent
     */
    public static Intent getNetworkSettingIntent() {
        Intent intent;
        //判断手机系统的版本  即API大于13 就是5.0或以上版本
        if(Build.VERSION.SDK_INT>13){
            intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        }else{
            if (Build.VERSION.SDK_INT > 10) {
                intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
            } else {
                intent = new Intent();
                ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
                intent.setComponent(component);
                intent.setAction("android.intent.action.VIEW");
            }
        }
        return intent;
    }

    public static boolean notEmpty(Object[] array) {
        return array != null && array.length > 0;
    }

    public static boolean notEmpty(SparseArray array) {
        return array != null && array.size() > 0;
    }

    public static boolean notEmpty(Collection collection) {
        return collection != null && collection.size() > 0;
    }

    public static boolean notEmpty(Map map) {
        return map != null && map.size() > 0;
    }

    public static boolean notEmpty(JSONArray jArr) {
        return jArr != null && jArr.length() > 0;
    }

    public static boolean notEmpty(JSONObject jObj) {
        return jObj != null && jObj.length() > 0;
    }

    public static boolean isEmulator(Context context) {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("vbox")
                || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.SERIAL.equalsIgnoreCase("unknown")
                || Build.SERIAL.equalsIgnoreCase("android")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT)
                || ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                .getNetworkOperatorName().toLowerCase().equals("android");
    }

}