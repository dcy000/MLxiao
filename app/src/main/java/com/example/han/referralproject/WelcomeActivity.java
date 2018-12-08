package com.example.han.referralproject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Chronometer;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.module_login.ui.ChooseLoginTypeActivity;
import com.example.module_setting.UpdateAppManager;
import com.gzq.lib_core.bean.VersionInfoBean;
import com.example.han.referralproject.new_music.MusicService;
import com.example.han.referralproject.util.WiFiUtil;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.service.CommonAPI;
import com.gzq.lib_core.utils.AppUtils;
import com.gzq.lib_core.utils.RxUtils;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class WelcomeActivity extends BaseActivity {

    private static final String TAG = "afirez";

    private Chronometer ch;

    public static final String VEDIO_URL = "http://oyptcv2pb.bkt.clouddn.com/abc_1521797390144";
    public static final String IMAGE_URL = "http://oyptcv2pb.bkt.clouddn.com/abc_1521797325763";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
    }

    private void initContentView() {
        setContentView(R.layout.activity_welcome);
        Log.i(TAG, "onCreate: ");
        //启动音乐服务
        if (!isWorked("com.example.han.referralproject.MusicService")) {
            startService(new Intent(this, MusicService.class));
        }
        if (!WiFiUtil.getInstance(getApplicationContext()).isNetworkEnabled(this)) {//网络没有连接，这跳转到WiFi页面
            Intent mIntent = new Intent(WelcomeActivity.this, WifiConnectActivity.class);
            mIntent.putExtra("is_first_wifi", true);
            startActivity(mIntent);
            finish();
            return;
        }
        checkVersion();
    }

    private void checkVersion() {
        final String userId = Box.getUserId();

        Box.getRetrofit(CommonAPI.class)
                .getAppVersion(AppUtils.getMeta("com.gcml.version") + "")
                .compose(RxUtils.httpResponseTransformer())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<VersionInfoBean>() {
                    @Override
                    public void onNext(VersionInfoBean versionInfoBean) {
                        try {
                            if (versionInfoBean != null && versionInfoBean.vid > AppUtils.getAppInfo().getVersionCode()) {
                                new UpdateAppManager(WelcomeActivity.this).showNoticeDialog(versionInfoBean.url);
                            } else {
                                ch = (Chronometer) findViewById(R.id.chronometer);
                                //设置开始计时时间
                                ch.setBase(SystemClock.elapsedRealtime());
                                //启动计时器
                                ch.start();
                                //为计时器绑定监听事件
                                ch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                                    @Override
                                    public void onChronometerTick(Chronometer ch) {
                                        // 如果从开始计时到现在超过了60s
                                        if (SystemClock.elapsedRealtime() - ch.getBase() > 2 * 1000) {
                                            ch.stop();
                                            if (TextUtils.isEmpty(userId)) {
                                                Intent intent = new Intent(getApplicationContext(), ChooseLoginTypeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                queryUserInfo(userId);
                                            }

                                        }
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        ch = (Chronometer) findViewById(R.id.chronometer);
                        //设置开始计时时间
                        ch.setBase(SystemClock.elapsedRealtime());
                        //启动计时器
                        ch.start();
                        //为计时器绑定监听事件
                        ch.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                            @Override
                            public void onChronometerTick(Chronometer ch) {
                                // 如果从开始计时到现在超过了60s
                                if (SystemClock.elapsedRealtime() - ch.getBase() > 2 * 1000) {
                                    ch.stop();
                                    if (TextUtils.isEmpty(userId)) {
                                        Intent intent = new Intent(getApplicationContext(), ChooseLoginTypeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        queryUserInfo(userId);
                                    }

                                }
                            }
                        });
                    }
                });
    }

    private void queryUserInfo(String userId) {
        Box.getRetrofit(CommonAPI.class)
                .queryUserInfo(userId)
                .compose(RxUtils.httpResponseTransformer(false))
                .doOnNext(new Consumer<UserInfoBean>() {
                    @Override
                    public void accept(UserInfoBean userInfoBean) throws Exception {
                        Box.getSessionManager().setUser(userInfoBean);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new CommonObserver<UserInfoBean>() {
                    @Override
                    public void onNext(UserInfoBean userInfoBean) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        Intent intent = new Intent(getApplicationContext(), ChooseLoginTypeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    //判断服务是否已经启动
    private boolean isWorked(String className) {
        ActivityManager myManager = (ActivityManager) Box.getApp().getSystemService(
                Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(200);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(className)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        setDisableWakeup(true);
        super.onResume();
    }
}
