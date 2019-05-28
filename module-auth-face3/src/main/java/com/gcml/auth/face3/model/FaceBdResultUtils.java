package com.gcml.auth.face3.model;

import android.support.annotation.NonNull;


import com.gcml.auth.face3.model.entity.FaceBdResult;
import com.gcml.auth.face3.model.exception.FaceBdError;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

public class FaceBdResultUtils {
    public static <T> ObservableTransformer<FaceBdResult<T>, T> faceBdResultTransformer() {
        return new ObservableTransformer<FaceBdResult<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<FaceBdResult<T>> upstream) {
                return upstream
                        .onErrorResumeNext(httpErrorProcessor())
                        .flatMap(faceBdResultMapper());
            }
        };
    }

    @NonNull
    private static <T> Function<FaceBdResult<T>, Observable<T>> faceBdResultMapper() {
        return new Function<FaceBdResult<T>, Observable<T>>() {
            @Override
            public Observable<T> apply(FaceBdResult<T> result) {
                if (result.isSuccess()) {
                    return Observable.just(result.getData());
                }
                FaceBdErrorUtils.clearTokenIfNeeded(result.getErrorCode());
                return Observable.error(new FaceBdError(result.getErrorCode(), result.getErrorMsg()));
            }
        };
    }

    @NonNull
    private static <T> Function<Throwable, ObservableSource<? extends FaceBdResult<T>>> httpErrorProcessor() {
        return new Function<Throwable, ObservableSource<? extends FaceBdResult<T>>>() {
            @Override
            public ObservableSource<? extends FaceBdResult<T>> apply(Throwable throwable) throws Exception {
                return Observable.error(new FaceBdError(throwable));
            }
        };
    }
}
