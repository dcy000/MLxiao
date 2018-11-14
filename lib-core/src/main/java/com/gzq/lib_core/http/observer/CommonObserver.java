package com.gzq.lib_core.http.observer;

import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.utils.ToastUtils;


public abstract class CommonObserver<T> extends BaseObserver<T>{
    private static final String TAG = "CommonObserver";

    @Override
    protected void onError(ApiException ex) {
        ToastUtils.showShort(ex.message+":"+ex.code);
    }

    @Override
    public abstract void onNext(T t);

    @Override
    public void onComplete() {

    }
}
