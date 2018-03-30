package com.example.han.referralproject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.SystemClock;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.ChooseLoginTypeActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.new_music.MusicService;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.UpdateAppManager;
import com.example.han.referralproject.util.WiFiUtil;

import java.util.ArrayList;

import cn.jzvd.JZVideoPlayerStandard;

public class WelcomeActivity extends BaseActivity {

    private static final String TAG = "afirez";

    private Chronometer ch;

    public static final String VEDIO_URL = "http://oyptcv2pb.bkt.clouddn.com/abc_1521797390144";
    public static final String IMAGE_URL = "http://oyptcv2pb.bkt.clouddn.com/abc_1521797325763";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        playVideo();
//        checkVersion();
    }

    private void checkVersion() {
        NetworkApi.getVersionInfo(new NetworkManager.SuccessCallback<VersionInfoBean>() {
            @Override
            public void onSuccess(VersionInfoBean response) {
                try {
                    if (response != null && response.vid > getPackageManager().getPackageInfo(WelcomeActivity.this.getPackageName(), 0).versionCode) {
                        new UpdateAppManager(WelcomeActivity.this).showNoticeDialog(response.url);
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
                                    if (TextUtils.isEmpty(MyApplication.getInstance().userId)) {
                                        Intent intent = new Intent(getApplicationContext(), ChooseLoginTypeActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }
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
                            if (TextUtils.isEmpty(MyApplication.getInstance().userId)) {
                                Intent intent = new Intent(getApplicationContext(), ChooseLoginTypeActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
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

    @Override
    protected void onPause() {
        JZVideoPlayerStandard.releaseAllVideos();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayerStandard.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    private void playVideo() {
        boolean isFirstIn = LocalShared.getInstance(this).getIsFirstIn();
        if (false) {
            JZVideoPlayerStandard.startFullscreen(this, MyVideoPlayer.class, VEDIO_URL, "迈联智慧");
        } else {
            checkVersion();
        }
    }

    public static class MyVideoPlayer extends JZVideoPlayerStandard {
        private WelcomeActivity mWelcomeActivity;

        public MyVideoPlayer(Context context) {
            super(context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.ml_player_splash;
        }

        @Override
        public void init(Context context) {
            super.init(context);
            findViewById(R.id.common_tv_action).setOnClickListener(this);
            backButton.setVisibility(GONE);
            batteryTimeLayout.setVisibility(GONE);
            try {
                mWelcomeActivity = (WelcomeActivity) context;
            } catch (Throwable e) {
                mWelcomeActivity = null;
                e.printStackTrace();
            }

            backButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyVideoPlayer.this.onClick(v);
                    if (mWelcomeActivity != null) {
                        mWelcomeActivity.onVideoPlayedComplete();
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if (v.getId() == R.id.common_tv_action) {
                backPress();
            }
        }

        @Override
        public void onStateAutoComplete() {
            super.onStateAutoComplete();
            backPress();
            if (mWelcomeActivity != null) {
                mWelcomeActivity.onVideoPlayedComplete();
            }
        }
    }

    private void onVideoPlayedComplete() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        LocalShared.getInstance(this).setIsFirstIn(false);
        checkVersion();
    }

    @Override
    protected void onResume() {
        setDisableGlobalListen(true);
        setEnableListeningLoop(false);
        super.onResume();
    }
}
