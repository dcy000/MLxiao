package com.gzq.lib_core.utils;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import com.gzq.lib_core.http.exception.ErrorTransformer;
import com.gzq.lib_core.http.model.BaseModel;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxUtils {
    /**
     * 默认回调到主线程
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<BaseModel<T>, T> httpResponseTransformer() {
        return new ObservableTransformer<BaseModel<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<BaseModel<T>> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .compose(ErrorTransformer.<T>getInstance())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 自己决定是否回调到主线程
     * @param isObserveOnMain
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<BaseModel<T>, T> httpResponseTransformer(final boolean isObserveOnMain) {
        return new ObservableTransformer<BaseModel<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<BaseModel<T>> upstream) {
                if (isObserveOnMain){
                    return upstream.subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.newThread())
                            .compose(ErrorTransformer.<T>getInstance())
                            .observeOn(AndroidSchedulers.mainThread());
                }
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .compose(ErrorTransformer.<T>getInstance());
            }
        };
    }

    public static <T> AutoDisposeConverter<T> autoDisposeConverter(LifecycleOwner owner) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner));
    }

    public static <T> AutoDisposeConverter<T> autoDisposeConverter(LifecycleOwner owner, Lifecycle.Event event) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner, event));
    }

}
