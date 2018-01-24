package com.example.han.referralproject;

import android.content.Intent;
import android.os.SystemClock;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Chronometer;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.new_music.MusicService;
import com.example.han.referralproject.util.UpdateAppManager;
import com.example.han.referralproject.util.WiFiUtil;
import com.medlink.danbogh.signin.SignInActivity;

public class WelcomeActivity extends BaseActivity {

    private Chronometer ch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //启动音乐服务
        startService(new Intent(this, MusicService.class));
        if (!WiFiUtil.getInstance(getApplicationContext()).isNetworkEnabled(this)) {
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

}
