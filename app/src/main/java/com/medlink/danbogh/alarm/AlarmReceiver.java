package com.medlink.danbogh.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by lenovo on 2017/9/19.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final String ACTION_ALARM = "com.medlink.intent.Alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "定时提醒", Toast.LENGTH_SHORT).show();
    }
}
