package com.example.han.referralproject.homepage;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.cc.CCHealthMeasureActions;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.base.RecycleBaseFragment;
import com.gcml.lib_utils.data.LunarUtils;
import com.gcml.lib_utils.data.TimeUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.lib_utils.thread.ThreadUtils;
import com.gcml.lib_widget.EclipseImageView;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.call2.NimCallActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
        synchroSystemTime();
        getLocation();
    }

    private void getLocation() {
        //TODO:打正式包的时候打开该注释
        WeatherUtils.getInstance().initLocation(UtilsManager.getApplication());
        WeatherUtils.getInstance().setOnLocationResultListener(new WeatherUtils.LocationResult() {
            @Override
            public void onResult(String city, String county) {
                getWeather(city);
            }
        });
//        getWeather("杭州");
    }

    private void getWeather(String address) {
        WeatherUtils.getInstance().setOnWeatherResultListener(new WeatherUtils.WeatherResult() {
            @Override
            public void onResult(HomepageWeatherBean homepageWeatherBean) {
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
            public void onError() {

            }
        });
        ThreadUtils.executeByCachedAtFixRate(new ThreadUtils.SimpleTask<Void>() {
            @Nullable
            @Override
            public Void doInBackground() {
                WeatherUtils.getInstance().requestWeatherData(address);
                return null;
            }

            @Override
            public void onSuccess(@Nullable Void result) {

            }
        }, 1, TimeUnit.HOURS);
    }

    private void synchroSystemTime() {
        String[] results = new String[4];
        ThreadUtils.executeByCachedAtFixRate(new ThreadUtils.SimpleTask<String[]>() {
            @Nullable
            @Override
            public String[] doInBackground() {

                Calendar instance = Calendar.getInstance();
                results[0] = TimeUtils.date2String(instance.getTime(), new SimpleDateFormat("HH:mm"));
                int month = instance.get(Calendar.MONTH) + 1;
                int day = instance.get(Calendar.DATE);
                results[1] = month + "月" + day + "日";
                LunarUtils lunarUtils = new LunarUtils(instance);
                results[2] = lunarUtils.toString();
                results[3] = TimeUtils.getChineseWeek(instance.getTime());
                return results;
            }

            @Override
            public void onSuccess(@Nullable String[] result) {
                if (isAdded()) {
                    mClock.setText(result[0]);
                    mGregorianCalendar.setText(result[1]);
                    mLunarCalendar.setText(result[2]);
                    mWeekToday.setText(result[3]);
                }
            }
        }, 10000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.clock:
                break;
            case R.id.image_weather:
                break;
            case R.id.temperature:
                break;
            case R.id.weather:
                break;
            case R.id.gregorian_calendar:
                break;
            case R.id.lunar_calendar:
                break;
            case R.id.weekToday:
                break;
            case R.id.ll_date_and_week:
                break;
            case R.id.iv_health_measure:
                CC.obtainBuilder("com.gcml.auth.face.signin")
                        .addParam("skip", true)
                        .addParam("currentUser", false)
                        .build()
                        .callAsyncCallbackOnMainThread(new IComponentCallback() {
                            @Override
                            public void onResult(CC cc, CCResult result) {
                                boolean skip = "skip".equals(result.getErrorMessage());
                                if (result.isSuccess() || skip) {
                                    if (skip) {
                                        CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true);
                                        return;
                                    }
                                    CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                                } else {
                                    ToastUtils.showShort(result.getErrorMessage());
                                }
                            }
                        });
                break;
            case R.id.iv_health_dialy_task:
//                startActivity(new Intent(getContext(), SlowDiseaseManagementActivity.class));
//                startActivity(new Intent(getContext(), OlderHealthManagementSerciveActivity.class));
                CCResult result;
                Observable<UserEntity> rxUser;
                result = CC.obtainBuilder("com.gcml.auth.getUser").build().call();
                rxUser = result.getDataItem("data");
                rxUser.subscribeOn(Schedulers.io())
                        .as(RxUtils.autoDisposeConverter(this))
                        .subscribe(new DefaultObserver<UserEntity>() {
                            @Override
                            public void onNext(UserEntity user) {
                                if (TextUtils.isEmpty(user.height) || TextUtils.isEmpty(user.weight)) {
                                    ToastUtils.showShort("请先去个人中心完善体重和身高信息");
                                    MLVoiceSynthetize.startSynthesize(
                                            getActivity().getApplicationContext(),
                                            "请先去个人中心完善体重和身高信息");
                                } else {
                                    CC.obtainBuilder("com.gcml.task.isTask")
                                            .build()
                                            .callAsync(new IComponentCallback() {
                                                @Override
                                                public void onResult(CC cc, CCResult result) {
                                                    if (result.isSuccess()) {
                                                        CC.obtainBuilder("app.component.task").build().callAsync();
                                                    } else {
                                                        CC.obtainBuilder("app.component.task.comply").build().callAsync();
                                                    }
                                                }
                                            });
                                }
                            }
                        });


                break;
            case R.id.iv_health_call_family:
                NimCallActivity.launchNoCheck(getContext(), MyApplication.getInstance().eqid);
                break;
        }
    }
}
