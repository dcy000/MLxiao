package com.medlink.danbogh.alarm;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

import java.util.Locale;

public class AlarmActivity extends BaseActivity {
    private static final String TAG = "AlarmActivity";

    ConstraintLayout clAlarmWake;
    TextView tvTitle;
    TextView tvTime;
    TextView tvContent;
    Button btnDismiss;

    public String mContent;

    private PowerManager.WakeLock mWakeLock;

    private static final int WAKELOCK_TIMEOUT = 30 * 1000;

    private Handler mHandler = new Handler();

    private int mAlarmCount;

    private Runnable mAlarm = new Runnable() {
        @Override
        public void run() {
            mHandler.removeCallbacks(this);
            mAlarmCount++;
            if (mAlarmCount <= 5) {
                speak(mContent);
                mHandler.postDelayed(this, 3000);
            } else {
                mAlarmCount = 0;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        clAlarmWake = findViewById(R.id.cl_alarm_wake);
        clAlarmWake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClAlarmWakeClicked();
            }
        });
        tvTitle = findViewById(R.id.alarm_tv_title);
        tvTime = findViewById(R.id.alarm_tv_time);
        tvContent = findViewById(R.id.alarm_tv_content);
        btnDismiss= findViewById(R.id.alarm_btn_dismiss);
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnDismissClicked();
            }
        });
        mContent = getIntent().getStringExtra(AlarmHelper.CONTENT);
        int hourOfDay = getIntent().getIntExtra(AlarmHelper.HOUR_OF_DAY, 0);
        int minute = getIntent().getIntExtra(AlarmHelper.MINUTE, 0);
        String tone = getIntent().getStringExtra(AlarmHelper.TONE);
        mContent = "该吃药啦";
        tvContent.setText(mContent);
        tvContent.setText(String.format(Locale.CHINA, "%02d : %02d", hourOfDay, minute));

        mHandler.postDelayed(mAlarm, 1000);

        //Ensure wakelock release
        Runnable releaseWakelock = new Runnable() {
            @Override
            public void run() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
                if (mWakeLock != null && mWakeLock.isHeld()) {
                    mWakeLock.release();
                }
            }
        };
        getWindow().getDecorView().postDelayed(releaseWakelock, WAKELOCK_TIMEOUT);
    }

    public void onBtnDismissClicked() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (mWakeLock == null) {
            mWakeLock = pm.newWakeLock(
                    (PowerManager.FULL_WAKE_LOCK
                            | PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                            | PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);
        }
        if (!mWakeLock.isHeld()) {
            mWakeLock.acquire();
        }
    }

    public void onClAlarmWakeClicked() {
        finish();
    }

    @Override
    protected void onPause() {
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mAlarmCount = 0;
        mHandler.removeCallbacks(mAlarm);
        super.onDestroy();
    }
}
