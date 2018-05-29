package com.example.han.referralproject.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.view.WindowManager;

import com.example.han.referralproject.application.MyApplication;

import java.text.SimpleDateFormat;

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

    /*
    * 将时间戳转换为时间
    */
    public static String stampToDate(long s){
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Long time=new Long(s);
        String d = format.format(time);
        return d;
    }


    public static String stampToYMD(long s){
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd");
        Long time=new Long(s);
        String d = format.format(time);
        return d;
    }

    /**
     * 时间格式转换
     * @param s
     * @return
     */
    public static String stampToDate2(long s){
        SimpleDateFormat format =  new SimpleDateFormat("yyyy.MM.dd HH:mm");
        Long time=new Long(s);
        String d = format.format(time);
        return d;
    }
    /**
     * 时间格式转换
     * @param s
     * @return
     */
    public static String stampToDate3(long s){
        SimpleDateFormat format =  new SimpleDateFormat("MMddHH");
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

    public static String getChineseNumber(int number) {
        String[] str = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String ss[] = new String[]{"", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿"};
        String s = String.valueOf(number);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            String index = String.valueOf(s.charAt(i));
            sb = sb.append(str[Integer.parseInt(index)]);
        }
        String sss = String.valueOf(sb);
        int i = 0;
        for (int j = sss.length(); j > 0; j--) {
            sb = sb.insert(j, ss[i++]);
        }
        return sb.toString();
    }

    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }
}
