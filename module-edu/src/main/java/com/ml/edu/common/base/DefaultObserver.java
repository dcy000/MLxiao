package com.ml.edu.common.base;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by afirez on 18-2-1.
 */

public class DefaultObserver<T> extends DisposableObserver<T> {
    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
