package com.gcml.common.repository.utils;


import com.gcml.common.repository.http.ApiException;
import com.gcml.common.repository.http.ApiResult;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

/**
 * Created by afirez on 18-2-6.
 */

public class RxResultUtils {

    public static <T> ObservableTransformer<ApiResult<T>, T> apiResultTransformer() {
        return new ObservableTransformer<ApiResult<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<ApiResult<T>> upstream) {
                return upstream.flatMap(
                        new Function<ApiResult<T>, Observable<T>>() {
                            @Override
                            public Observable<T> apply(ApiResult<T> result) {
                                if (result.isSuccessful()) {
                                    return Observable.just(result.getData());
                                } else {
                                    return Observable.error(new ApiException(result.getMessage()));
                                }
                            }
                        }
                );
            }
        };
    }
}
