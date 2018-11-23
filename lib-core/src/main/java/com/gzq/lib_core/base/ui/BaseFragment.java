package com.gzq.lib_core.base.ui;

import android.app.Activity;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gzq.lib_core.base.App;
import com.gzq.lib_core.base.delegate.AppDelegate;
import com.gzq.lib_core.base.delegate.AppLifecycle;

import java.util.Set;

import timber.log.Timber;


public abstract class BaseFragment<V extends IView, P extends IPresenter>
        extends Fragment implements IView {
    protected View mView;
    protected Activity mActivity;
    protected Context mContext;
    protected P mPresenter;
    private Set<IEvents> events = App.getEvents();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = layoutId(savedInstanceState);
        if (layoutId <= 0) {
            throw new IllegalArgumentException("layout is null");
        }
        if (mView == null) {
            mView = inflater.inflate(layoutId, container, false);
            initParams(getArguments());
            mPresenter = obtainPresenter();
            if (mPresenter == null || !(mPresenter instanceof LifecycleObserver)) {
                throw new IllegalArgumentException("obtain a wrong presenter");
            }
            getLifecycle().addObserver(mPresenter);
            initView(mView);

        }
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mView != null) {
            ((ViewGroup) mView.getParent()).removeView(mView);
            mView = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mView = null;
        mActivity = null;
        mContext = null;
        mPresenter = null;
    }

    public abstract int layoutId(Bundle savedInstanceState);

    public abstract void initParams(Bundle bundle);

    public abstract void initView(View view);

    public abstract P obtainPresenter();

    public void createEvent(String tag, Object... params) {
        for (IEvents event : events) {
            event.onEvent(tag, params);
        }
    }
}