package com.ml.edu.common.base.scheduler;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by afirez on 18-2-1.
 */
@Singleton
public class TaskSchedulerFactory implements SchedulerFactory {

    private TaskExecutor taskExecutor;

    @Inject
    public TaskSchedulerFactory(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public Scheduler scheduler() {
        return Schedulers.from(taskExecutor);
    }
}
