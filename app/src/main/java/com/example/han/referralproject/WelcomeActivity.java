package com.example.han.referralproject;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Chronometer;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.ChooseLoginTypeActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.facerecognition.AuthenticationActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.new_music.MusicService;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.UpdateAppManager;
import com.example.han.referralproject.util.WiFiUtil;
import com.medlink.danbogh.signin.SignInActivity;

import java.util.ArrayList;

public class WelcomeActivity extends BaseActivity {

    private Chronometer ch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //启动音乐服务
        if (!isWorked("com.example.han.referralproject.MusicService")) {
            startService(new Intent(this, MusicService.class));
        }
        if (!WiFiUtil.getInstance(getApplicationContext()).isNetworkEnabled(this)) {//网络没有连接，这跳转到WiFi页面
            Intent mIntent = new Intent(WelcomeActivity.this, WifiConnectActivity.class);
            mIntent.putExtra("is_first_wifi", true);
            startActivity(mIntent);
            finish();
        }


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
                                    //获取所有账号
//                                    String[] accounts = LocalShared.getInstance(WelcomeActivity.this).getAccounts();
//                                    if (!TextUtils.isEmpty(MyApplication.getInstance().userId)) {
//                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                        startActivity(intent);
//                                    } else if (accounts != null) {
//                                        startActivity(new Intent(WelcomeActivity.this, AuthenticationActivity.class)
//                                                .putExtra("from", "Welcome"));
//                                    } else {
//                                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
//                                        startActivity(intent);
//                                    }


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
                                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
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
}
