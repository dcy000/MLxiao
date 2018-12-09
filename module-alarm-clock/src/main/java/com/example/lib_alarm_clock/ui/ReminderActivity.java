package com.example.lib_alarm_clock.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lib_alarm_clock.AlarmHelper;
import com.example.lib_alarm_clock.R;
import com.example.lib_alarm_clock.R2;
import com.example.lib_alarm_clock.service.AlarmAPI;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.Handlers;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class ReminderActivity extends ToolbarBaseActivity {

    private static final String TAG = "ReminderActivity";

    @BindView(R2.id.tv_alarm_reminder_content)
    TextView tvContent;
    @BindView(R2.id.iv_alarm_medical)
    ImageView ivMedical;
    @BindView(R2.id.tv_btn_ignore)
    TextView tvIgnore;
    @BindView(R2.id.tv_btn_confirm)
    TextView tvConfirm;
    public Unbinder mUnbinder;

    private String mContent;
    private UserInfoBean user;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_reminder;
    }

    @Override
    protected boolean isShowToolbar() {
        return false;
    }

    @Override
    public void initParams(Intent intentArgument) {
        user = Box.getSessionManager().getUser();
        mContent = intentArgument.getStringExtra(AlarmHelper.CONTENT);
        int hourOfDay = intentArgument.getIntExtra(AlarmHelper.HOUR_OF_DAY, 0);
        int minute = intentArgument.getIntExtra(AlarmHelper.MINUTE, 0);

        if (TextUtils.isEmpty(mContent)) {
            mContent = "主人,该吃药了！";
        } else {
            if (!mContent.startsWith("主人")) {
                mContent = "主人," + mContent;
            }
        }
        UserInfoBean user = Box.getSessionManager().getUser();
        mContent = user.bname + mContent;
    }

    @Override
    public void initView() {
        mUnbinder = ButterKnife.bind(this);

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

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }

    private int mAlarmCount;

    private Runnable mAlarm = new Runnable() {
        @Override
        public void run() {
            if (mAlarmCount < 5) {
                mAlarmCount++;
                MLVoiceSynthetize.startSynthesize(mContent, new SynthesizerListener() {
                    @Override
                    public void onSpeakBegin() {

                    }

                    @Override
                    public void onBufferProgress(int i, int i1, int i2, String s) {

                    }

                    @Override
                    public void onSpeakPaused() {

                    }

                    @Override
                    public void onSpeakResumed() {

                    }

                    @Override
                    public void onSpeakProgress(int i, int i1, int i2) {

                    }

                    @Override
                    public void onCompleted(SpeechError speechError) {
                        Handlers.runOnUiThread(mAlarm);
                    }

                    @Override
                    public void onEvent(int i, int i1, int i2, Bundle bundle) {

                    }
                });
            } else {
                mAlarmCount = 0;
            }
        }
    };

    private static final int WAKELOCK_TIMEOUT = 30 * 1000;

    private PowerManager.WakeLock mWakeLock;

    @OnClick(R2.id.tv_btn_ignore)
    public void onTvBtnIgnoreClicked() {
        String content = getIntent().getStringExtra(AlarmHelper.CONTENT);

        Box.getRetrofit(AlarmAPI.class)
                .addEatMedicalRecord(user.bname, content, Calendar.getInstance().getTimeInMillis(), "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        finish();
                    }
                })
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                    }
                });
    }

    @OnClick(R2.id.tv_btn_confirm)
    public void onTvBtnConfirmClicked() {
        String content = getIntent().getStringExtra(AlarmHelper.CONTENT);
        Box.getRetrofit(AlarmAPI.class)
                .addEatMedicalRecord(user.bname, content, Calendar.getInstance().getTimeInMillis(), "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        finish();
                    }
                })
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                    }
                });
    }

    @SuppressLint("InvalidWakeLockTag")
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