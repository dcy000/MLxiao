package com.gcml.common.mvp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.gcml.common.utils.RxUtils;
import com.uber.autodispose.AutoDisposeConverter;

/**
 * Created by afirez on 2017/7/13.
 */
public abstract class BaseFragment<V extends IView, P extends IPresenter>
        extends Fragment
        implements IView, UiFactory<V, P> {
    protected P presenter;
    private V baseView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            baseView = (V) context;
        } catch (Exception e) {
            throw new RuntimeException("activity must implement IView");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = providePresenter(provideView());
        presenter.setLifecycleOwner(this);
        getLifecycle().addObserver(presenter);
    }

    @Override
    public void onDestroy() {
        baseView = null;
        super.onDestroy();
    }

    public <T> AutoDisposeConverter<T> autoDisposeConverter() {
        return RxUtils.autoDisposeConverter(this);
    }
}