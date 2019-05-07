package com.example.han.referralproject.network;

import com.example.han.referralproject.homepage.HomepageWeatherBean;
import com.gcml.common.http.ApiResult;
import com.gcml.common.recommend.bean.get.ServicePackageBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/10/8 10:06
 * created by:gzq
 * description:TODO
 */
public interface AppServer {
    @Headers({"Domain-Name:seniverse"})
    @GET("weather/now.json")
    Observable<HomepageWeatherBean> getWeather(
            @Query("key") String key,
            @Query("location") String address,
            @Query("language") String lang,
            @Query("unit") String unit);

    /**
     * 查询该用户的检测套餐是否生效
     *
     * @param userId
     * @return
     */
    @GET("ZZB/order/judge")
    Observable<ApiResult<ServicePackageBean>> queryServicePackage(
            @Query("userid") String userId
    );
}
