package com.example.han.referralproject.homepage;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/7/30 12:07
 * created by:gzq
 * description:TODO
 */
public class WeatherUtils {
    private static String TIANQI_NOW_WEATHER_URL = "https://api.seniverse.com/v3/weather/now.json";

    private static String TIANQI_API_SECRET_KEY = "rodu3msnpwbpzosf";

    private static String TIANQI_API_USER_ID = "UF15D0DC7E";
    private LocationClient mLocationClient;
    private static volatile WeatherUtils instance;

    public static WeatherUtils getInstance() {
        if (instance == null) {
            synchronized (WeatherUtils.class) {
                if (instance == null) {
                    instance = new WeatherUtils();
                }
            }
        }
        return instance;
    }

    public void initLocation(Context context) {
        mLocationClient = new LocationClient(context);
        LocationClientOption locOption = new LocationClientOption();
        locOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locOption.setCoorType("bd09ll");
        locOption.setIsNeedAddress(true);
        locOption.setOpenGps(true);
        locOption.setScanSpan(3000);
        mLocationClient.setLocOption(locOption);
        startLocation();
    }

    private void startLocation() {
        if (mListener != null && mLocationClient != null) {
            mLocationClient.registerLocationListener(mListener);
        }
        if (mLocationClient != null && !mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }


    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            stopLocation();
            String province = bdLocation.getProvince();
            String city = bdLocation.getCity();
            String county = bdLocation.getDistrict();
            String street = bdLocation.getStreet();
            String streetNumber = bdLocation.getStreetNumber();
            if (locationResult != null) {
                locationResult.onResult(city, county);
            }
        }
    };

    private void stopLocation() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            mLocationClient.stop();
        }
        if (mListener != null && mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(mListener);
        }
    }

    public void requestWeatherData(String address) {
        OkGo.<String>get(TIANQI_NOW_WEATHER_URL)
                .tag(WeatherUtils.this)
                .params("key", TIANQI_API_SECRET_KEY)
                .params("location", address)
                .params("language", "zh-Hans")
                .params("unit", "c")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        HomepageWeatherBean homepageWeatherBean = new Gson().fromJson(response.body(), HomepageWeatherBean.class);
                        if (weatherResult != null) {
                            if (homepageWeatherBean != null) {
                                weatherResult.onResult(homepageWeatherBean);
                            } else {
                                weatherResult.onError();
                            }
                        }
                    }
                });
    }
    public void destroy(){
        startLocation();
        OkGo.cancelTag(OkGo.getInstance().getOkHttpClient(), WeatherUtils.this);
        locationResult=null;
        weatherResult=null;
    }
    private WeatherResult weatherResult;

    public void setOnWeatherResultListener(WeatherResult weatherResult) {
        this.weatherResult = weatherResult;
    }

    public interface WeatherResult {
        void onResult(HomepageWeatherBean homepageWeatherBean);

        void onError();
    }

    private LocationResult locationResult;

    public void setOnLocationResultListener(LocationResult locationResult) {
        this.locationResult = locationResult;
    }

    public interface LocationResult {
        void onResult(String city, String county);
    }
}
