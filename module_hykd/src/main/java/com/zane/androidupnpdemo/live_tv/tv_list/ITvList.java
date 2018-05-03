package com.zane.androidupnpdemo.live_tv.tv_list;

import com.zane.androidupnpdemo.live_tv.LiveBean;

import java.util.List;

/**
 * Created by gzq on 2018/3/26.
 */

public interface ITvList {
    void showDialog();
    void hideDialog();
    void fillData(List<LiveBean> channels);
    void onError(String error);
    void closeActivity();
}
