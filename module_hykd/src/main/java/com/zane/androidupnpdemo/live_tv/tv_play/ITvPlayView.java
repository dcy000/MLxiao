package com.zane.androidupnpdemo.live_tv.tv_play;

import com.ksyun.media.player.KSYTextureView;

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
}
