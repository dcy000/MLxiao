package com.example.han.referralproject.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.medlink.danbogh.alarm.AlarmHelper;
import com.medlink.danbogh.alarm.AlarmModel;
import com.medlink.danbogh.call2.NimCallActivity;

import org.litepal.crud.DataSupport;

public class DoctorAlarmActivity extends BaseActivity {

    private PowerManager.WakeLock mWakeLock;
    private static final int WAKELOCK_TIMEOUT = 30 * 1000;
    private static final String TAG = "DoctorAlarmActivity";

    Button mButton1;
    Button mButton2;
    long id;
    AlarmModel model;
    //   ImageView mImageView;
    //   ImageView mImageView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_alarm);

        id = getIntent().getLongExtra(AlarmHelper.ID, -1);
        model = DataSupport.find(AlarmModel.class, id);
        mButton1 = (Button) findViewById(R.id.video_true);
        mButton2 = (Button) findViewById(R.id.video_cancel);

        mTitleText.setText(getString(R.string.test_record));


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
                int rows = DataSupport.delete(AlarmModel.class, id);

                if (rows >= 1) {

                    Intent intent = new Intent(DoctorAlarmActivity.this, DoctorappoActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });


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
