package com.gzq.lib_core.http.observer;

import android.util.Log;

import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.exception.ErrorType;
import com.gzq.lib_core.utils.NetworkUtils;
import com.gzq.lib_core.utils.ToastUtils;

import io.reactivex.observers.DisposableObserver;


public abstract class BaseObserver<T> extends DisposableObserver<T>{
    private static final String TAG = "BaseObserver";
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        if (!NetworkUtils.isNetworkAvailable()) {
            ToastUtils.showShort("当前无网络，请检查网络情况");
            onComplete();
            // 无网络执行complete后取消注册防调用onError
            if (!isDisposed()) {
                dispose();
            }
        } else {
            Log.d(TAG, "network available");
        }
    }

    @Override
    public void onError(Throwable e) {
        if(e instanceof ApiException){
            onError((ApiException)e);
        }else{
            onError(new ApiException(e, ErrorType.UNKNOWN));
        }
    }
    /**
     * 错误回调
     */
    protected abstract void onError(ApiException ex);
}
