package com.gcml.call;

import android.telephony.TelephonyManager;

import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by afirez on 2017/10/20.
 */

public class CallPhoneStateObserver {
    public enum PhoneState {
        IDLE,           // 空闲
        INCOMING_CALL,  // 有来电
        DIALING_OUT,    // 呼出电话已经接通
        DIALING_IN      // 来电已接通
    }

    private final String TAG = "CallHelper";

    private int phoneState = TelephonyManager.CALL_STATE_IDLE;
    private PhoneState state = PhoneState.IDLE;

    private List<Observer<Integer>> autoHangUpObservers = new ArrayList<>(1); // 与本地电话互斥的挂断监听

    private static class Holder {
        private final static CallPhoneStateObserver INSTANCE = new CallPhoneStateObserver();
    }

    private CallPhoneStateObserver() {

    }

    public static CallPhoneStateObserver getInstance() {
        return Holder.INSTANCE;
    }

    public void onPhoneStateChanged(String state) {
        Timber.tag(TAG).d("onPhoneStateChanged: state=%s", state);
        this.state = PhoneState.IDLE;
        if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
            phoneState = TelephonyManager.CALL_STATE_IDLE;
            this.state = PhoneState.IDLE;
        } else if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            phoneState = TelephonyManager.CALL_STATE_RINGING;
            this.state = PhoneState.INCOMING_CALL;
        } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
            int lastPhoneState = phoneState;
            phoneState = TelephonyManager.CALL_STATE_OFFHOOK;
            if (lastPhoneState == TelephonyManager.CALL_STATE_IDLE) {
                this.state = PhoneState.DIALING_OUT;
            } else if (lastPhoneState == TelephonyManager.CALL_STATE_RINGING) {
                this.state = PhoneState.DIALING_IN;
            }
        }

        handleLocalCall();
    }

    /**
     * 处理本地电话与网络通话的互斥
     */
    public void handleLocalCall() {
        if (state != PhoneState.IDLE) {
            long chatId = AVChatManager.getInstance().getCurrentChatId();
            Timber.tag(TAG).d("handleLocalCall: action=Busy and hangUp2 who named %s", chatId);
            AVChatManager.getInstance().hangUp2(chatId, new HandleLocalCallCallback(1));
        }
    }

    public PhoneState getCallState() {
        return state;
    }

    private class HandleLocalCallCallback implements AVChatCallback<Void> {
        private int reason;
        private String log;

        public HandleLocalCallCallback(int reason) {
            this.reason = reason;
            this.log = "handleLocalCall";
        }

        @Override
        public void onSuccess(Void param) {
            Timber.tag(TAG).d("%s -> onSuccess: ", log);
            notifyObservers(autoHangUpObservers, reason);
        }

        @Override
        public void onFailed(int code) {
            Timber.tag(TAG).d("%s -> onFailed: code=%s", log, code);
            notifyObservers(autoHangUpObservers, -1 * reason);
        }

        @Override
        public void onException(Throwable exception) {
            Timber.tag(TAG).d("%s -> onException: exception=%s", log, exception.getMessage());
            notifyObservers(autoHangUpObservers, 0);
        }
    }

    private <T> void notifyObservers(List<Observer<T>> observers, T result) {
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

    /**
     * 监听网络通话发起，接听或正在进行时有本地来电的通知
     * 网络通话发起或者正在接通时，需要监听是否有本地来电（用户接通本地来电）。
     * 若有本地来电，目前Demo中示例代码的处理是网络通话自动拒绝或者挂断，开发者可以自行灵活处理。
     */
    public void observeAutoHangUpForLocalPhone(Observer<Integer> observer, boolean register) {
        Timber.tag(TAG).d("observeAutoHangUpForLocalPhone: observer=%s register=%s", observer, register);
        registerObservers(this.autoHangUpObservers, observer, register);
    }
}
