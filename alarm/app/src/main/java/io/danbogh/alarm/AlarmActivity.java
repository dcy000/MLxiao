package io.danbogh.alarm;

import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.han.referralproject.activity.BaseActivity;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class AlarmActivity extends BaseActivity {
    private static final String TAG = "AlarmActivity";

    @BindView(R.id.alarm_tv_title)
    TextView tvTitle;
    @BindView(R.id.alarm_tv_time)
    TextView tvTime;
    @BindView(R.id.alarm_tv_content)
    TextView tvContent;
    @BindView(R.id.alarm_btn_dismiss)
    Button btnDismiss;
    public Unbinder mUnbinder;
    public String mContent;
    private PowerManager.WakeLock mWakeLock;
    private static final int WAKELOCK_TIMEOUT = 30 * 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        mUnbinder = ButterKnife.bind(this);
        mContent = getIntent().getStringExtra(AlarmHelper.CONTENT);
        int hourOfDay = getIntent().getIntExtra(AlarmHelper.HOUR_OF_DAY, 0);
        int minute = getIntent().getIntExtra(AlarmHelper.MINUTE, 0);
        String tone = getIntent().getStringExtra(AlarmHelper.TONE);
        tvContent.setText(mContent);
        tvContent.setText(String.format(Locale.CHINA, "%02d : %02d", hourOfDay, minute));
        speak(mContent);

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

    @OnClick(R.id.alarm_btn_dismiss)
    public void onBtnDismissClicked() {

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

    @Override
    protected void onPause() {
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }
}
