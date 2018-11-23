package com.gzq.lib_core.base.ui;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.CallSuper;

import com.gzq.lib_core.base.App;

import java.util.Set;

/**
 * Created by afirez on 2017/7/12.
 */

public abstract class BasePresenter<V extends IView>
        implements IPresenter {
    protected V mView;

    protected LifecycleOwner mLifecycleOwner;
    private Set<IEvents> events = App.getEvents();

    public BasePresenter(V view) {
        this.mView = view;
    }

    @CallSuper
    @Override
    public void onCreate(LifecycleOwner owner) {
        this.mLifecycleOwner=owner;
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

    @CallSuper
    @Override
    public void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
        mView=null;
        mLifecycleOwner=null;
    }

    public void createEvent(String tag, Object... params) {
        for (IEvents event : events) {
            event.onEvent(tag, params);
        }
    }
}