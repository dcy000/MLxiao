package com.medlink.danbogh.alarm;

import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.Handlers;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ReminderActivity extends BaseActivity {

    private static final String TAG = "ReminderActivity";

    @BindView(R.id.tv_alarm_reminder_content)
    TextView tvContent;
    @BindView(R.id.iv_alarm_medical)
    ImageView ivMedical;
    @BindView(R.id.tv_btn_ignore)
    TextView tvIgnore;
    @BindView(R.id.tv_btn_confirm)
    TextView tvConfirm;
    public Unbinder mUnbinder;

    private String mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        mUnbinder = ButterKnife.bind(this);

        mContent = getIntent().getStringExtra(AlarmHelper.CONTENT);
        int hourOfDay = getIntent().getIntExtra(AlarmHelper.HOUR_OF_DAY, 0);
        int minute = getIntent().getIntExtra(AlarmHelper.MINUTE, 0);

        if (TextUtils.isEmpty(mContent)) {
            mContent = "该吃药了！";
        } else {
            if (!mContent.startsWith("主人")) {
                mContent = "" + mContent;
            }
        }

        mContent = LocalShared.getInstance(this).getUserName() + mContent;
        tvContent.setText(mContent);
        Handlers.runOnUiThread(mAlarm);

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

    private int mAlarmCount;

    private Runnable mAlarm = new Runnable() {
        @Override
        public void run() {
            if (mAlarmCount < 5) {
                mAlarmCount++;
                speak(mContent);
            } else {
                mAlarmCount = 0;
            }
        }
    };

    @Override
    protected void onActivitySpeakFinish() {
        Handlers.runOnUiThread(mAlarm);
    }

    private static final int WAKELOCK_TIMEOUT = 30 * 1000;

    private PowerManager.WakeLock mWakeLock;

    @OnClick(R.id.tv_btn_ignore)
    public void onTvBtnIgnoreClicked() {
        String content = getIntent().getStringExtra(AlarmHelper.CONTENT);
        NetworkApi.addEatMedicalRecord(
                UserSpHelper.getUserName()
                , content,
                "0",
                new NetworkManager.SuccessCallback<Object>() {
                    @Override
                    public void onSuccess(Object response) {
                        finish();
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        finish();
                    }
                });
    }

    @OnClick(R.id.tv_btn_confirm)
    public void onTvBtnConfirmClicked() {
        String content = getIntent().getStringExtra(AlarmHelper.CONTENT);
        NetworkApi.addEatMedicalRecord(
                UserSpHelper.getUserName(),
                content, "1",
                new NetworkManager.SuccessCallback<Object>() {
                    @Override
                    public void onSuccess(Object response) {
                        finish();
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
//                        ToastUtils.showShort(message);
                        finish();
                    }
                });
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
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mAlarm != null) {
            Handlers.ui().removeCallbacks(mAlarm);
        }
        super.onDestroy();
    }
}