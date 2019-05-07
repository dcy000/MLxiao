package com.example.han.referralproject.network;

import com.example.han.referralproject.homepage.HomepageWeatherBean;
import com.gcml.common.RetrofitHelper;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.recommend.bean.get.ServicePackageBean;
import com.gcml.common.utils.RxUtils;

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

    /**
     * 查询套餐是否生效
     *
     * @return
     */
    public static Observable<ServicePackageBean> queryServicePackage() {
        return healthMeasureServer.queryServicePackage(UserSpHelper.getUserId()).compose(RxUtils.apiResultTransformer());
    }

}
