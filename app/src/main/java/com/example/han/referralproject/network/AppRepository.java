package com.example.han.referralproject.network;

import com.example.han.referralproject.homepage.HomepageWeatherBean;
import com.gcml.common.repository.IRepositoryHelper;
import com.gcml.common.repository.RepositoryApp;
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

    private static IRepositoryHelper mRepositoryHelper = RepositoryApp.INSTANCE.repositoryComponent().repositoryHelper();
    private static AppServer healthMeasureServer = mRepositoryHelper.retrofitService(AppServer.class);
    public static Observable<HomepageWeatherBean>  getWeather(String city){
        return healthMeasureServer
                .getWeather(TIANQI_API_SECRET_KEY,city,"zh-Hans","c");
    }
}
