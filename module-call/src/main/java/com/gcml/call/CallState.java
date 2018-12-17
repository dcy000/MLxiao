package com.gcml.call;

/**
 * Created by afirez on 2017/10/24.
 */

public enum CallState {
    INVALID(-1), //无效的状态,该状态下无界面显示

    VIDEO(0), //正在进行视频通话(发起者)
    AUDIO(1), //正在进行语音通话(发起者)
    OUTGOING_VIDEO_CALLING(2), //邀请好友视频通话
    INCOMING_VIDEO_CALLING(4),
    OUTGOING_AUDIO_TO_VIDEO(6), //向好友发起从语音切换到视频的邀请
    VIDEO_CONNECTING(8), //视频通话连接中
    CONNECT_SUCCESS(10),
    CONNECT_FAILED(-2),
    VIDEO_OFF(12), // 对方关闭摄像头
    VIDEO_ON(14), // 对方关闭摄像头
    INCOMING_VIDEO_REFUSING(-4),
    INCOMING_VIDEO_REFUSE_FAILED(-6),
    INCOMING_VIDEO_REFUSE_SUCCESS(-8),
    INCOMING_VIDEO_RECEIVING(22),
    INCOMING_VIDEO_RECEIVE_FAILED(-10),
    INCOMING_VIDEO_RECEIVE_SUCCESS(26),

    OUTGOING_AUDIO_CALLING(3), //邀请好友语音通话
    INCOMING_AUDIO_CALLING(5), //来自好友的视频通话、语音通话邀请
    INCOMING_AUDIO_TO_VIDEO(7), //音频切换为视频的邀请
    AUDIO_CONNECTING(9); //语音通话连接中

    private int value;

    CallState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static boolean isVideoMode(CallState value) {
        return value.getValue() % 2 == 0;
    }

    public static boolean isAudioMode(CallState value) {
        return value.getValue() % 2 == 1;
    }

    public static CallState getCallState(int value) {
        for (CallState e : values()) {
            if (e.getValue() == value) {
                return e;
            }
        }

        return INVALID;
    }
}
