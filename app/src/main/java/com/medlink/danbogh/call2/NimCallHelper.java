package com.medlink.danbogh.call2;

import com.netease.nimlib.sdk.avchat.model.AVChatData;

/**
 * Created by lenovo on 2017/10/23.
 */

public class NimCallHelper {

    public static NimCallHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final NimCallHelper INSTANCE = new NimCallHelper();
    }

    private boolean isChatting = false;

    public boolean isChatting() {
        return isChatting;
    }

    public void setChatting(boolean chatting) {
        isChatting = chatting;
    }

    public void dispatchIncomingCallFromBroadCast(AVChatData avChatData) {

    }
}
