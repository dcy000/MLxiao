package com.zane.androidupnpdemo.live_tv.tv_list;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * Created by gzq on 2018/3/26.
 */

public interface ITvChannelPresenter{
    void getChannels();
    void tvListItemClick(BaseQuickAdapter adapter, View view, int position);
    void onResume();
    void onPause();
    void onDestroy();

}
