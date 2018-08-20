package com.gcml.common.mvp;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import com.gcml.common.repository.utils.Preconditions;
import com.gcml.common.utils.RxUtils;
import com.uber.autodispose.AutoDisposeConverter;

/**
 * Created by afirez on 2017/7/12.
 */

public abstract class BasePresenter<V extends IView>
        implements IPresenter {
    protected V view;

    protected LifecycleOwner lifecycleOwner;

    public BasePresenter(V view) {
        this.view = view;
    }

    @Override
    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
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

    public <T> AutoDisposeConverter<T> autoDisposeConverter() {
        Preconditions.checkNotNull(lifecycleOwner, "lifecycleOwner == null");
        return RxUtils.autoDisposeConverter(lifecycleOwner);
    }
}