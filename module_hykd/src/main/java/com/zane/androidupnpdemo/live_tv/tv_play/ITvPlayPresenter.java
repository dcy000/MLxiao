package com.zane.androidupnpdemo.live_tv.tv_play;

/**
 * Created by gzq on 2018/3/26.
 */

public interface ITvPlayPresenter {
    void startPlay(String url);
    void onResume();
    void onPause();
    void onDestroy();
}
