package com.ml.edu.common.base.scheduler;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;

/**
 * Created by afirez on 18-2-1.
 */
@Singleton
public class TaskSchedulerFactory implements SchedulerFactory {

    private static volatile TaskSchedulerFactory sInstance;

    public static TaskSchedulerFactory getInstance() {
        if (sInstance == null) {
            synchronized (TaskSchedulerFactory.class) {
                if (sInstance == null) {
                    sInstance = new TaskSchedulerFactory();
                }
            }
        }
        return sInstance;
    }

    private TaskExecutor taskExecutor;

    public TaskSchedulerFactory() {
        taskExecutor = TaskExecutor.getInstance();
    }

    @Inject
    public TaskSchedulerFactory(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public Scheduler scheduler() {
        return Schedulers.from(taskExecutor);
    }
}
