package com.ml.edu.common.base;

import com.ml.edu.common.base.scheduler.PostSchedulerFactory;
import com.ml.edu.common.base.scheduler.TaskSchedulerFactory;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by afirez on 18-2-1.
 */

public abstract class UseCase<Params, Result> {

    private final TaskSchedulerFactory taskSchedulerFactory;
    private final PostSchedulerFactory postSchedulerFactory;

    private final CompositeDisposable disposables;

    public UseCase() {
        this(TaskSchedulerFactory.getInstance(), PostSchedulerFactory.getInstance());
    }

    public UseCase(
            TaskSchedulerFactory taskSchedulerFactory,
            PostSchedulerFactory postSchedulerFactory) {
        this.taskSchedulerFactory = taskSchedulerFactory;
        this.postSchedulerFactory = postSchedulerFactory;
        this.disposables = new CompositeDisposable();
    }

    protected abstract Observable<Result> rxResult(Params params);

    public void execute(Params params, DisposableObserver<Result> observer) {
        Observable<Result> observable = this.rxResult(params)
                .subscribeOn(taskSchedulerFactory.scheduler())
                .observeOn(postSchedulerFactory.scheduler());
        addDisposable(observable.subscribeWith(observer));
    }

    private void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    public void dispose() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }
}
