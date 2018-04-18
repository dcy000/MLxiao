package com.zane.androidupnpdemo.live_tv.tv_list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iflytek.recognition.MLVoiceRecognize;
import com.zane.androidupnpdemo.live_tv.LiveBean;
import com.zane.androidupnpdemo.live_tv.tv_play.TvPlayActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gzq on 2018/3/26.
 */

public class TvChannelPresenterImp implements ITvChannelInteractor.OnParseExcelListener, ITvChannelPresenter {
    private ITvList tvChannelActivity;
    private ITvChannelInteractor tvChannelInteractor;
    private List<LiveBean> mData;

    public TvChannelPresenterImp(ITvList tvChannelActivity, ITvChannelInteractor tvChannelInteractor) {
        this.tvChannelActivity = tvChannelActivity;
        this.tvChannelInteractor = tvChannelInteractor;

    }

    @Override
    public void onStart() {
        if (tvChannelActivity != null)
            tvChannelActivity.showDialog();
    }

    @Override
    public void onFinished(List<LiveBean> channels) {
        this.mData = channels;
        if (tvChannelActivity != null) {
            tvChannelActivity.hideDialog();
            tvChannelActivity.fillData(channels);
        }
    }

    @Override
    public void onError(String error) {
        if (tvChannelActivity != null) {
            tvChannelActivity.hideDialog();
            tvChannelActivity.onError(error);
        }
    }

    @Override
    public void getChannels() {
        tvChannelInteractor.getChannels(this, tvChannelActivity);
    }

    @Override
    public void tvListItemClick(BaseQuickAdapter adapter, View view, int position) {
        TvPlayActivity.startTvPlayActivity((Context) tvChannelActivity,
                new Intent((Context) tvChannelActivity, TvPlayActivity.class)
        .putParcelableArrayListExtra("tvs", (ArrayList<? extends Parcelable>) mData)
        .putExtra("position",position));
    }

    @Override
    public void onDestroy() {
        tvChannelActivity = null;
        Log.e("看视图的引用是否被消除", "onDestroy: ");
    }

}
