package com.example.han.referralproject.homepage;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.han.referralproject.network.AppRepository;
import com.gcml.common.utils.RxUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;

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
    public void destroy() {
        startLocation();
        OkGo.cancelTag(OkGo.getInstance().getOkHttpClient(), WeatherUtils.this);
        locationResult = null;
    }

    private LocationResult locationResult;

    public void setOnLocationResultListener(LocationResult locationResult) {
        this.locationResult = locationResult;
    }

    public interface LocationResult {
        void onResult(String city, String county);
    }
}
