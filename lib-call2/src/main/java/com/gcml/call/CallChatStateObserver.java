package com.gcml.call;

import com.netease.nimlib.sdk.avchat.AVChatStateObserver;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;

import java.util.Map;

import timber.log.Timber;

/**
 * Created by afirez on 2018/6/4.
 */

public class CallChatStateObserver implements AVChatStateObserver {
    @Override
    public void onAudioMixingProgressUpdated(long progressMs, long durationMs) {
        Timber.i("onDisconnectServer: progressMs = %s, durationMs =%s", progressMs, durationMs);
    }

    @Override
    public void onDisconnectServer(int code) {
        Timber.w("onDisconnectServer: code = %s", code);
    }

    @Override
    public void onTakeSnapshotResult(String account, boolean success, String file) {
        Timber.i("onTakeSnapshotResult: account=%s success=%s file=%s", account, success, file);
    }

    @Override
    public void onAVRecordingCompletion(String account, String filePath) {
        Timber.i("onAVRecordingCompletion: account=%s file=%s", account, filePath);
    }

    @Override
    public void onAudioRecordingCompletion(String filePath) {
        Timber.i("onAudioRecordingCompletion: file=%s", filePath);
    }

    @Override
    public void onLowStorageSpaceWarning(long availableSize) {
        Timber.w("onLowStorageSpaceWarning: availableSize=%s", availableSize);
    }

    @Override
    public void onAudioMixingEvent(int event) {
        Timber.i("onAudioMixingEvent: event=%s", event);
    }

    @Override
    public void onJoinedChannel(int code, String audioFile, String videoFile, int elapsed) {
        Timber.i("onJoinedChannel: code=%s audioFile=%s videoFile=%s elapsed=%s", code, audioFile, videoFile, elapsed);
    }

    @Override
    public void onUserJoined(String account) {
        Timber.i("onUserJoined:account=%s", account);
    }

    @Override
    public void onUserLeave(String account, int event) {
        Timber.w("onUserLeave: account=%s event=%s", account, event);
    }

    @Override
    public void onLeaveChannel() {
        Timber.w("onLeaveChannel: ");
    }

    @Override
    public void onProtocolIncompatible(int status) {
        Timber.w("onProtocolIncompatible: ");
    }

    @Override
    public void onNetworkQuality(String user, int quality, AVChatNetworkStats stats) {
        Timber.w("onNetworkQuality: user=%s quality=%s stats=%s", user, quality, stats);
    }

    @Override
    public void onCallEstablished() {
        Timber.i("onCallEstablished: ");
    }

    @Override
    public void onDeviceEvent(int code, String desc) {
        Timber.i("onDeviceEvent: code=%s desc=%s", code, desc);
    }

    @Override
    public void onConnectionTypeChanged(int netType) {
        Timber.i("onConnectionTypeChanged: netType=%s", netType);
    }

    @Override
    public void onFirstVideoFrameAvailable(String account) {
        Timber.i("onFirstVideoFrameAvailable: account=%s", account);
    }

    @Override
    public void onFirstVideoFrameRendered(String user) {
        Timber.i("onFirstVideoFrameRendered: user=%s", user);
    }

    @Override
    public void onVideoFrameResolutionChanged(String user, int width, int height, int rotate) {
        Timber.i("onVideoFrameResolutionChanged: user=%s width=%s, height=%s, rotate=%s", user, width, height, rotate);
}

    @Override
    public void onVideoFpsReported(String account, int fps) {
        Timber.i("onVideoFpsReported: account=%s fps=%s", account, fps);
    }

    @Override
    public boolean onVideoFrameFilter(AVChatVideoFrame frame, boolean maybeDualInput) {
        Timber.i("onVideoFrameFilter: frame=%s maybeDualInput=%s", frame, maybeDualInput);
        return false;
    }

    @Override
    public boolean onAudioFrameFilter(AVChatAudioFrame frame) {
        Timber.i("onAudioFrameFilter: frame=%s", frame);
        return false;
    }

    @Override
    public void onAudioDeviceChanged(int device) {
        Timber.i("onAudioDeviceChanged: device=%s", device);
    }

    @Override
    public void onReportSpeaker(Map<String, Integer> speakers, int mixedEnergy) {
        Timber.i("onReportSpeaker: speakers=%s mixedEnergy=%s", speakers, mixedEnergy);
    }

    @Override
    public void onSessionStats(AVChatSessionStats sessionStats) {
        Timber.i("onSessionStats: sessionStats=%s", sessionStats);
    }

    @Override
    public void onLiveEvent(int event) {
        Timber.i("onLiveEvent: event=%s", event);
    }
}
