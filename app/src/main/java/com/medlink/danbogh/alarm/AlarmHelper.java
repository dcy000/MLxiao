package com.medlink.danbogh.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by lenovo on 2017/9/19.
 */

public class AlarmHelper {
    public static void addAlarm(Context context, int hourOfDay, int minute) {
        addAlarm(context, hourOfDay, minute, 0, 0);
    }

    public static void addAlarm(
            Context context,
            int hourOfDay,
            int minute,
            int second,
            int millisecond) {
        long nextTime = SystemClock.elapsedRealtime();
        long nowTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(nowTime);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        long targetTime = calendar.getTimeInMillis();
        if (nowTime > targetTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            targetTime = calendar.getTimeInMillis();
        }
        long nextTimeDelta = targetTime - nowTime;
        nextTime += nextTimeDelta;

        Intent i = new Intent().setAction(AlarmService.ACTION_ALARM);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        i.putExtra("targetTime", targetTime);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                nextTime,
                AlarmManager.INTERVAL_DAY,
                pi
        );


    }
}
