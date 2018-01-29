package com.example.han.referralproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.annotation.IntDef;
import android.util.Log;

/**
 * Created by afirez on 18-1-26.
 */

public class BatteryHelper {

    private static final String TAG = "BatteryHelper";

    public static final int STATUS_LOW = 1;
    public static final int STATUS_NORMAL = 2;
    public static final int STATUS_CHARGING = 3;
    public static final int STATUS_FULL = 4;

    @IntDef({
            STATUS_LOW,
            STATUS_NORMAL,
            STATUS_CHARGING,
            STATUS_FULL
    })
    public @interface Status {

    }

    public interface OnBatteryChangeListener {
        void onBatteryChanged(int percent);

        void onBatteryStatusChanged(@Status int status);
    }

    public interface OnPowerConnectionChangeListener {
        void onPowerConnectionChanged(boolean connected);
    }

    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    private OnBatteryChangeListener mOnBatteryChangeListener;

    private OnPowerConnectionChangeListener mOnPowerConnectionChangeListener;

    public void setOnBatteryChangeListener(
            OnBatteryChangeListener onBatteryChangeListener) {
        mOnBatteryChangeListener = onBatteryChangeListener;
    }

    public void setOnPowerConnectionChangeListener(
            OnPowerConnectionChangeListener onPowerConnectionChangeListener) {
        mOnPowerConnectionChangeListener = onPowerConnectionChangeListener;
    }

    private IntentFilter batteryFilter;

    public void start() {
        if (batteryFilter == null) {
            batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        }
        Intent intent = sContext.registerReceiver(
                null, batteryFilter);
        if (intent != null) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int percent = level * 100 / scale;
            Log.d(TAG, String.valueOf(percent));
            if (mOnBatteryChangeListener != null) {
                mOnBatteryChangeListener.onBatteryChanged(percent);
            }
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                    BatteryManager.BATTERY_STATUS_UNKNOWN);
            sContext.registerReceiver(receiver(), filter());

            boolean charging = BatteryManager.BATTERY_STATUS_CHARGING == status;
            if (mOnPowerConnectionChangeListener != null) {
                mOnPowerConnectionChangeListener.onPowerConnectionChanged(charging);
            }

            if (mOnBatteryChangeListener == null) {
                return;
            }
            if (!charging && percent <= 20) {
                mOnBatteryChangeListener.onBatteryStatusChanged(STATUS_LOW);
                return;
            }
            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    mOnBatteryChangeListener.onBatteryStatusChanged(STATUS_CHARGING);
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    mOnBatteryChangeListener.onBatteryStatusChanged(STATUS_FULL);
                    break;
                default:
                    mOnBatteryChangeListener.onBatteryStatusChanged(STATUS_NORMAL);
                    break;
            }
        }
    }

    public void stop() {
        if (receiver != null) {
            BatteryReceiver temp = this.receiver;
            this.receiver = null;
            filter = null;
            sContext.unregisterReceiver(temp);
        }
    }

    private volatile BatteryReceiver receiver;

    private volatile IntentFilter filter;


    private BatteryReceiver receiver() {
        if (receiver != null) {
            return receiver;
        }
        receiver = new BatteryReceiver();
        return receiver;
    }

    private IntentFilter filter() {
        if (filter != null) {
            return filter;
        }
        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        return filter;
    }


    public class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int percent = level * 100 / scale;
                Log.d(TAG, String.valueOf(percent));
                if (mOnBatteryChangeListener != null) {
                    mOnBatteryChangeListener.onBatteryChanged(percent);
                }
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                        BatteryManager.BATTERY_STATUS_UNKNOWN);
                boolean charging = BatteryManager.BATTERY_STATUS_CHARGING == status;
                if (mOnPowerConnectionChangeListener != null) {
                    mOnPowerConnectionChangeListener.onPowerConnectionChanged(charging);
                }
                if (mOnBatteryChangeListener == null) {
                    return;
                }
                if (!charging && percent <= 20) {
                    mOnBatteryChangeListener.onBatteryStatusChanged(STATUS_LOW);
                    return;
                }
                switch (status) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        mOnBatteryChangeListener.onBatteryStatusChanged(STATUS_CHARGING);
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        mOnBatteryChangeListener.onBatteryStatusChanged(STATUS_FULL);
                        break;
                    default:
                        mOnBatteryChangeListener.onBatteryStatusChanged(STATUS_NORMAL);
                        break;
                }
                return;
            }

            if (Intent.ACTION_POWER_CONNECTED.equals(action)) {
                Log.d(TAG, "power connected");
                if (mOnPowerConnectionChangeListener != null) {
                    mOnPowerConnectionChangeListener.onPowerConnectionChanged(true);
                }
            } else if (Intent.ACTION_POWER_DISCONNECTED.equals(action)) {
                Log.e(TAG, "power disconnected");
                if (mOnPowerConnectionChangeListener != null) {
                    mOnPowerConnectionChangeListener.onPowerConnectionChanged(false);
                }
            }
        }
    }
}