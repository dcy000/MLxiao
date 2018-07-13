package com.example.han.referralproject.yiyuan.idle;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.MessageQueue;
import android.util.Log;

import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.require2.login.ChoiceLoginTypeActivity;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.yiyuan.fragment.CountdownDialog;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by lenovo on 2018/5/22.
 */

public class YiYuanIdleHandler implements MessageQueue.IdleHandler, CountdownDialog.Ontouch {
    private static YiYuanIdleHandler handler;

    private YiYuanIdleHandler() {
    }

    public static YiYuanIdleHandler getInstance() {
        if (handler == null) {
            synchronized (YiYuanIdleHandler.class) {
                if (handler == null) {
                    handler = new YiYuanIdleHandler();
                }
            }
        }

        return handler;
    }


    private volatile long mStartTime = -1;
    private HandlerThread mHandlerThread;
    private CountdownDialog dialog;

    {
        mHandlerThread = new HandlerThread("bg");
        mHandlerThread.start();
    }

    public Handler mHandler = new Handler(mHandlerThread.getLooper()) {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 5:
                    showDialog();
                    break;
                case 10:
//                    tuichu();
                    break;
            }
        }
    };


    @Override
    public boolean queueIdle() {
        long theTime = System.currentTimeMillis();
        Log.d("idleHandler", "queueIdle: " + theTime);
        if (mStartTime == -1) {
            mStartTime = theTime;
            mHandler.sendEmptyMessageDelayed(5, 5000);
            mHandler.sendEmptyMessageDelayed(10, 10000);

        } else {
            long duration = theTime - mStartTime;
            Log.d("duration", "durationTime:" + duration);
            if (duration < 5000) {
                mHandler.removeMessages(5);
                mHandler.removeMessages(10);
            } else if (duration < 10000) {
//                    mHandler.removeMessages(10);
            }
            mStartTime = -1;
        }
        return true;
    }


    private void showDialog() {
        if (dialog == null) {
            dialog = new CountdownDialog();
        }
        dialog.setOntouch(this);
        Activity currentActivity = MyApplication.getCurrentActivity();
        if (currentActivity == null || currentActivity.isFinishing()) {
            return;
        }
        dialog.show(currentActivity.getFragmentManager(), "tuichu");

    }


    private void tuichu() {
        MobclickAgent.onProfileSignOff();
        NimAccountHelper.getInstance().logout();
        LocalShared.getInstance(MyApplication.getCurrentActivity()).loginOut();

        Activity currentActivity = MyApplication.getCurrentActivity();
        Intent intent = new Intent(currentActivity, ChoiceLoginTypeActivity.class);
        currentActivity.startActivity(intent);
        currentActivity.finish();
    }

    @Override
    public void OnTouch() {
        mHandler.removeMessages(10);
    }

    @Override
    public void OnTime() {

    }
}
