package com.example.han.referralproject.homepage;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.BuildConfig;
import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.RecycleBaseFragment;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.lib_widget.EclipseImageView;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import tech.linjiang.pandora.Pandora;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/7/29 16:50
 * created by:gzq
 * description:主页第1页
 */
public class NewMain1Fragment extends RecycleBaseFragment implements View.OnClickListener {

    private TextView mClock;
    private ImageView mImageWeather;
    /**
     * 8℃
     */
    private TextView mTemperature;
    /**
     * 晴
     */
    private TextView mWeather;
    /**
     * 12月18日
     */
    private TextView mGregorianCalendar;
    /**
     * 农历十一月初一
     */
    private TextView mLunarCalendar;
    /**
     * 星期一
     */
    private TextView mWeekToday;
    private LinearLayout mLlDateAndWeek;
    private EclipseImageView mIvHealthMeasure;
    private EclipseImageView mIvHealthDialyTask;
    private EclipseImageView mIvHealthCallFamily;

    @Override
    protected int initLayout() {
        return R.layout.fragment_newmain1;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mClock = view.findViewById(R.id.clock);
        mClock.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/DINEngschrift-Alternate.otf"));
        mImageWeather = view.findViewById(R.id.image_weather);
        mTemperature = view.findViewById(R.id.temperature);
        mWeather = view.findViewById(R.id.weather);
        mGregorianCalendar = view.findViewById(R.id.gregorian_calendar);
        mLunarCalendar = view.findViewById(R.id.lunar_calendar);
        mWeekToday = view.findViewById(R.id.weekToday);
        mLlDateAndWeek = view.findViewById(R.id.ll_date_and_week);
        mClock.setOnClickListener(this);
        mImageWeather.setOnClickListener(this);
        mTemperature.setOnClickListener(this);
        mWeather.setOnClickListener(this);
        mGregorianCalendar.setOnClickListener(this);
        mLunarCalendar.setOnClickListener(this);
        mWeekToday.setOnClickListener(this);
        mLlDateAndWeek.setOnClickListener(this);
        mIvHealthMeasure = view.findViewById(R.id.iv_health_measure);
        mIvHealthMeasure.setOnClickListener(this);
        mIvHealthDialyTask = view.findViewById(R.id.iv_health_dialy_task);
        mIvHealthDialyTask.setOnClickListener(this);
        mIvHealthCallFamily = view.findViewById(R.id.iv_health_call_family);
        mIvHealthCallFamily.setOnClickListener(this);
        observeTimeAndWeather();
    }

