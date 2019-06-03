package com.gcml.module_yzn.repository;

import android.support.annotation.NonNull;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.http.ApiException;
import com.gcml.common.http.ApiResult;
import com.gcml.common.utils.Serializer;
import com.gcml.module_yzn.bean.OutBean;
import com.gcml.module_yzn.bean.WenJuanOutBean;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;

/**
 * Created by lenovo on 2019/5/10.
 */

public class YZNRepository {
    private YZNService yznService = RetrofitHelper.service(YZNService.class);

    public Observable<OutBean> chat(String appId, String currentTime, String param, String token) {
        return yznService.chat(appId, currentTime, param, token).compose(apiResultTransformer());
    }

    public Observable<Object> regiter(String appId, String currentTime, String param, String token) {
        return yznService.register(appId, currentTime, param, token).compose(apiResultTransformer());
    }

    public Observable<Object> bingLi(String appId, String currentTime, String param, String token) {
        return yznService.bingLi(appId, currentTime, param, token).compose(apiResultTransformer());
    }

    public Observable<List<WenJuanOutBean.ItemBean>> wenJuan(String appId, String currentTime, String param, String token) {
        return yznService.wenJuan(appId, currentTime, param, token).compose(apiResultTransformer());
    }

    public static <T> ObservableTransformer<ApiResult<T>, T> apiResultTransformer() {
        return upstream -> upstream
                .onErrorResumeNext(httpErrorProcessor())
                .flatMap(apiResultMapper());
    }

    @NonNull
    private static <T> Function<Throwable, ObservableSource<? extends ApiResult<T>>> httpErrorProcessor() {
        return throwable -> {
//                if (throwable instanceof HttpException
//                        && ((HttpException) throwable).code() >= 500) {
//                    HttpException error = (HttpException) throwable;
//                    ResponseBody body = error.response().errorBody();
//                    String resultJson;
//                    if (body != null) {
//                        resultJson = body.string();
//                        Type type = new TypeToken<ApiResult<T>>() {
//                        }.getType();
//                        ApiResult<T> result = Serializer.getInstance().deserialize(resultJson, type);
//                        return Observable.just(result);
//                    }
//                }
            return Observable.error(new ApiException("网络繁忙"));
        };
    }

    @NonNull
    private static <T> Function<ApiResult<T>, Observable<T>> apiResultMapper() {
        return result -> {
            if (result.getCode() == 0) {
                if (result.getData() == null) {
                    TypeToken<T> typeToken = new TypeToken<T>() {
                    };
                    Type type = typeToken.getType();
                    T t;
                    try {
                        t = (T) new ArrayList<>();
                    } catch (Throwable e) {
                        t = Serializer.getInstance().deserialize("{}", type);
                    }
                    return Observable.just(t);
                }
                return Observable.just(result.getData());
            }
            return Observable.error(new ApiException(result.getMessage(), result.getCode()));
        };
    }

}
