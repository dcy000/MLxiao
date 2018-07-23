package com.gzq.test_all_devices;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gcml.lib_utils.data.TimeCountDownUtils;

public class TestCountDown extends AppCompatActivity {
    private static final String TAG = "TestCountDown";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Log.e(TAG, "onCreate: " );

        TimeCountDownUtils.getInstance().create(5000, 1000, "tag1", new TimeCountDownUtils.TimeCountListener() {
            @Override
            public void onTick(long millisUntilFinished, String tag) {
                Log.e(TAG, "onTick: "+tag );
            }

            @Override
            public void onFinish(String tag) {

            }
        });
        TimeCountDownUtils.getInstance().create(10000, 1000, "tag2", new TimeCountDownUtils.TimeCountListener() {
            @Override
            public void onTick(long millisUntilFinished, String tag) {
                Log.e(TAG, "onTick: "+tag );
            }

            @Override
            public void onFinish(String tag) {

            }
        });
        TimeCountDownUtils.getInstance().start("tag1");
        TimeCountDownUtils.getInstance().start("tag2");
//        TimeCountDownUtils.getInstance().create(5000,1000,timeCountListener);
//        TimeCountDownUtils.getInstance().start();
    }

    final TimeCountDownUtils.TimeCountListener timeCountListener = new TimeCountDownUtils.TimeCountListener() {
        @Override
        public void onTick(long millisUntilFinished, String tag) {
            Log.e(TAG, "onTick: " );
            if (tag.equals("tag1")) {
                Log.e(TAG, "onTick: " + "tag1");
            }
            if (tag.equals("tag2")) {
                Log.e(TAG, "onTick: " + "tag2");
            }
        }

        @Override
        public void onFinish(String tag) {

        }
    };
}
