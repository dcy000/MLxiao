package com.example.han.referralproject.yiyuan.idle;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.MessageQueue;
import android.util.Log;

import com.example.han.referralproject.activity.ChooseLoginTypeActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.settting.EventType;
import com.example.han.referralproject.settting.dialog.ClearCacheOrResetDialog;
import com.example.han.referralproject.util.LocalShared;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by lenovo on 2018/5/22.
 */

public class YiYuanIdleHandler implements MessageQueue.IdleHandler {
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
    private ClearCacheOrResetDialog dialog;

    {
        mHandlerThread = new HandlerThread("bg");
        mHandlerThread.start();
    }

    public Handler mHandler = new Handler(mHandlerThread.getLooper()) {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 5:
                    showDialog(EventType.exit);
                    break;
                case 10:
                    tuichu();
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


    private void showDialog(EventType type) {
        if (dialog == null) {
            dialog = new ClearCacheOrResetDialog(type);
        }
        dialog.setListener(new ClearCacheOrResetDialog.OnDialogClickListener() {
            @Override
            public void onClickConfirm(EventType type) {

            }

            @Override
            public void onClickCancel() {
                mHandler.removeMessages(10);
            }
        });
        dialog.show(MyApplication.getCurrentActivity().getFragmentManager(), "tuichu");

    }


    private void tuichu() {
        if (dialog != null) {
            dialog.dismiss();
        }
        MobclickAgent.onProfileSignOff();
        NimAccountHelper.getInstance().logout();
        LocalShared.getInstance(MyApplication.getCurrentActivity()).loginOut();
        MyApplication.getInstance().startActivity(new Intent(MyApplication.getCurrentActivity(), ChooseLoginTypeActivity.class));
//        MyApplication.getCurrentActivity().finish();
    }
}
