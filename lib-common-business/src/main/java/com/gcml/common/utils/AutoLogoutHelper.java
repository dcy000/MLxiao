package com.gcml.common.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.MessageQueue;

import timber.log.Timber;

public class AutoLogoutHelper {

    private static final int MSG_TRIGGER_ON_IDLE = -1;

    public static AutoLogoutHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final AutoLogoutHelper INSTANCE = new AutoLogoutHelper();
    }

    private boolean isLogin = false;

    private long delayMillis = 1500L;

    public void setDelayMillis(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    private boolean hasInit;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Handler bgHandler;

    private AutoLogoutHelper() {
        HandlerThread idle = new HandlerThread("idle");
        idle.start();
        bgHandler = new Handler(idle.getLooper());
        init();
    }

    private void init() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (hasInit) {
                    return;
                }
                hasInit = true;
                handler.sendEmptyMessage(MSG_TRIGGER_ON_IDLE);
                Looper.myQueue().addIdleHandler(idleHandler);
            }
        });
    }

    private MessageQueue.IdleHandler idleHandler = new MessageQueue.IdleHandler() {
        @Override
        public boolean queueIdle() {
            Timber.i("idle remove ");
            bgHandler.removeCallbacks(idleRunnable);
            if (!isLogin) {
                return true;
            }
            if (delayMillis < 0) {
                delayMillis = 0;
            }
            Timber.i("idle ...");
            bgHandler.postDelayed(idleRunnable, delayMillis);
            return true;
        }
    };

    private Runnable idleRunnable = new Runnable() {
        @Override
        public void run() {
            Timber.i("idle dispatch");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (action != null) {
                        action.logout();
                    }
                }
            });
        }
    };

    private LogoutAction action;

    public void setAction(LogoutAction action) {
        this.action = action;
    }

    public interface LogoutAction {
        void logout();
    }

}
