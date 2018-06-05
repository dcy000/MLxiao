package com.ml.call;

import android.util.Log;

import com.netease.nimlib.sdk.avchat.AVChatStateObserver;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;

import java.util.Map;

/**
 * Created by lenovo on 2018/6/4.
 */

public abstract class AbsChatStateObserver implements AVChatStateObserver {

    private static final String TAG = "AbsChatStateObserver";

    @Override
    public void onTakeSnapshotResult(String account, boolean success, String file) {
        Log.d(TAG, "onTakeSnapshotResult: ");
    }

    @Override
    public void onAVRecordingCompletion(String account, String filePath) {
        Log.d(TAG, "onAVRecordingCompletion: ");
    }

    @Override
    public void onAudioRecordingCompletion(String filePath) {
        Log.d(TAG, "onAudioRecordingCompletion: ");
    }

    @Override
    public void onLowStorageSpaceWarning(long availableSize) {
        Log.d(TAG, "onLowStorageSpaceWarning: ");
    }

    @Override
    public void onAudioMixingEvent(int event) {
        Log.d(TAG, "onAudioMixingEvent: ");
    }

    @Override
    public void onJoinedChannel(int code, String audioFile, String videoFile, int elapsed) {
        Log.d(TAG, "onJoinedChannel: ");
    }

    @Override
    public void onUserJoined(String account) {
        Log.d(TAG, "onUserJoined: " + account);
    }

    @Override
    public void onUserLeave(String account, int event) {
        Log.d(TAG, "onUserLeave: " + account +" " + event);
    }

    @Override
    public void onLeaveChannel() {
        Log.d(TAG, "onLeaveChannel: ");
    }

    @Override
    public void onProtocolIncompatible(int status) {
        Log.d(TAG, "onProtocolIncompatible: ");
    }

    @Override
    public void onDisconnectServer() {
        Log.d(TAG, "onDisconnectServer: ");
    }

    @Override
    public void onNetworkQuality(String user, int quality, AVChatNetworkStats stats) {
        Log.d(TAG, "onNetworkQuality: ");
    }

    @Override
    public void onCallEstablished() {
        Log.d(TAG, "onCallEstablished: ");
    }

    @Override
    public void onDeviceEvent(int code, String desc) {
        Log.d(TAG, "onDeviceEvent: ");
    }

    @Override
    public void onConnectionTypeChanged(int netType) {
        Log.d(TAG, "onConnectionTypeChanged: ");
    }

    @Override
    public void onFirstVideoFrameAvailable(String account) {
        Log.d(TAG, "onFirstVideoFrameAvailable: ");
    }

    @Override
    public void onFirstVideoFrameRendered(String user) {
        Log.d(TAG, "onFirstVideoFrameRendered: ");
    }

    @Override
    public void onVideoFrameResolutionChanged(String user, int width, int height, int rotate) {
        Log.d(TAG, "onVideoFrameResolutionChanged: ");
    }

    @Override
    public void onVideoFpsReported(String account, int fps) {
        Log.d(TAG, "onVideoFpsReported: ");
    }

    @Override
    public boolean onVideoFrameFilter(AVChatVideoFrame frame, boolean maybeDualInput) {
        Log.d(TAG, "onVideoFrameFilter: ");
        return false;
    }

    @Override
    public boolean onAudioFrameFilter(AVChatAudioFrame frame) {
        Log.d(TAG, "onAudioFrameFilter: ");
        return false;
    }

    @Override
    public void onAudioDeviceChanged(int device) {
        Log.d(TAG, "onAudioDeviceChanged: ");
    }

    @Override
    public void onReportSpeaker(Map<String, Integer> speakers, int mixedEnergy) {
        Log.d(TAG, "onReportSpeaker: ");
    }

    @Override
    public void onSessionStats(AVChatSessionStats sessionStats) {
        Log.d(TAG, "onSessionStats: ");
    }

    @Override
    public void onLiveEvent(int event) {
        Log.d(TAG, "onLiveEvent: " + event);
    }
}
