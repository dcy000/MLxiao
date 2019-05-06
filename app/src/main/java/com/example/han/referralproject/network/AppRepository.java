package com.example.han.referralproject.network;

import com.example.han.referralproject.bean.GuardianInfo;
import com.example.han.referralproject.bean.HealthRecordBean;
import com.example.han.referralproject.health.intelligentdetection.entity.ApiResponse;
import com.example.han.referralproject.homepage.HomepageWeatherBean;
import com.example.han.referralproject.yizhinang.OutBean;
import com.example.lenovo.rto.http.API;
import com.gcml.common.RetrofitHelper;
import com.gcml.common.http.ApiResult;
import com.gcml.common.utils.RxUtils;

import java.util.List;

import io.reactivex.Observable;
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
        return healthMeasureServer.chat(appId, currentTime, param, token).compose(RxUtils.apiResultTransformer());
    }

}
