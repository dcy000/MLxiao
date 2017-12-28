package com.medlink.danbogh.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lenovo on 2017/9/19.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final String ACTION_ALARM = "com.medlink.intent.Alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmHelper.setupAlarms(context);
    }
}
