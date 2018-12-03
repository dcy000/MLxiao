package com.example.lib_alarm_clock.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.lib_alarm_clock.AlarmHelper;
import com.example.lib_alarm_clock.R;
import com.example.lib_alarm_clock.service.AlarmAPI;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.AlreadyYuyue;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.gzq.lib_core.bean.AlarmModel;

import org.litepal.crud.DataSupport;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DoctorAlarmActivity extends ToolbarBaseActivity {

    private PowerManager.WakeLock mWakeLock;
    private static final int WAKELOCK_TIMEOUT = 30 * 1000;
    private static final String TAG = "DoctorAlarmActivity";

    Button mButton1;
    Button mButton2;
    long id;
    AlarmModel model;

    String startTime;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_doctor_alarm;
    }

    @Override
    public void initParams(Intent intentArgument) {

        id = intentArgument.getLongExtra(AlarmHelper.ID, -1);
        model = DataSupport.find(AlarmModel.class, id);
        if (model != null && model.getTimestamp() != 0) {

            startTime = String.valueOf(model.getTimestamp() + 60000);

        }
    }

    @Override
    public void initView() {
        mButton1 = (Button) findViewById(R.id.video_true);
        mButton2 = (Button) findViewById(R.id.video_cancel);

        mTitleText.setText(getString(R.string.yuyue_video));

        MLVoiceSynthetize.startSynthesize(R.string.yuyue_tim);


        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int rows = DataSupport.delete(AlarmModel.class, id);

                if (rows >= 1) {
                    emitEvent("NimCall", "doctor_18940866148");
                    finish();
                }


            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if (model != null && model.getTimestamp() != 0) {
            Box.getRetrofit(AlarmAPI.class)
                    .queryDoctorReservationList(((UserInfoBean) Box.getSessionManager().getUser()).doid)
                    .compose(RxUtils.httpResponseTransformer(false))
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new CommonObserver<List<AlreadyYuyue>>() {
                        @Override
                        public void onNext(List<AlreadyYuyue> alreadyYuyues) {
                            for (int i = 0; i < alreadyYuyues.size(); i++) {

                                if (startTime.equals(alreadyYuyues.get(i).getStart_time())) {
                                    Box.getRetrofit(AlarmAPI.class)
                                            .updateReservationStatus(alreadyYuyues.get(i).getRid(), "5")
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new CommonObserver<Object>() {
                                                @Override
                                                public void onNext(Object o) {

                                                }
                                            });
                                }

                            }
                        }
                    });
        }


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


    /**
     * 返回上一页
     */
    protected void backLastActivity() {
        int rows = DataSupport.delete(AlarmModel.class, id);
        if (rows >= 1) {
            finish();
        }
    }

    /**
     * 返回到主页面
     */
    protected void backMainActivity() {
        int rows = DataSupport.delete(AlarmModel.class, id);
        if (rows >= 1) {
            super.backMainActivity();
        }

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

        super.onDestroy();
    }
}
