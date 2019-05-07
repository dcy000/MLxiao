package com.example.han.referralproject.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.AlreadyYuyue;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.medlink.danbogh.alarm.AlarmHelper;
import com.medlink.danbogh.alarm.AlarmModel;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

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

        mButton1 = findViewById(R.id.video_true);
        mButton2 = findViewById(R.id.video_cancel);

        mTitleText.setText("预 约 视 频");

        speak("主人您的预约时间到了，请及时和健康顾问进行视频通话");


        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int rows = DataSupport.delete(AlarmModel.class, id);

                if (rows >= 1) {
                    //TODO:这里还是写死的？
//                    CallHelper.launch(DoctorAlarmActivity.this, "doctor_18940866148");
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


            NetworkApi.YuYue_already(sharedPreferences1.getString("doctor_id", ""), new NetworkManager.SuccessCallback<ArrayList<AlreadyYuyue>>() {
                @Override
                public void onSuccess(ArrayList<AlreadyYuyue> response) {

                    for (int i = 0; i < response.size(); i++) {

                        if (startTime.equals(response.get(i).getStart_time())) {

                            NetworkApi.update_status(response.get(i).getRid(), "5", new NetworkManager.SuccessCallback<String>() {
                                @Override
                                public void onSuccess(String response) {

                                }

                            }, new NetworkManager.FailedCallback() {
                                @Override
                                public void onFailed(String message) {

                                }
                            });


                        }

                    }

                }

            }, new NetworkManager.FailedCallback()

            {
                @Override
                public void onFailed(String message) {

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

        super.onDestroy();
    }
}
