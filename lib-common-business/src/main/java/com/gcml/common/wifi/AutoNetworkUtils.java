package com.gcml.common.wifi;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Cancellable;

public class AutoNetworkUtils {

    public static boolean showWifiDisconnectedPage = true;

    /**
     * 1. network_type_wifi: WIFI connected
     * 2. network_type_mobile: mobile connected
     * 3. network_none: disconnected
     *
     * @param context context
     * @return  rxNetworkConnectionState flows network_type_wifi， network_type_mobile or network_unknown
     */
    public static Observable<String> rxNetworkConnectionState(Context context) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Context applicationContext = context.getApplicationContext();
                BroadcastReceiver receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String action = intent.getAction();
                        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
                            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                            switch (state) {
                                case WifiManager.WIFI_STATE_DISABLED:
                                    break;
                                case WifiManager.WIFI_STATE_ENABLED:
                                    break;
                            }
                        }

                        NetworkInfo networkInfo = null;
                        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                            ConnectivityManager cm = (ConnectivityManager) applicationContext.
                                    getSystemService(Context.CONNECTIVITY_SERVICE);
                            networkInfo = cm == null ? null : cm.getActiveNetworkInfo();
                        }
                        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
                            networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                        }
                        String connected = "network_none";
                        if (networkInfo != null
                                && networkInfo.isAvailable()
                                && NetworkInfo.State.CONNECTED == networkInfo.getState()) {
                            int type = networkInfo.getType();
                            connected =
                                    type == ConnectivityManager.TYPE_WIFI
                                            ? "network_type_wifi" : type == ConnectivityManager.TYPE_MOBILE
                                            ? "network_type_mobile" : "network_none";
                            emitter.onNext(connected);
                        } else {
                            emitter.onNext(connected);
                        }

                    }
                };
                IntentFilter filter = new IntentFilter();
                filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
                filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
                filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                applicationContext.registerReceiver(receiver, filter);
                emitter.setCancellable(new Cancellable() {
                    @Override
                    public void cancel() throws Exception {
                        applicationContext.unregisterReceiver(receiver);
                    }
                });
            }
        }).distinctUntilChanged();
    }


    public static boolean inMainProcess(Context context) {
        String packageName = context.getPackageName();
        String processName = getProcessName(context);
        return packageName.equals(processName);
    }

    public static String getProcessName(Context context) {
        String processName = null;
        ActivityManager manager = ((ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE));
        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : manager.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;
                    break;
                }
            }
            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }
            // take a rest and again
            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
