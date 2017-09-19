package com.medlink.danbogh.alarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by lenovo on 2017/9/19.
 */

public class AlarmService extends Service {
    public static final String ACTION_ALARM = "com.medlink.intent.Alarm";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_ALARM.equals(intent.getAction())) {
            onAlarm();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void onAlarm() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
