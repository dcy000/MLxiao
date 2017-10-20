/**
 * BCCache.java
 *
 * Created by xuanzhui on 2015/7/27.
 * Copyright (c) 2015 BeeCloud. All rights reserved.
*/
package cn.beecloud;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 配置缓存类
 * 单例模式
 */
public class BCCache {

    private static BCCache instance;

    private static final String separator = "#@#";

    /**
     * BeeCloud控制台注册的App Id
     */
    public String appId;

    /**
     * BeeCloud控制台注册的App Secret(如果是正式版本)，或者Test Secret(如果是测试模式)
     */
    public String secret;

    /**
     * 是否为测试模式
     */
    public boolean isTestMode;

    /**
     * 微信App Id
     */
    public String wxAppId;

    /**
     * PayPal
     */
    public String paypalClientID;
    public String paypalSecret;
    public BCPay.PAYPAL_PAY_TYPE paypalPayType;
    public Boolean retrieveShippingAddresses;
    public static final String BC_PAYPAL_SHARED_PREFERENCE_NAME = "BC_CACHE_PAYPAL_SHARED_PREFERENCE";
    public static final String BC_PAYPAL_UNSYNCED_STR_CACHE = "BC_CACHE_PAYPAL_UNSYNCED";

    /**
     * 网络请求timeout时间
     * 以毫秒为单位
     */
    public Integer connectTimeout;

    /**
     * 暂存每次支付结束后的返回的订单唯一标识
     */
    public String billID;

    /**
     * 线程池
     */
    public static ExecutorService executorService = Executors.newCachedThreadPool();

    private BCCache() {
    }

    /**
     * 唯一获取实例的方法
     * @return  BCCache实例
     */
    public synchronized static BCCache getInstance() {
        if (instance == null) {
            instance = new BCCache();

            instance.appId = null;
            instance.secret = null;
            instance.wxAppId = null;

            instance.connectTimeout = 10000;
        }

        return instance;
    }

    /**
     * to retrieve paypal not synced records
     */
    public List<String> getUnSyncedPayPalRecords(Context context) {
        if (context == null) {
            Log.e("BCCache", "param context NPE");
            return null;
        }

        String cacheStr =
                context.getSharedPreferences(BC_PAYPAL_SHARED_PREFERENCE_NAME, 0)
                        .getString(BC_PAYPAL_UNSYNCED_STR_CACHE, null);

        if (cacheStr != null)
            return new ArrayList<String>(Arrays.asList(cacheStr.split(separator)));
        else
            return null;
    }

    /**
     * clear un-synced cache
     */
    public void clearUnSyncedPayPalRecords(Context context) {
        if (context == null) {
            Log.e("BCCache", "param context NPE");
            return;
        }

        final SharedPreferences prefs =
                context.getSharedPreferences(BC_PAYPAL_SHARED_PREFERENCE_NAME, 0);
        SharedPreferences.Editor spEditor = prefs.edit();
        spEditor.clear();
        spEditor.apply();
    }

    /**
     * remove synced record
     */
    public void removeSyncedPalPalRecords(Context context, List<String> rmRecords) {
        if (context == null) {
            Log.e("BCCache", "param context NPE");
            return;
        }

        final SharedPreferences prefs =
                context.getSharedPreferences(BC_PAYPAL_SHARED_PREFERENCE_NAME, 0);

        String records = prefs.getString(BC_PAYPAL_UNSYNCED_STR_CACHE, null);

        if (records == null)
            return;

        List<String> oldRecords = Arrays.asList(records.split(separator));

        List<String> leftRecords = new ArrayList<String>();
        if (oldRecords.size() != 0)
            leftRecords.addAll(oldRecords);

        leftRecords.removeAll(rmRecords);

        if (leftRecords.size() == 0) {
            clearUnSyncedPayPalRecords(context);
        } else {
            SharedPreferences.Editor spEditor = prefs.edit();
            spEditor.putString(BC_PAYPAL_UNSYNCED_STR_CACHE, joinStrings(leftRecords));
            spEditor.apply();
        }
    }

    /**
     * to store paypal not synced records
     */
    public void storeUnSyncedPayPalRecords(Context context, String newRecord) {
        if (context == null) {
            Log.e("BCCache", "param context NPE");
            return;
        }

        final SharedPreferences prefs =
                context.getSharedPreferences(BC_PAYPAL_SHARED_PREFERENCE_NAME, 0);

        String cachedStr = prefs.getString(BC_PAYPAL_UNSYNCED_STR_CACHE, null);

        SharedPreferences.Editor spEditor = prefs.edit();

        if (cachedStr == null) {
            spEditor.putString(BC_PAYPAL_UNSYNCED_STR_CACHE, newRecord);
        } else {
            List<String> totalRec = new ArrayList<String>(Arrays.asList(cachedStr.split(separator)));
            totalRec.add(newRecord);

            spEditor.putString(BC_PAYPAL_UNSYNCED_STR_CACHE, joinStrings(totalRec));
        }

        spEditor.apply();
    }

    private String joinStrings(List<String> strings){
        if (strings == null || strings.size() == 0)
            return null;

        StringBuilder joined = new StringBuilder();
        int cnt = 0;
        for (String s : strings) {
            joined.append(s);

            if (++cnt < strings.size())
                joined.append(separator);
        }

        return joined.toString();
    }

}
