package com.gcml.common.utils.data;

import android.os.CountDownTimer;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * create:gzq
 * create-time:2018/07/23
 * tips:倒计时工具类
 */
public class TimeCountDownUtils {
    private static volatile TimeCountDownUtils singleton = null;
    private static HashMap<String, TimeCount> timeCountMap = new HashMap<>();
    private static HashMap<String, TimeCountListener> listeners = new HashMap<>();
    private TimeCount timeCount;

    public static TimeCountDownUtils getInstance() {
        if (singleton == null) {
            synchronized (TimeCountDownUtils.class) {
                if (singleton == null) {
                    singleton = new TimeCountDownUtils();
                }
            }
        }
        return singleton;
    }

    class TimeCount extends CountDownTimer {

        private String tag;

        public TimeCount(long millisInFuture, long countDownInterval, String tag) {
            super(millisInFuture, countDownInterval);
            this.tag = tag;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (timeCountListener != null) {
                timeCountListener.onTick(millisUntilFinished, null);
            }
            if (!listeners.isEmpty() && !TextUtils.isEmpty(tag)) {
                TimeCountListener timeCountListener = listeners.get(tag);
                timeCountListener.onTick(millisUntilFinished, tag);
            }
        }

        @Override
        public void onFinish() {
            if (timeCountListener != null) {
                timeCountListener.onFinish(tag);
            }
            if (!listeners.isEmpty() && !TextUtils.isEmpty(tag)) {
                TimeCountListener timeCountListener = listeners.get(tag);
                timeCountListener.onFinish(tag);
            }
        }

    }

    public void create(long millisInFuture, long countDownInterval, TimeCountListener timeCountListener) {
        timeCount = new TimeCount(millisInFuture, countDownInterval, null);
        this.timeCountListener = timeCountListener;
    }

    public void create(long millisInFuture, long countDownInterval, String tag, TimeCountListener timeCountListener) {
        timeCount = new TimeCount(millisInFuture, countDownInterval, tag);
        timeCountMap.put(tag, timeCount);
        listeners.put(tag, timeCountListener);
    }

    public void start() {
        if (timeCount != null) {
            timeCount.start();
        }
    }

    public void start(String tag) {
        if (TextUtils.isEmpty(tag) || timeCountMap.isEmpty()) {
            return;
        }
        TimeCount timeCount = timeCountMap.get(tag);
        timeCount.start();
    }

    public void cancel(boolean isRecovery) {
        if (timeCount != null) {
            timeCount.cancel();
            if (isRecovery) {
                timeCountListener=null;
                timeCount = null;
            }
        }
    }

    public void cancel(String tag, boolean isRecovery) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        Iterator<Map.Entry<String, TimeCount>> iteratorTimeCount = timeCountMap.entrySet().iterator();
        while (iteratorTimeCount.hasNext()) {
            Map.Entry<String, TimeCount> entry = iteratorTimeCount.next();
            if (entry.getKey().equals(tag)) {
                entry.getValue().cancel();
                if (isRecovery) {
                    timeCountMap.remove(tag);
                    listeners.remove(tag);
                }
            }
        }
    }

    public void cancelAll() {
        if (timeCount == null && timeCountMap.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<String, TimeCount>> iteratorTimeCount = timeCountMap.entrySet().iterator();
        while (iteratorTimeCount.hasNext()) {
            Map.Entry<String, TimeCount> entry = iteratorTimeCount.next();
            entry.getValue().cancel();
        }
        timeCountMap.clear();
        listeners.clear();
        timeCountListener=null;
        if (timeCount != null) {
            timeCount.cancel();
            timeCount = null;
        }

    }

    private TimeCountListener timeCountListener;

    public interface TimeCountListener {
        void onTick(long millisUntilFinished, String tag);

        void onFinish(String tag);
    }
}
