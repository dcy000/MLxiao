package com.zane.androidupnpdemo.live_tv.tv_play;

import com.ksyun.media.player.KSYTextureView;
import com.zane.androidupnpdemo.connect_tv.entity.ClingDevice;
import com.zane.androidupnpdemo.connect_tv.entity.IDevice;

import java.util.Collection;

/**
 * Created by gzq on 2018/3/26.
 */

public interface ITvPlayView {
    void showLoadingDialog();
    void hideLoadingDialog();
    void showControlBar();
    void hideControlBar();
    void closeTv();
    void connectTv();
    void showStatusBar();
    void hideStatusBar();
    void addVideoView(KSYTextureView ksyTextureView);
    void removeVideoView(KSYTextureView ksyTextureView);
    int getControlBarVisibility();
    void showVoiceView();
    void hideVoiceView();
    void findNewDevice(IDevice device);
    void removeDevice(IDevice device);
    void refreshDeices(Collection<ClingDevice> devices);
}
