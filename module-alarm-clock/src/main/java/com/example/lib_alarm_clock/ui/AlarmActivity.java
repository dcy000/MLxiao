package com.example.lib_alarm_clock.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.constraint.ConstraintLayout;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.lib_alarm_clock.AlarmHelper;
import com.example.lib_alarm_clock.R;
import com.example.lib_alarm_clock.R2;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AlarmActivity extends ToolbarBaseActivity {
    private static final String TAG = "AlarmActivity";

    @BindView(R2.id.cl_alarm_wake)
    ConstraintLayout clAlarmWake;
    @BindView(R2.id.alarm_tv_title)
    TextView tvTitle;
    @BindView(R2.id.alarm_tv_time)
    TextView tvTime;
    @BindView(R2.id.alarm_tv_content)
    TextView tvContent;
    @BindView(R2.id.alarm_btn_dismiss)
    Button btnDismiss;
    public Unbinder mUnbinder;

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
                MLVoiceSynthetize.startSynthesize(mContent);
                mHandler.postDelayed(this, 3000);
            } else {
                mAlarmCount = 0;
            }
        }
    };
    private int hourOfDay;
    private int minute;


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_alarm;
    }

    @Override
    public void initParams(Intent intentArgument) {
        mContent = intentArgument.getStringExtra(AlarmHelper.CONTENT);
        hourOfDay =intentArgument.getIntExtra(AlarmHelper.HOUR_OF_DAY, 0);
        minute = intentArgument.getIntExtra(AlarmHelper.MINUTE, 0);
        String tone = intentArgument.getStringExtra(AlarmHelper.TONE);
    }

    @Override
    public void initView() {
        mUnbinder = ButterKnife.bind(this);

        mContent = "主人,该吃药啦";
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

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }

    @OnClick(R2.id.alarm_btn_dismiss)
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

    @OnClick(R2.id.cl_alarm_wake)
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
        mUnbinder.unbind();
        super.onDestroy();
    }
}
