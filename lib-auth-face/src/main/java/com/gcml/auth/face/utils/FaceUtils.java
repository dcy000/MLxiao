package com.gcml.auth.face.utils;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Function;

public class FaceUtils {
    public static String produceFaceId() {
        Date date = new Date();
        SimpleDateFormat simple = new SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault());
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            str.append(random.nextInt(10));
        }
        return simple.format(date) + str;
    }

    public static Observable<Integer> rxWifiLevel(Context context, int numsLevel) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("1");
                BroadcastReceiver receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        emitter.onNext("1");
                    }
                };
                IntentFilter filter = new IntentFilter(WifiManager.RSSI_CHANGED_ACTION);
                context.getApplicationContext().registerReceiver(receiver, filter);
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        context.getApplicationContext().unregisterReceiver(receiver);
                    }
                });
            }
        }).map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) throws Exception {
                @SuppressLint("WifiManagerPotentialLeak")
                WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wm == null) {
                    return 0;
                }
                @SuppressLint("MissingPermission")
                WifiInfo wifiInfo = wm.getConnectionInfo();
                String bssid = wifiInfo.getBSSID();
                if (bssid == null) {
                    return 0;
                }
                return WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numsLevel);
            }
        }).distinct();
    }
}
