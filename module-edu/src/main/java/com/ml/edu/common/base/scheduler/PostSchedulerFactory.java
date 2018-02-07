package com.ml.edu.common.base.scheduler;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by afirez on 18-2-1.
 */

@Singleton
public class PostSchedulerFactory implements SchedulerFactory {

    private static volatile PostSchedulerFactory sInstance;

    public static PostSchedulerFactory getInstance() {
        if (sInstance == null) {
            synchronized (PostSchedulerFactory.class) {
                if (sInstance == null) {
                    sInstance = new PostSchedulerFactory();
                }
            }
        }
        return sInstance;
    }

    @Inject
    public PostSchedulerFactory() {
    }

    @Override
    public Scheduler scheduler() {
        return AndroidSchedulers.mainThread();
    }
}