    private void observeTimeAndWeather() {
        MyApplication.getInstance().timeData.observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(@Nullable String[] result) {
                if (isAdded() && result.length == 4) {
                    mClock.setText(result[0]);
                    mGregorianCalendar.setText(result[1]);
                    mLunarCalendar.setText(result[2]);
                    mWeekToday.setText(result[3]);
                }
            }
        });

        MyApplication.getInstance().weatherData.observe(this, new Observer<HomepageWeatherBean>() {
            @Override
            public void onChanged(@Nullable HomepageWeatherBean homepageWeatherBean) {
                handleWeatherResult(homepageWeatherBean);
            }
        });
    }

    private void handleWeatherResult(HomepageWeatherBean homepageWeatherBean) {
        List<HomepageWeatherBean.ResultsBean> results = homepageWeatherBean.getResults();
        if (results != null && results.size() != 0) {
            HomepageWeatherBean.ResultsBean resultsBean = results.get(0);
            if (resultsBean != null) {
                HomepageWeatherBean.ResultsBean.NowBean now = resultsBean.getNow();
                if (now != null) {
                    String temperature = now.getTemperature();
                    String text = now.getText();
                    mTemperature.setText(temperature + "℃");
                    mWeather.setText(text);
                    switch (text) {
                        case "晴":
                            mImageWeather.setImageResource(R.drawable.weather_sunny_light);
                            break;
                        case "多云":
                            mImageWeather.setImageResource(R.drawable.weather_cloudy);
                            break;
                        case "晴间多云":
                            mImageWeather.setImageResource(R.drawable.weather_partly_cloudy_light);
                            break;
                        case "大部多云":
                            mImageWeather.setImageResource(R.drawable.weather_mostly_cloudy_light);
                            break;
                        case "阴":
                            mImageWeather.setImageResource(R.drawable.weather_overcast);
                            break;
                        case "阵雨":
                            mImageWeather.setImageResource(R.drawable.weather_shower);
                            break;
                        case "雷阵雨":
                            mImageWeather.setImageResource(R.drawable.weather_thundershower);
                            break;
                        case "雷阵雨伴有冰雹":
                            mImageWeather.setImageResource(R.drawable.weather_thundershower_with_hail);
                            break;
                        case "小雨":
                            mImageWeather.setImageResource(R.drawable.weather_light_rain);
                            break;
                        case "中雨":
                            mImageWeather.setImageResource(R.drawable.weather_moderate_rain);
                            break;
                        case "大雨":
                            mImageWeather.setImageResource(R.drawable.weather_heavy_rain);
                            break;
                        case "暴雨":
                            mImageWeather.setImageResource(R.drawable.weather_storm);
                            break;
                        case "大暴雨":
                            mImageWeather.setImageResource(R.drawable.weather_heavy_storm);
                            break;
                        case "特大暴雨":
                            mImageWeather.setImageResource(R.drawable.weather_severe_storm);
                            break;
                        case "冻雨":
                            mImageWeather.setImageResource(R.drawable.weather_ice_rain);
                            break;
                        case "雨夹雪":
                            mImageWeather.setImageResource(R.drawable.weather_sleet);
                            break;
                        case "阵雪":
                            mImageWeather.setImageResource(R.drawable.weather_snow_flurry);
                            break;
                        case "小雪":
                            mImageWeather.setImageResource(R.drawable.weather_light_snow);
                            break;
                        case "中雪":
                            mImageWeather.setImageResource(R.drawable.weather_moderate_snow);
                            break;
                        case "大雪":
                            mImageWeather.setImageResource(R.drawable.weather_heavy_snow);
                            break;
                        case "暴雪":
                            mImageWeather.setImageResource(R.drawable.weather_snowstorm);
                            break;
                        case "浮尘":
                            mImageWeather.setImageResource(R.drawable.weather_dust);
                            break;
                        case "扬沙":
                            mImageWeather.setImageResource(R.drawable.weather_sand);
                            break;
                        case "沙尘暴":
                            mImageWeather.setImageResource(R.drawable.weather_duststorm);
                            break;
                        case "强沙尘暴":
                            mImageWeather.setImageResource(R.drawable.weather_sandstorm);
                            break;
                        case "雾":
                            mImageWeather.setImageResource(R.drawable.weather_foggy);
                            break;
                        case "霾":
                            mImageWeather.setImageResource(R.drawable.weather_haze);
                            break;
                        case "风":
                            mImageWeather.setImageResource(R.drawable.weather_windy);
                            break;
                        case "大风":
                            mImageWeather.setImageResource(R.drawable.weather_blustery);
                            break;
                        case "飓风":
                            mImageWeather.setImageResource(R.drawable.weather_hurricane);
                            break;
                        case "热带风暴":
                            mImageWeather.setImageResource(R.drawable.weather_tropical_storm);
                            break;
                        case "龙卷风":
                            mImageWeather.setImageResource(R.drawable.weather_tornado);
                            break;
                        case "未知":
                            mImageWeather.setImageResource(R.drawable.weather_unknow);
                            break;
                        default:
                            break;

                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.clock) {
        } else if (i == R.id.image_weather) {
            if (BuildConfig.DEBUG) {
                Pandora.get().open();
            }

        } else if (i == R.id.temperature) {
        } else if (i == R.id.weather) {
        } else if (i == R.id.gregorian_calendar) {
        } else if (i == R.id.lunar_calendar) {
        } else if (i == R.id.weekToday) {
        } else if (i == R.id.ll_date_and_week) {
        } else if (i == R.id.iv_health_measure) {
            if (UserSpHelper.isNoNetwork()) {
                Routerfit.register(AppRouter.class).skipMeasureChooseDeviceActivity(false, null, "");
                return;
            }
            Routerfit.register(AppRouter.class)
                    .getUserProvider()
                    .getUserEntity()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new DefaultObserver<UserEntity>() {
                        @Override
                        public void onNext(UserEntity userEntity) {
                            if (TextUtils.isEmpty(userEntity.sex) || TextUtils.isEmpty(userEntity.birthday)) {
                                ToastUtils.showShort("请先去个人中心完善性别和年龄信息");
                                MLVoiceSynthetize.startSynthesize(
                                        getActivity().getApplicationContext(),
                                        "请先去个人中心完善性别和年龄信息");
                            } else {
                                Routerfit.register(AppRouter.class).skipServicePackageActivity(false);
                            }
                        }
                    });


        } else if (i == R.id.iv_health_dialy_task) {
            if (UserSpHelper.isNoNetwork()) {
                ToastUtils.showShort("请使用有网模式登录");
                return;
            }
            Routerfit.register(AppRouter.class).skipSymptomCheckActivity();

        } else if (i == R.id.iv_health_call_family) {
            if (UserSpHelper.isNoNetwork()) {
                ToastUtils.showShort("请使用有网模式登录");
                return;
            }
            Routerfit.register(AppRouter.class).skipDoctorAskGuideActivity();

        } else {
        }
    }
}