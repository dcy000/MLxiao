package com.ml.call;

import android.os.Handler;
import android.os.Looper;

import com.netease.nimlib.sdk.Observer;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by afirez on 2017/10/24.
 */

public class CallTimeoutObserver {
    private static final String TAG = "CallHelper";
    private List<TimeoutObserver> timeoutObservers = new ArrayList<>();
    private List<Observer<Integer>> timeoutObserverLocal = new ArrayList<>(1); // 来电or呼出超时监听
    private Handler uiHandler;
    private static final int OUTGOING_TIME_OUT = 45 * 1000;
    private static final int INCOMING_TIME_OUT = 46 * 1000;

    private static class Holder {
        public final static CallTimeoutObserver INSTANCE = new CallTimeoutObserver();
    }

    public static CallTimeoutObserver getInstance() {
        return Holder.INSTANCE;
    }

    private CallTimeoutObserver() {
        uiHandler = new Handler(Looper.getMainLooper());
    }


    // 通知APP观察者
    private <T> void notifyObservers(List<Observer<T>> observers, T result) {
        Timber.tag(TAG).d("notifyObservers: observer=%s, result=%s" , observers, result);
        if (observers == null || observers.isEmpty()) {
            return;
        }

        // 创建副本，为了使得回调到app后，app如果立即注销观察者，会造成List异常。
        List<Observer<T>> copy = new ArrayList<>(observers.size());
        copy.addAll(observers);

        for (Observer<T> o : copy) {
            o.onEvent(result);
        }
    }

    // 注册注销APP观察者
    private <T> void registerObservers(List<Observer<T>> observers, final Observer<T> observer, boolean register) {
        if (observers == null || observer == null) {
            return;
        }

        if (register) {
            observers.add(observer);
        } else {
            observers.remove(observer);
        }
    }

    public void observeTimeoutNotification(Observer<Integer> observer, boolean register, boolean isIncoming) {
        Timber.tag(TAG).d("observeTimeoutNotification: observer=%s, register=%s, isIncoming=%s" , observer, register, isIncoming);
        registerObservers(timeoutObserverLocal, observer, register);
        if (register) {
            if (isIncoming) {
                addIncomingTimeout();
            } else {
                addOutgoingTimeout();
            }
        } else {
            removeAllTimeout();
        }
    }

    private class TimeoutObserver implements Runnable {

        TimeoutObserver() {

        }

        @Override
        public void run() {
            notifyObservers(timeoutObserverLocal, 0);
        }
    }


    private void addOutgoingTimeout() {
        Timber.tag(TAG).d("addOutgoingTimeout: ");
        TimeoutObserver timeoutObserver = new TimeoutObserver();
        timeoutObservers.add(timeoutObserver);
        uiHandler.postDelayed(timeoutObserver, OUTGOING_TIME_OUT);
    }


    private void removeAllTimeout() {
        Timber.tag(TAG).d("removeAllTimeout: ");
        for (TimeoutObserver observer : timeoutObservers) {
            uiHandler.removeCallbacks(observer);
        }
        timeoutObservers.clear();
    }

    private void addIncomingTimeout() {
        Timber.tag(TAG).d("addIncomingTimeout: ");
        TimeoutObserver timeoutObserver = new TimeoutObserver();
        timeoutObservers.add(timeoutObserver);
        uiHandler.postDelayed(timeoutObserver, INCOMING_TIME_OUT);
    }
}
