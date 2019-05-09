
package com.example.han.referralproject.application;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.example.han.referralproject.BuildConfig;
import com.example.han.referralproject.homepage.HomepageWeatherBean;
import com.example.han.referralproject.network.AppRepository;
import com.gcml.common.AppDelegate;
import com.gcml.common.location.BdLocationHelper;
import com.gcml.common.utils.data.LunarUtils;
import com.gcml.common.utils.data.TimeUtils;
import com.gcml.common.utils.ui.UiUtils;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MyApplication extends Application {
    private static MyApplication mInstance;

    private String city;
    public MutableLiveData<String[]> timeData = new MutableLiveData<>();
    public MutableLiveData<HomepageWeatherBean> weatherData = new MutableLiveData<>();
    public BdLocationHelper bdLocation = new BdLocationHelper();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        AppDelegate.INSTANCE.attachBaseContext(this, base);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        AppDelegate.INSTANCE.onTerminate(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppDelegate.INSTANCE.onCreate(this);

        mInstance = this;

        syncWeatherAndTime();
    }

    private void syncWeatherAndTime() {
        Observable.interval(0, 10, TimeUnit.SECONDS)
                .map(new Function<Long, String[]>() {
                    @Override
                    public String[] apply(Long aLong) throws Exception {
                        String[] results = new String[4];
                        Calendar instance = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
                        results[0] = TimeUtils.date2String(instance.getTime(), new SimpleDateFormat("HH:mm"));
                        int month = instance.get(Calendar.MONTH) + 1;
                        int day = instance.get(Calendar.DATE);
                        results[1] = month + "月" + day + "日";
                        LunarUtils lunarUtils = new LunarUtils(instance);
                        results[2] = lunarUtils.toString();
                        results[3] = TimeUtils.getChineseWeek(instance.getTime());
                        return results;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.observers.DefaultObserver<String[]>() {
                    @Override
                    public void onNext(String[] result) {
                        timeData.setValue(result);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        checkLocation();
    }

    private void checkLocation() {
        if (TextUtils.isEmpty(city)) {
            bdLocation.startLocation(this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new io.reactivex.observers.DefaultObserver<BDLocation>() {
                        @Override
                        public void onNext(BDLocation bdLocation) {
                            city = bdLocation.getCity();
                            getWeather();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            getWeather();
        }
    }

    private void getWeather() {
        //2个小时查询一次天气接口
        Observable.interval(0, 2, TimeUnit.HOURS)
                .flatMap(new Function<Long, ObservableSource<HomepageWeatherBean>>() {
                    @Override
                    public ObservableSource<HomepageWeatherBean> apply(Long aLong) throws Exception {
                        return AppRepository.getWeather(city);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.observers.DefaultObserver<HomepageWeatherBean>() {
                    @Override
                    public void onNext(HomepageWeatherBean homepageWeatherBean) {
                        weatherData.setValue(homepageWeatherBean);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public static MyApplication getInstance() {
        return mInstance;
    }
}
