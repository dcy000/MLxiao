package com.medlink.danbogh.cache;


import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Looper;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by afirez on 2017/9/6.
 */

public class RxLife<T> implements ObservableTransformer<T, T>, LifecycleObserver {

    public static <T> ObservableTransformer<T, T> ensureResumed(LifecycleOwner owner) {
        return new RxLife<>(owner, Lifecycle.State.RESUMED);
    }

    public static <T> ObservableTransformer<T, T> ensureStarted(LifecycleOwner owner) {
        return new RxLife<>(owner, Lifecycle.State.STARTED);
    }

    public static <T> ObservableTransformer<T, T> ensureCreated(LifecycleOwner owner) {
        return new RxLife<>(owner, Lifecycle.State.CREATED);
    }

    private LifecycleOwner mLifecycleOwner;

    private Disposable mDisposable;

    private Lifecycle.State mActiveState;

    private boolean mIsActiveState;

    private int mVersion = -1;

    private int mLastVersion = -1;

    private T mLastData;

    private PublishSubject<T> mRxData = PublishSubject.create();

    private RxLife(LifecycleOwner owner, Lifecycle.State activeState) {
        mLifecycleOwner = owner;
        mActiveState = activeState;
    }

    @Override
    public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
        assertMainThread();
        Lifecycle lifecycle = mLifecycleOwner.getLifecycle();
        if (lifecycle.getCurrentState() != Lifecycle.State.DESTROYED) {
            lifecycle.addObserver(this);
            upstream.subscribe(new Observer<T>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    assertMainThread();
                    mDisposable = d;
                }

                @Override
                public void onNext(@NonNull T t) {
                    assertMainThread();
                    mVersion++;
                    mLastData = t;
                    considerNotify();
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    assertMainThread();
                    if (!mDisposable.isDisposed()) {
                        mRxData.onError(e);
                    }
                }

                @Override
                public void onComplete() {
                    assertMainThread();
                    if (!mDisposable.isDisposed()) {
                        mRxData.onComplete();
                    }
                }
            });
            return mRxData.doOnDispose(new Action() {
                @Override
                public void run() throws Exception {
                    mDisposable.dispose();
                }
            });
        } else {
            return Observable.empty();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    void onStateChange() {
        Lifecycle lifecycle = mLifecycleOwner.getLifecycle();
        if (lifecycle.getCurrentState() == Lifecycle.State.DESTROYED) {
            if (mDisposable != null && !mDisposable.isDisposed()) {
                mDisposable.isDisposed();
            }
            lifecycle.removeObserver(this);
        } else {
            onActiveStateChanged(isActiveState(lifecycle.getCurrentState(), mActiveState));
        }
    }

    private void onActiveStateChanged(boolean activeState) {
        if (mIsActiveState != activeState) {
            mIsActiveState = activeState;
            considerNotify();
        }
    }

    private void considerNotify() {
        if (mIsActiveState
                && isActiveState(mLifecycleOwner.getLifecycle().getCurrentState(), mActiveState)
                && mLastVersion < mVersion) {
            mLastVersion = mVersion;
            if (mDisposable != null && !mDisposable.isDisposed()) {
                mRxData.onNext(mLastData);
            }
        }
    }

    public static boolean isActiveState(Lifecycle.State state, Lifecycle.State activeState) {
        return state.isAtLeast(activeState);
    }

    private static void assertMainThread() {
        if (!isMainThread()) {
            throw new IllegalStateException("should not use RxLife Transformer at a background thread");
        }
    }

    private static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
