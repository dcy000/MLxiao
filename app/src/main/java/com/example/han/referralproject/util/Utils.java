package com.example.han.referralproject.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

import com.example.han.referralproject.application.MyApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.TELEPHONY_SERVICE;

public class Utils {
    @SuppressLint("MissingPermission")
    public static String getDeviceId(){
//        WifiManager wm = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        String WLANMAC = wm.getConnectionInfo().getMacAddress();

//        TelephonyManager TelephonyMgr = (TelephonyManager) MyApplication.getInstance().getSystemService(TELEPHONY_SERVICE);
//        return TelephonyMgr.getDeviceId();

        return Settings.System.getString(MyApplication.getInstance().getContentResolver(), Settings.System.ANDROID_ID);
    }

    @SuppressLint("MissingPermission")
    public static String getMacAddress(){
        WifiManager wm = (WifiManager)MyApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wm.getConnectionInfo().getMacAddress();
    }

    /*
    * 将时间戳转换为时间
    */
    public static String stampToDate(long s){
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Long time=new Long(s);
        String d = format.format(time);
        return d;
    }

    /**
     * 调节屏幕透明度
     * @param context
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
}
