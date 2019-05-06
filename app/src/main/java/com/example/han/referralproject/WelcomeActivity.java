package com.example.han.referralproject;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.Chronometer;

import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.new_music.MusicService;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.common.recommend.bean.get.VersionInfoBean;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.network.NetUitls;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import java.util.ArrayList;

@Route(path = "/app/welcome/activity")
public class WelcomeActivity extends AppCompatActivity {

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
        //启动音乐服务
        if (!isWorked("com.example.han.referralproject.MusicService")) {
            startService(new Intent(this, MusicService.class));
        }
        if (!NetUitls.isWifiConnected()) {
            Routerfit.register(AppRouter.class).skipWifiConnectActivity(true);
            finish();
            return;
        }
        playVideo();
    }

    private void checkVersion() {
        NetworkApi.getVersionInfo(new NetworkManager.SuccessCallback<VersionInfoBean>() {
            @Override
            public void onSuccess(VersionInfoBean response) {
                try {
                    if (response != null && response.vid > getPackageManager().getPackageInfo(WelcomeActivity.this.getPackageName(), 0).versionCode) {
                        Routerfit.register(AppRouter.class).getAppUpdateProvider().showDialog(WelcomeActivity.this, response.url);
                    } else {
                        ch = findViewById(R.id.chronometer);
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
                                    Routerfit.register(AppRouter.class).skipAuthActivity();
                                    finish();
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ch = findViewById(R.id.chronometer);
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
                            Routerfit.register(AppRouter.class).skipAuthActivity();
                            finish();
                        }
                    }
                });
            }
        });
    }

    //判断服务是否已经启动
    private boolean isWorked(String className) {
        ActivityManager myManager = (ActivityManager) MyApplication.getInstance().getSystemService(
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


    private void playVideo() {
        boolean isFirstIn = LocalShared.getInstance(this).getIsFirstIn();
        if (false) {
            Routerfit.register(AppRouter.class).skipNormalVideoPlayActivity(
                    null, VEDIO_URL, "迈联智慧",
                    new ActivityCallback() {
                        @Override
                        public void onActivityResult(int result, Object data) {
                            if (result == Activity.RESULT_OK) {
                                if (data == null) return;
                                if (data.toString().equals("pressed_button_skip")) {
                                    onVideoPlayedComplete();
                                } else if (data.toString().equals("video_play_end")) {
                                    onVideoPlayedComplete();
                                }
                            } else if (result == Activity.RESULT_CANCELED) {
                            }
                        }
                    });
        } else {
            checkVersion();
        }
    }

    private void onVideoPlayedComplete() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        LocalShared.getInstance(this).setIsFirstIn(false);
        checkVersion();
    }
}
