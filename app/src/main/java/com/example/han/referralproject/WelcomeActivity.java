package com.example.han.referralproject;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;

import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.LoginActivity;
import com.example.han.referralproject.application.MyApplication;

public class WelcomeActivity extends BaseActivity {

    Chronometer ch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        speak(R.string.tips_splash);

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
                    if (TextUtils.isEmpty(MyApplication.getInstance().userId)){
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
}
