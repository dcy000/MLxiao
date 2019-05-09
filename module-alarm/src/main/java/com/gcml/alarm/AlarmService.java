package com.gcml.alarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.gcml.common.utils.DefaultObserver;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2017/9/19.
 */

public class AlarmService extends Service {
    public static final String ACTION_ALARM = "com.medlink.intent.Alarm";

    private AlarmRepository alarmRepository = new AlarmRepository();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long id = intent.getLongExtra(AlarmHelper.ID, -1);
        if (id != -1) {
            alarmRepository.findOneById(id)
                    .flatMap(new Function<AlarmEntity, ObservableSource<AlarmEntity>>() {
                        @Override
                        public ObservableSource<AlarmEntity> apply(AlarmEntity model) throws Exception {
                            if (model.getInterval() == AlarmEntity.INTERVAL_NONE) {
                                model.setEnabled(false);
                                return alarmRepository.update(model)
                                        .map(new Function<Object, AlarmEntity>() {
                                            @Override
                                            public AlarmEntity apply(Object o) throws Exception {
                                                return model;
                                            }
                                        }).subscribeOn(Schedulers.io());
                            }
                            return Observable.just(model);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .subscribe(new DefaultObserver<AlarmEntity>() {
                        @Override
                        public void onNext(AlarmEntity model) {
                        }
                    });
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
