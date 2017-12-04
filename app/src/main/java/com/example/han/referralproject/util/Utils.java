package com.example.han.referralproject.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

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
        TelephonyManager TelephonyMgr = (TelephonyManager) MyApplication.getInstance().getSystemService(TELEPHONY_SERVICE);
        return TelephonyMgr.getDeviceId();
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
}
