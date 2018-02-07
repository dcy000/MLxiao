package com.example.han.referralproject.new_music;

/**
 * Created by hzwangchenyan on 2017/2/8.
 */
public abstract class HttpCallback<T> {
    public abstract void onSuccess(T t);

    public abstract void onFail(Exception e);

    public void onFinish() {
    }
}
