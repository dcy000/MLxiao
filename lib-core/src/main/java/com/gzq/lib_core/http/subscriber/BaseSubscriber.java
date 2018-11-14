package com.gzq.lib_core.http.subscriber;

import android.util.Log;

import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.exception.ErrorType;
import com.gzq.lib_core.utils.NetworkUtils;
import com.gzq.lib_core.utils.ToastUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.2.5
 * created on 2018/10/15 17:07
 * created by: gzq
 * description: TODO
 */
public abstract class BaseSubscriber<T> implements Subscriber<T> {
    private static final String TAG = "BaseSubscriber";
    protected Subscription mSubscription;

    @Override
    public void onSubscribe(Subscription s) {
        mSubscription = s;
        if (!NetworkUtils.isNetworkAvailable()) {
            ToastUtils.showShort("当前无网络，请检查网络情况");
            onComplete();
            // 无网络执行complete后取消注册防调用onError
            mSubscription.cancel();

        } else {
            Log.d(TAG, "network available");
            mSubscription.request(1);
        }
    }


    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            onError((ApiException) e);
        } else {
            onError(new ApiException(e, ErrorType.UNKNOWN));
        }
    }

    @Override
    public void onComplete() {

    }

    /**
     * 错误回调
     */
    protected abstract void onError(ApiException ex);

}
