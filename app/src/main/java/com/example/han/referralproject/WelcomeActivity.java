package com.example.han.referralproject;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;

public class WelcomeActivity extends AppCompatActivity {

    Chronometer ch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

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
                if (SystemClock.elapsedRealtime() - ch.getBase() > 3 * 1000) {
                    ch.stop();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }
}
