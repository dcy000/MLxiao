package com.gcml.common;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleRegistry;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public class LazyFragment extends Fragment implements LifecycleObserver {
    public static final String TAG = "LazyFragment";

    private boolean isPageResume;

    public boolean isPageResume() {
        return isPageResume;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Lifecycle lifecycle = getLifecycle();
        if (lifecycle instanceof LifecycleRegistry) {
            lifecycle.addObserver(this);
        }
    }

    @Override
    public void onResume() {
        if (!isPageResume && !isHidden() && getUserVisibleHint()) {
            isPageResume = true;
            onPageResume();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (isPageResume) {
            isPageResume = false;
            onPagePause();
        }
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            if (isPageResume && getUserVisibleHint()) {
                isPageResume = false;

                handleEvent(Lifecycle.Event.ON_PAUSE);

                onPagePause();
            }
        } else {
            if (!isPageResume && getUserVisibleHint()) {
                isPageResume = true;

                handleEvent(Lifecycle.Event.ON_RESUME);

                onPageResume();
            }
        }
        super.onHiddenChanged(hidden);
    }

    private void handleEvent(Lifecycle.Event event) {
        Lifecycle lifecycle = getLifecycle();
        if (lifecycle instanceof LifecycleRegistry) {
            ((LifecycleRegistry) lifecycle).handleLifecycleEvent(event);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (!isPageResume && !isHidden()) {
                isPageResume = true;

                handleEvent(Lifecycle.Event.ON_RESUME);

                onPageResume();
            }
        } else {
            if (isPageResume && !isHidden()) {
                isPageResume = false;

                handleEvent(Lifecycle.Event.ON_PAUSE);

                onPagePause();
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    protected void onPageResume() {

    }

    protected void onPagePause() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Lifecycle lifecycle = getLifecycle();
        if (lifecycle instanceof LifecycleRegistry) {
            lifecycle.removeObserver(this);
        }
    }
}
