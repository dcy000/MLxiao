package com.zane.androidupnpdemo.live_tv.tv_play;

/**
 * Created by gzq on 2018/3/26.
 */

public interface ITvPlayPresenter {
    void startPlay(String url,int position);
    void onResume();
    void onPause();
    void playLast(String url,int position);
    void playNext(String url,int position);
    void onDestroy();
    int getOnPlayingPosition();
    void onBehindWakeuped();
    void refreshDevices();
    void startCastTv(String url);
    void stopCastTv();
    void searchTvDevices();
    boolean isBindedService();
}
