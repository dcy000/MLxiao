package com.gcml.common.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

/**
 * Created by afirez on 2017/7/12.
 */

public abstract class BasePresenter<V extends IView>
        implements IPresenter<V> {
    private V view;

    private LifecycleOwner lifecycleOwner;

    public BasePresenter() {

    }

    @Override
    public void setView(V view) {
        this.view = view;
    }

    public V getView() {
        return view;
    }

    @Override
    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    @Override
    public LifecycleOwner getLifecycleOwner() {
        return lifecycleOwner;
    }

    @Override
    public void onCreate(LifecycleOwner owner) {

    }

    @Override
    public void onStart(LifecycleOwner owner) {

    }

    @Override
    public void onResume(LifecycleOwner owner) {

    }

    @Override
    public void onPause(LifecycleOwner owner) {

    }

    @Override
    public void onStop(LifecycleOwner owner) {

    }

    @Override
    public void onDestroy(LifecycleOwner owner) {

    }

    @Override
    public void onLifecycleChanged(LifecycleOwner owner, Lifecycle.Event event) {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onCleared() {
        view = null;
        lifecycleOwner = null;
    }
}