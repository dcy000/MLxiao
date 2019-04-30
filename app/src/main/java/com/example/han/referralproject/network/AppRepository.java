package com.example.han.referralproject.network;

import com.example.han.referralproject.bean.ServicePackageBean;
import com.example.han.referralproject.homepage.HomepageWeatherBean;
import com.example.han.referralproject.tcm.bean.OlderHealthManagementBean;
import com.gcml.common.RetrofitHelper;
import com.gcml.common.data.UserSpHelper;
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

    /**
     * 使购买的套餐生效
     * @param type
     * @param orderId
     * @return
     */
    public static Observable<String> servicePackageEffective(String type, String orderId) {
        return healthMeasureServer.servicePackageEffective(type, orderId, UserSpHelper.getUserId()).compose(RxUtils.apiResultTransformer());
    }

    /**
     * 购买套餐预支付
     * @param price
     * @param des
     * @return
     */
    public static Observable<Object> bugServicePackage(String price, String des){
        return healthMeasureServer.bugServicePackage(UserSpHelper.getUserId(),price,des).compose(RxUtils.apiResultTransformer());
    }

    public static Observable<Object> getOrderStarte(String orderId){
        return healthMeasureServer.getOrderStarte(orderId).compose(RxUtils.apiResultTransformer());
    }

    public static Observable<String> getCallId(String doctorId){
        return healthMeasureServer.getCallId(doctorId).compose(RxUtils.apiResultTransformer());
    }

    public static Observable<OlderHealthManagementBean.DataBean> getHealthManagementForOlder(){
        return healthMeasureServer.getHealthManagementForOlder().compose(RxUtils.apiResultTransformer());
    }

}
