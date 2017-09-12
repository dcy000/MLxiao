package com.medlink.danbogh.call;

import com.hyphenate.chat.EMCallStateChangeListener;

/**
 * 通话相关事件传递对象
 */
public class CallEvent {

    private boolean isState;
    private boolean isTime;
    private EMCallStateChangeListener.CallState callState;
    private EMCallStateChangeListener.CallError callError;

    public EMCallStateChangeListener.CallState getCallState() {
        return callState;
    }

    public void setCallState(EMCallStateChangeListener.CallState callState) {
        this.callState = callState;
    }

    public EMCallStateChangeListener.CallError getCallError() {
        return callError;
    }

    public void setCallError(EMCallStateChangeListener.CallError callError) {
        this.callError = callError;
    }

    public boolean isState() {
        return isState;
    }

    public void setState(boolean state) {
        isState = state;
    }

    public boolean isTime() {
        return isTime;
    }

    public void setTime(boolean time) {
        isTime = time;
    }
}
