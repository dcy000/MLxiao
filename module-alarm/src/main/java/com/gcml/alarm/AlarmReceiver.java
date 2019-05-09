package com.gcml.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gcml.common.utils.DefaultObserver;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2017/9/19.
 */

public class AlarmReceiver extends BroadcastReceiver {
    public static final String ACTION_ALARM = "com.medlink.intent.Alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                AlarmHelper.setupAlarms(context);
                return new Object();
            }
        }).subscribeOn(Schedulers.io()).subscribe(new DefaultObserver<>());
    }
}
