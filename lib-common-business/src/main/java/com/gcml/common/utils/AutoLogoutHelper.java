package com.gcml.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;

import com.sjtu.yifei.route.Routerfit;

public class AutoLogoutHelper {

    private static final int MSG_TRIGGER_ON_IDLE = -1;

    public static AutoLogoutHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final AutoLogoutHelper INSTANCE = new AutoLogoutHelper();
    }

    private Handler handler = new Handler(Looper.getMainLooper());

    private boolean isLogout = true;

    public boolean isLogout() {
        return isLogout;
    }

    public void autoLogout() {
        if (isLogout) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(MSG_TRIGGER_ON_IDLE);
                Looper.myQueue().addIdleHandler(idleHandler);
            }
        });
    }

    private MessageQueue.IdleHandler idleHandler = new MessageQueue.IdleHandler() {
        @Override
        public boolean queueIdle() {
            handler.removeCallbacks(idleRunnable);
            handler.postDelayed(idleRunnable, 1500);
            return true;
        }
    };

    private Runnable idleRunnable = new Runnable() {
        @Override
        public void run() {
//            Routerfit
        }
    };


}
