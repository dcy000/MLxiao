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
import java.util.TimeZone;

/**
 * Created by lenovo on 2017/9/19.
 */

public class AlarmHelper {

    public static final String ID = "id";
    public static final String CONTENT = "content";
    public static final String HOUR_OF_DAY = "hourOfDay";
    public static final String MINUTE = "minute";
    public static final String TONE = "tone";
    public static final String TAG = "tag";
    public static final String TIMESTAMP = "timestamp";

    public static void setupAlarm(Context context, long timestamp, String content, String tag) {
        AlarmModel model = new AlarmModel();
        model.setTimestamp(timestamp);
        model.setContent(content);
        model.setInterval(AlarmModel.INTERVAL_NONE);
        model.setEnabled(true);
        model.setTag(tag);
        setupAlarm(context, model);
    }

    public static void setupAlarm(
            Context context,
            int hourOfDay,
            int minute,
            String content,
            int interval) {
        AlarmModel model = new AlarmModel();
        model.setMinute(minute);
        model.setHourOfDay(hourOfDay);
        model.setContent(content);
        model.setInterval(interval);
        model.setEnabled(true);
        setupAlarm(context, model);
    }

    public static void setupAlarm(Context context, int hourOfDay, int minute, String content) {
        AlarmModel model = new AlarmModel();
        model.setMinute(minute);
        model.setHourOfDay(hourOfDay);
        model.setContent(content);
        model.setInterval(AlarmModel.INTERVAL_DAY);
        model.setEnabled(true);
        setupAlarm(context, model);
    }

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
            if (!model.isEnabled()) {
                continue;
            }

            Calendar nextCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
            PendingIntent pi = newPendingIntent(context, model);
            //一次性闹钟
            if (model.getInterval() == AlarmModel.INTERVAL_NONE) {
                long timestamp = model.getTimestamp();
                if (timestamp != -1
                        && timestamp > nextCalendar.getTimeInMillis()) {
                        setupAlarm(context, timestamp, pi);
                }
                continue;
            }

            nextCalendar.set(Calendar.HOUR_OF_DAY, model.getHourOfDay());
            nextCalendar.set(Calendar.MINUTE, model.getMinute());
            nextCalendar.set(Calendar.SECOND, 0);
            final int nowDayOfWeek = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).get(Calendar.DAY_OF_WEEK);
            final int nowHourOfDay = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).get(Calendar.HOUR_OF_DAY);
            final int nowMinute = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).get(Calendar.MINUTE);

            //每天
            if (model.getInterval() == AlarmModel.INTERVAL_DAY) {
                if (model.getHourOfDay() < nowHourOfDay
                        || model.getMinute() <= nowMinute) {
                    nextCalendar.add(Calendar.DAY_OF_MONTH, 1);
                }
                setupAlarm(context, nextCalendar.getTimeInMillis(), pi);
                continue;
            }

            //隔天
            if (model.getInterval() == AlarmModel.INTERVAL_ONE_DAY) {
                if (model.getHourOfDay() < nowHourOfDay
                        || model.getMinute() <= nowMinute) {
                    nextCalendar.add(Calendar.DAY_OF_MONTH, 2);
                }
                setupAlarm(context, nextCalendar.getTimeInMillis(), pi);
                continue;
            }


            //每周
            if (model.getInterval() == AlarmModel.INTERVAL_WEEK) {
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

    public static void setupAlarm(Context context, long triggerAtMillis, PendingIntent pi) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pi);
        }
    }

    public static void cancelAlarm(Context context, AlarmModel model) {
        int delete = model.delete();
        AlarmManager manager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (model.isEnabled()) {
            PendingIntent pi = newPendingIntent(context, model);
            manager.cancel(pi);
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
        return PendingIntent.getService(context, (int) model.getId(), i, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @NonNull
    private static Intent newIntent(Context context, AlarmModel model, Class<?> clazz) {
        Intent i = new Intent(context, clazz);
        i.putExtra(ID, model.getId());
        i.putExtra(CONTENT, model.getContent());
        long timestamp = model.getTimestamp();
        if (timestamp == -1) {
            i.putExtra(HOUR_OF_DAY, model.getHourOfDay());
            i.putExtra(MINUTE, model.getMinute());
        } else {
            i.putExtra(TIMESTAMP, timestamp);
        }
        Uri tone = model.getTone();
        i.putExtra(TONE, tone == null ? "" : tone.toString());
        String tag = model.getTag();
        i.putExtra(TAG, tag == null ? "" : tag);
        return i;
    }
}
