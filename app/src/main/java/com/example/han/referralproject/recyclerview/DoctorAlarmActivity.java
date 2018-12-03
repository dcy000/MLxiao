package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.AlreadyYuyue;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.service.API;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.alarm.AlarmHelper;
import com.medlink.danbogh.alarm.AlarmModel;
import com.medlink.danbogh.call2.NimCallActivity;

import org.litepal.crud.DataSupport;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DoctorAlarmActivity extends BaseActivity {

    private PowerManager.WakeLock mWakeLock;
    private static final int WAKELOCK_TIMEOUT = 30 * 1000;
    private static final String TAG = "DoctorAlarmActivity";

    Button mButton1;
    Button mButton2;
    long id;
    AlarmModel model;
    //   ImageView headImg;
    //   ImageView mImageView1;

    SharedPreferences sharedPreferences1;
    String startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_alarm);

        mToolbar.setVisibility(View.VISIBLE);

        sharedPreferences1 = getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE);


        id = getIntent().getLongExtra(AlarmHelper.ID, -1);
        model = DataSupport.find(AlarmModel.class, id);
        if (model != null && model.getTimestamp() != 0) {

            startTime = String.valueOf(model.getTimestamp() + 60000);

        }

        mButton1 = (Button) findViewById(R.id.video_true);
        mButton2 = (Button) findViewById(R.id.video_cancel);

        mTitleText.setText(getString(R.string.yuyue_video));

        MLVoiceSynthetize.startSynthesize(R.string.yuyue_tim);


        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int rows = DataSupport.delete(AlarmModel.class, id);

                if (rows >= 1) {

                    NimCallActivity.launch(DoctorAlarmActivity.this, "doctor_18940866148");
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
            Box.getRetrofit(API.class)
                    .queryDoctorReservationList(((UserInfoBean) Box.getSessionManager().getUser()).doid)
                    .compose(RxUtils.httpResponseTransformer(false))
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new CommonObserver<List<AlreadyYuyue>>() {
                        @Override
                        public void onNext(List<AlreadyYuyue> alreadyYuyues) {
                            for (int i = 0; i < alreadyYuyues.size(); i++) {

                                if (startTime.equals(alreadyYuyues.get(i).getStart_time())) {
                                    Box.getRetrofit(API.class)
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

            Intent intent = new Intent(DoctorAlarmActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
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
