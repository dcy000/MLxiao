package com.example.han.referralproject.network;

import com.example.han.referralproject.bean.GuardianInfo;
import com.example.han.referralproject.homepage.HomepageWeatherBean;
import com.example.han.referralproject.recyclerview.Docter;
import com.gcml.common.http.ApiResult;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
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

    @GET("ZZB/api/guardian/user/{userId}/guardians/")
    Observable<ApiResult<List<GuardianInfo>>> getGuardians(@Path("userId") String userId);

}
