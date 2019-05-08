package com.medlink.danbogh.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;

import com.gcml.common.utils.DefaultObserver;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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

    private static AlarmRepository alarmRepository = new AlarmRepository();

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
        Observable.just("updateAlarms")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        AlarmHelper.cancelAlarms(context);
                        return s;
                    }
                })
                .flatMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<Object> apply(String s) throws Exception {
                        if (model.getId() < 0) {
                            return alarmRepository.add(model).subscribeOn(Schedulers.io());
                        } else {
                            return alarmRepository.update(model).subscribeOn(Schedulers.io());
                        }
                    }
                })
                .map(new Function<Object, Object>() {
                    @Override
                    public Object apply(Object o) throws Exception {
                        AlarmHelper.setupAlarms(context);
                        return o;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

    public static void setupAlarms(Context context) {
        Observable.just("setupAlarms")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        cancelAlarms(context);
                        return s;
                    }
                })
                .flatMap(new Function<String, ObservableSource<List<AlarmModel>>>() {
                    @Override
                    public ObservableSource<List<AlarmModel>> apply(String o) throws Exception {
                        return alarmRepository.findAll();
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultObserver<List<AlarmModel>>() {
                    @Override
                    public void onNext(List<AlarmModel> models) {
                        setAlarms(context, models);
                    }
                });
    }

    private static void setAlarms(Context context, List<AlarmModel> models) {
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
        alarmRepository.delete(model.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        AlarmManager manager =
                                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        if (model.isEnabled()) {
                            PendingIntent pi = newPendingIntent(context, model);
                            manager.cancel(pi);
                        }
                    }
                })
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {

                    }
                });
    }

    public static void cancelAlarms(Context context) {
        Observable.just("cancelAlarms")
                .flatMap(new Function<String, ObservableSource<List<AlarmModel>>>() {
                    @Override
                    public ObservableSource<List<AlarmModel>> apply(String o) throws Exception {
                        return alarmRepository.findAll();
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultObserver<List<AlarmModel>>() {
                    @Override
                    public void onNext(List<AlarmModel> models) {
                        AlarmManager manager =
                                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        for (AlarmModel alarm : models) {
                            if (alarm.isEnabled()) {
                                PendingIntent pi = newPendingIntent(context, alarm);
                                manager.cancel(pi);
                            }
                        }
                    }
                });
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
