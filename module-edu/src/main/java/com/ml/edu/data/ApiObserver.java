package com.ml.edu.data;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by afirez on 18-2-6.
 */

public class ApiObserver<T> extends DisposableObserver<T> {
    @Override
    public void onNext(T o) {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            ApiException ae = (ApiException) e;
            onError(ae.getMessage());
            return;
        }
        onError("服务器繁忙, 请稍后再试!");
    }

    public void onError(String message) {

    }

    @Override
    public void onComplete() {

    }
}
