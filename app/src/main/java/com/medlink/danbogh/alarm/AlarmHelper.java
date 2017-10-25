package com.medlink.danbogh.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;

/**
 * Created by lenovo on 2017/9/19.
 */

public class AlarmHelper {

    public static final String ID = "id";
    public static final String CONTENT = "content";
    public static final String HOUR_OF_DAY = "hourOfDay";
    public static final String MINUTE = "minute";
    public static final String TONE = "tone";


    public static void setupAlarm(Context context, int hourOfDay, int minute) {
        AlarmModel model = new AlarmModel();
        model.setMinute(minute);
        model.setHourOfDay(hourOfDay);
        model.setContent("吃药提醒");
        model.setInterval(AlarmModel.INTERVAL_DAY);
        model.setEnabled(true);
        setupAlarm(context, model);
    }

    public static void setupAlarm(Context context, AlarmModel model) {
        AlarmHelper.cancelAlarms(context);
        if (model.getId() < 0) {
            model.save();
        } else {
            model.update(model.getId());
        }
        AlarmHelper.setupAlarms(context);
    }

    public static void setupAlarms(Context context) {
        cancelAlarms(context);

        List<AlarmModel> models = DataSupport.findAll(AlarmModel.class);
        for (AlarmModel model : models) {
            if (model.isEnabled()) {
                PendingIntent pi = newPendingIntent(context, model);
                Calendar nextCalendar = Calendar.getInstance();
                nextCalendar.set(Calendar.HOUR_OF_DAY, model.getHourOfDay());
                nextCalendar.set(Calendar.MINUTE, model.getMinute());
                nextCalendar.set(Calendar.SECOND, 0);
                final int nowDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                final int nowHourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                final int nowMinute = Calendar.getInstance().get(Calendar.MINUTE);

                if (model.getInterval() == AlarmModel.INTERVAL_DAY) {
                    if (model.getHourOfDay() < nowHourOfDay
                            || model.getMinute() <= nowMinute) {
                        nextCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    setupAlarm(context, nextCalendar.getTimeInMillis(), pi);
                } else if (model.getInterval() == AlarmModel.INTERVAL_WEEK) {
                    boolean alarmSet = false;
                    for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++dayOfWeek) {
                        if (model.hasDayOfWeek(dayOfWeek)
                                && dayOfWeek >= nowDayOfWeek
                                && !(dayOfWeek == nowDayOfWeek && model.getHourOfDay() < nowHourOfDay)
                                && !(dayOfWeek == nowDayOfWeek && model.getHourOfDay() == nowHourOfDay && model.getMinute() <= nowMinute)) {
                            nextCalendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                            setupAlarm(context, nextCalendar.getTimeInMillis(), pi);
                            alarmSet = true;
                            break;
                        }
                    }
                    if (!alarmSet) {
                        for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; ++dayOfWeek) {
                            if (model.hasDayOfWeek(dayOfWeek)
                                    && dayOfWeek <= nowDayOfWeek
                                    && model.getInterval() == AlarmModel.INTERVAL_WEEK) {
                                nextCalendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                                nextCalendar.add(Calendar.WEEK_OF_YEAR, 1);
                                setupAlarm(context, nextCalendar.getTimeInMillis(), pi);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public static void setupAlarm(Context context, long triggerAtMillis, PendingIntent pi) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi);
        }
    }

    public static void cancelAlarms(Context context) {
        List<AlarmModel> models = DataSupport.findAll(AlarmModel.class);
        if (models != null) {
            AlarmManager manager =
                    (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            for (AlarmModel alarm : models) {
                if (alarm.isEnabled()) {
                    PendingIntent pi = newPendingIntent(context, alarm);
                    manager.cancel(pi);
                }
            }
        }
    }

    private static PendingIntent newPendingIntent(Context context, AlarmModel model) {
        Intent i = newIntent(context, model, AlarmService.class);
        return PendingIntent.getService(context, (int) model.getId(), i, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @NonNull
    private static Intent newIntent(Context context, AlarmModel model, Class<?> clazz) {
        Intent i = new Intent(context, clazz);
        i.putExtra(ID, model.getId());
        i.putExtra(CONTENT, model.getContent());
        i.putExtra(HOUR_OF_DAY, model.getHourOfDay());
        i.putExtra(MINUTE, model.getMinute());
        Uri tone = model.getTone();
        i.putExtra(TONE, tone == null ? "" : tone.toString());
        return i;
    }
}
