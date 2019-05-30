package com.medlink.danbogh.alarm;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.example.han.referralproject.recyclerview.DoctorAlarmActivity;

/**
 * Created by lenovo on 2017/9/19.
 */

public class AlarmService extends Service {
    public static final String ACTION_ALARM = "com.medlink.intent.Alarm";

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long id = intent.getLongExtra(AlarmHelper.ID, -1);
        if (id != -1) {
            AlarmModel model = AlarmModel.find(AlarmModel.class, id);
            if (model != null) {
                if (model.getInterval() == AlarmModel.INTERVAL_NONE) {
                    model.setEnabled(false);
                    model.update(id);
                }
            }
        }
        String tag = intent.getStringExtra("tag");
        Intent alarmIntent = new Intent();
        if (tag == null || tag.isEmpty()) {
            alarmIntent.setClass(getBaseContext(), ReminderActivity.class);
        } else if ("1".equals(tag)) {
            alarmIntent.setClass(getBaseContext(), DoctorAlarmActivity.class);
        }

        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtras(intent);
        getApplication().startActivity(alarmIntent);
        AlarmHelper.setupAlarms(this);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
