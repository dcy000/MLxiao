package com.medlink.danbogh.utils.rxbus;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.Functions;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * RxBus Observer
 * Created by afirez on 2017/9/20.
 */

public class BusObserver<T> extends AtomicReference<Disposable>
        implements Observer<T>, Disposable {
    private static final long serialVersionUID = -5251123623727029452L;
    private final Consumer<? super T> onNext;
    private final Consumer<? super Throwable> onError;
    private final Action onComplete;
    private final Consumer<? super Disposable> onSubscribe;

    public BusObserver(Consumer<? super T> onNext) {
        this(onNext,
                Functions.ON_ERROR_MISSING,
                Functions.EMPTY_ACTION,
                Functions.emptyConsumer());
    }

    public BusObserver(
            Consumer<? super T> onNext,
            Consumer<? super Throwable> onError,
            Action onComplete,
            Consumer<? super Disposable> onSubscribe) {
        super();
        this.onNext = onNext;
        this.onError = onError;
        this.onComplete = onComplete;
        this.onSubscribe = onSubscribe;
    }

    public static <T> BusObserver<T> of(Consumer<? super T> onNext) {
        return new BusObserver<>(onNext);
    }

    @Override
    public void onSubscribe(Disposable s) {
        if (DisposableHelper.setOnce(this, s)) {
            try {
                onSubscribe.accept(this);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                onError(ex);
            }
        }
    }

    @Override
    public void onNext(T t) {
        if (!isDisposed()) {
            try {
                onNext.accept(t);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                onError(e);
            }
        }
    }

    @Override
    public void onError(Throwable t) {
        if (!isDisposed()) {
//            lazySet(DisposableHelper.DISPOSED);
            try {
                onError.accept(t);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(new CompositeException(t, e));
            }
        }
    }

    @Override
    public void onComplete() {
        if (!isDisposed()) {
            lazySet(DisposableHelper.DISPOSED);
            try {
                onComplete.run();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
        }
    }

    @Override
    public void dispose() {
        DisposableHelper.dispose(this);
    }

    @Override
    public boolean isDisposed() {
        return get() == DisposableHelper.DISPOSED;
    }
}
