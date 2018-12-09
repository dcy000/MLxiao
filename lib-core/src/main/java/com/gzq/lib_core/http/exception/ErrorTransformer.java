package com.gzq.lib_core.http.exception;


import com.gzq.lib_core.http.model.BaseModel;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * 加入了对错误处理，已经比较完整了
 */
public class ErrorTransformer<T> implements ObservableTransformer<BaseModel<T>, T> {

    public static <T> ErrorTransformer<T> create() {
        return new ErrorTransformer<>();
    }

    private static ErrorTransformer instance = null;

    private ErrorTransformer() {
    }

    /**
     * 双重校验锁单例(线程安全)
     */
    public static <T> ErrorTransformer<T> getInstance() {
        if (instance == null) {
            synchronized (ErrorTransformer.class) {
                if (instance == null) {
                    instance = new ErrorTransformer<>();
                }
            }
        }
        return instance;
    }

    @Override
    public ObservableSource<T> apply(Observable<BaseModel<T>> responseObservable) {
        return responseObservable.map(new Function<BaseModel<T>, T>() {
            @Override
            public T apply(BaseModel<T> httpResult) throws Exception {
                // 通过对返回码进行业务判断决定是返回错误还是正常取数据
                if (httpResult.getCode() != ErrorType.SUCCESS) {
                    throw new ServerException(httpResult.getMessage(), httpResult.getCode());
                }
                return httpResult.getData();
            }
        }).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends T>>() {
            @Override
            public ObservableSource<? extends T> apply(Throwable throwable) throws Exception {
                //ExceptionEngine为处理异常的驱动器
                throwable.printStackTrace();
                return Observable.error(ExceptionEngine.handleException(throwable));
            }
        });
    }
}
