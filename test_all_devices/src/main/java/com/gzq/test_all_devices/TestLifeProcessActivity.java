package com.gzq.test_all_devices;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/9 10:35
 * created by:gzq
 * description:TODO
 */
public class TestLifeProcessActivity extends AppCompatActivity{
    private static final String TAG = "TestLifeProcessActivity";

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e(TAG, "onAttachedToWindow: ");
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Log.e(TAG, "onUserLeaveHint: " );
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_life_process);
        Log.e(TAG, "onCreate: " );
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: " );
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        Log.e(TAG, "onContentChanged: " );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: " );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: " );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: " );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: " );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " );
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.e(TAG, "onPostResume: " );
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.e(TAG, "onPostCreate: " );
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Log.e(TAG, "onResumeFragments: " );
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: " );
        return super.onTouchEvent(event);
    }
}
