package com.ml.call;

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

public abstract class AbsChatStateObserver implements AVChatStateObserver {

    private static final String TAG = "CallChatStateObserver";

    @Override
    public void onTakeSnapshotResult(String account, boolean success, String file) {
        Timber.tag(TAG).d("onTakeSnapshotResult: account=%s success=%s file=%s", account, success, file);
    }

    @Override
    public void onAVRecordingCompletion(String account, String filePath) {
        Timber.tag(TAG).d("onAVRecordingCompletion: account=%s file=%s", account, filePath);
    }

    @Override
    public void onAudioRecordingCompletion(String filePath) {
        Timber.tag(TAG).d("onAudioRecordingCompletion: file=%s", filePath);
    }

    @Override
    public void onLowStorageSpaceWarning(long availableSize) {
        Timber.tag(TAG).d("onLowStorageSpaceWarning: availableSize=%s", availableSize);
    }

    @Override
    public void onAudioMixingEvent(int event) {
        Timber.tag(TAG).d("onAudioMixingEvent: event=%s", event);
    }

    @Override
    public void onJoinedChannel(int code, String audioFile, String videoFile, int elapsed) {
        Timber.tag(TAG).d("onJoinedChannel: code=%s audioFile=%s videoFile=%s elapsed=%s", code, audioFile, videoFile, elapsed);
    }

    @Override
    public void onUserJoined(String account) {
        Timber.tag(TAG).d("onUserJoined: " + account);
    }

    @Override
    public void onUserLeave(String account, int event) {
        Timber.tag(TAG).d("onUserLeave: account=%s event=%s", account, event);
    }

    @Override
    public void onLeaveChannel() {
        Timber.tag(TAG).d("onLeaveChannel: ");
    }

    @Override
    public void onProtocolIncompatible(int status) {
        Timber.tag(TAG).d("onProtocolIncompatible: ");
    }

    @Override
    public void onDisconnectServer() {
        Timber.tag(TAG).d("onDisconnectServer: ");
    }

    @Override
    public void onNetworkQuality(String user, int quality, AVChatNetworkStats stats) {
        Timber.tag(TAG).d("onNetworkQuality: user=%s quality=%s stats=%s", user, quality, stats);
    }

    @Override
    public void onCallEstablished() {
        Timber.tag(TAG).d("onCallEstablished: ");
    }

    @Override
    public void onDeviceEvent(int code, String desc) {
        Timber.tag(TAG).d("onDeviceEvent: code=%s desc=%s", code, desc);
    }

    @Override
    public void onConnectionTypeChanged(int netType) {
        Timber.tag(TAG).d("onConnectionTypeChanged: netType=%s", netType);
    }

    @Override
    public void onFirstVideoFrameAvailable(String account) {
        Timber.tag(TAG).d("onFirstVideoFrameAvailable: account=%s", account);
    }

    @Override
    public void onFirstVideoFrameRendered(String user) {
        Timber.tag(TAG).d("onFirstVideoFrameRendered: user=%s", user);
    }

    @Override
    public void onVideoFrameResolutionChanged(String user, int width, int height, int rotate) {
        Timber.tag(TAG).d("onVideoFrameResolutionChanged: user=%s width=%s, height=, int rotate");
    }

    @Override
    public void onVideoFpsReported(String account, int fps) {
        Timber.tag(TAG).d("onVideoFpsReported: account=%s fps=%s", account, fps);
    }

    @Override
    public boolean onVideoFrameFilter(AVChatVideoFrame frame, boolean maybeDualInput) {
        Timber.tag(TAG).d("onVideoFrameFilter: frame=%s maybeDualInput=%s", frame, maybeDualInput);
        return false;
    }

    @Override
    public boolean onAudioFrameFilter(AVChatAudioFrame frame) {
        Timber.tag(TAG).d("onAudioFrameFilter: frame=%s", frame);
        return false;
    }

    @Override
    public void onAudioDeviceChanged(int device) {
        Timber.tag(TAG).d("onAudioDeviceChanged: device=%s", device);
    }

    @Override
    public void onReportSpeaker(Map<String, Integer> speakers, int mixedEnergy) {
        Timber.tag(TAG).d("onReportSpeaker: speakers=%s mixedEnergy=%s",speakers, mixedEnergy);
    }

    @Override
    public void onSessionStats(AVChatSessionStats sessionStats) {
        Timber.tag(TAG).d("onSessionStats: sessionStats=%s", sessionStats);
    }

    @Override
    public void onLiveEvent(int event) {
        Timber.tag(TAG).d("onLiveEvent: event=" + event);
    }
}
