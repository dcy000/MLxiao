package com.example.han.referralproject.network;

import android.support.annotation.NonNull;

import com.example.han.referralproject.bean.GuardianInfo;
import com.example.han.referralproject.bean.HealthRecordBean;
import com.example.han.referralproject.health.intelligentdetection.entity.ApiResponse;
import com.example.han.referralproject.homepage.HomepageWeatherBean;
import com.example.han.referralproject.yizhinang.OutBean;
import com.example.lenovo.rto.http.API;
import com.gcml.common.RetrofitHelper;
import com.gcml.common.http.ApiException;
import com.gcml.common.http.ApiResult;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Serializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/10/8 10:05
 * created by:gzq
 * description:TODO
 */
public class AppRepository {
    /**
     * 天气Api
     */
    private static String TIANQI_NOW_WEATHER_URL = "https://api.seniverse.com/v3/weather/now.json";

    private static String TIANQI_API_SECRET_KEY = "rodu3msnpwbpzosf";

    private static AppServer healthMeasureServer = RetrofitHelper.service(AppServer.class);

    public static Observable<HomepageWeatherBean> getWeather(String city) {
        Timber.i("天气接口被调用");
        return healthMeasureServer
                .getWeather(TIANQI_API_SECRET_KEY, city, "zh-Hans", "c");
    }

    public static Observable<List<GuardianInfo>> getGuardians(String userId) {
        return healthMeasureServer.getGuardians(userId).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 获取记录列表
     *
     * @param recordId
     * @param userId
     * @return
     */
    public Observable<List<HealthRecordBean>> getHealthRecordList(String recordId, String userId) {
        return healthMeasureServer.getHealthRecordList(recordId, userId).compose(RxUtils.apiResultTransformer());
    }


    public static Observable<OutBean> chat(String appId, String currentTime, String param, String token) {
        return healthMeasureServer.chat(appId, currentTime, param, token).compose(apiResultTransformer());
    }

    public static <T> ObservableTransformer<ApiResult<T>, T> apiResultTransformer() {
        return new ObservableTransformer<ApiResult<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<ApiResult<T>> upstream) {
                return upstream
                        .onErrorResumeNext(httpErrorProcessor())
                        .flatMap(apiResultMapper());
            }
        };
    }

    @NonNull
    private static <T> Function<Throwable, ObservableSource<? extends ApiResult<T>>> httpErrorProcessor() {
        return new Function<Throwable, ObservableSource<? extends ApiResult<T>>>() {
            @Override
            public ObservableSource<? extends ApiResult<T>> apply(Throwable throwable) throws Exception {
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
            }
        };
    }

    @NonNull
    private static <T> Function<ApiResult<T>, Observable<T>> apiResultMapper() {
        return new Function<ApiResult<T>, Observable<T>>() {
            @Override
            public Observable<T> apply(ApiResult<T> result) {
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
            }
        };
    }

}
