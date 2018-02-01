package com.ml.edu.common.base.scheduler;

import io.reactivex.Scheduler;

/**
 * Created by afirez on 18-2-1.
 */

public interface SchedulerFactory {
    Scheduler scheduler();
}
