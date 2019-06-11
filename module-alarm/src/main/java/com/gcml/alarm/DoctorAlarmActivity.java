package com.gcml.alarm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.gcml.common.recommend.bean.get.Doctor;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

@Deprecated
public class DoctorAlarmActivity extends ToolbarBaseActivity {

    private PowerManager.WakeLock mWakeLock;
    private static final int WAKELOCK_TIMEOUT = 30 * 1000;
    private static final String TAG = "DoctorAlarmActivity";

    Button mButton1;
    Button mButton2;
    long id;
    AlarmEntity model;
    //   ImageView headImg;
    //   ImageView mImageView1;

    String startTime = "";

    private AlarmRepository alarmRepository = new AlarmRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_alarm);

        mToolbar.setVisibility(View.VISIBLE);

        id = getIntent().getLongExtra(AlarmHelper.ID, -1);
        alarmRepository.findOneById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<AlarmEntity>() {
                    @Override
                    public void onNext(AlarmEntity alarmEntity) {
                        model = alarmEntity;
                        if (model != null && model.getTimestamp() != 0) {
                            startTime = String.valueOf(model.getTimestamp() + 60000);
                        }
                    }
                });

        mButton1 = findViewById(R.id.video_true);
        mButton2 = findViewById(R.id.video_cancel);

        mTitleText.setText("预 约 视 频");

        String tips = "主人您的预约时间到了，请及时和签约医生进行视频通话";
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), tips);

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if (model != null && model.getTimestamp() != 0) {
            alarmRepository.doctor()
                    .flatMap(new Function<Doctor, ObservableSource<List<AlreadyYuyue>>>() {
                        @Override
                        public ObservableSource<List<AlreadyYuyue>> apply(Doctor doctor) throws Exception {
                            return alarmRepository.contractAlready(String.valueOf(doctor.docterid));
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new DefaultObserver<List<AlreadyYuyue>>() {
                        @Override
                        public void onNext(List<AlreadyYuyue> alreadyYuyues) {
                            for (int i = 0; i < alreadyYuyues.size(); i++) {
                                if (startTime.equals(alreadyYuyues.get(i).getStart_time())) {
                                    alarmRepository.updateStatus(alreadyYuyues.get(i).getRid(), "5")
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .as(RxUtils.autoDisposeConverter(DoctorAlarmActivity.this))
                                            .subscribe(new DefaultObserver<String>() {
                                                @Override
                                                public void onNext(String s) {

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

    private void delete() {
        alarmRepository.delete(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object alarmModel) {
                        finish();
                    }
                });
    }


    /**
     * 返回上一页
     */
    protected void backLastActivity() {
        delete();
    }

    /**
     * 返回到主页面
     */
    protected void backMainActivity() {
        alarmRepository.delete(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object alarmModel) {
                        Routerfit.register(AppRouter.class).skipMainActivity();
                        finish();
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
}
