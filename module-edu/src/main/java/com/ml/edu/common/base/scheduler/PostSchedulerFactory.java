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

    @Inject
    public PostSchedulerFactory() {
    }

    @Override
    public Scheduler scheduler() {
        return AndroidSchedulers.mainThread();
    }
}
