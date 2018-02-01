package com.ml.edu.common.base.scheduler;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by afirez on 18-2-1.
 */
@Singleton
public class TaskExecutor implements Executor {

    private final ThreadPoolExecutor threadPoolExecutor;

    @Inject
    public TaskExecutor() {
        threadPoolExecutor = new ThreadPoolExecutor(
                3,
                5,
                10, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new TaskThreadFactory()
        );
    }

    @Override
    public void execute(@NonNull Runnable command) {
        this.threadPoolExecutor.execute(command);
    }

    private static class TaskThreadFactory implements ThreadFactory {

        private int counter = 0;

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable, "task-" + counter++);
        }
    }
}
