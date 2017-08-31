package com.example.han.referralproject.util;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.example.han.referralproject.application.MyApplication;

import static android.content.Context.TELEPHONY_SERVICE;

public class Utils {
    public static String getDeviceId(){
//        WifiManager wm = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        String WLANMAC = wm.getConnectionInfo().getMacAddress();
        TelephonyManager TelephonyMgr = (TelephonyManager) MyApplication.getInstance().getSystemService(TELEPHONY_SERVICE);
        return TelephonyMgr.getDeviceId();
    }
}
