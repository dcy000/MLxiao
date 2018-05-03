package com.zane.androidupnpdemo.live_tv.tv_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzq.administrator.lib_common.base.CommonBaseActivity;
import com.iflytek.cloud.SpeechError;
import com.iflytek.recognition.MLRecognizerListener;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLSynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.ksyun.media.player.KSYMediaPlayer;
import com.zane.androidupnpdemo.R;
import com.zane.androidupnpdemo.live_tv.GridViewDividerItemDecoration;
import com.zane.androidupnpdemo.live_tv.LiveBean;
import com.zane.androidupnpdemo.utils.PinyinHelper;

import java.util.List;

/**
 * Created by gzq on 2018/3/26.
 */

public class TvChannelActivity extends CommonBaseActivity implements ITvList, View.OnClickListener {
    private RecyclerView mTvList;
    private BaseQuickAdapter<LiveBean, BaseViewHolder> adapter;
    private ITvChannelPresenter tvChannelPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_list);
        initView();
        tvChannelPresenter = new TvChannelPresenterImp(this, new TvChannelInteractorImp());
        tvChannelPresenter.getChannels();
    }



    @Override
    public void showDialog() {
        Log.e("显示dialog", "showDialog: ");
    }

    @Override
    public void hideDialog() {
        Log.e("隐藏dialog", "hideDialog: ");
    }

    @Override
    public void fillData(List<LiveBean> channels) {
        mTvList.setLayoutManager(new GridLayoutManager(this, 4));
        mTvList.addItemDecoration(new GridViewDividerItemDecoration(20, 20));
        mTvList.setAdapter(adapter = new BaseQuickAdapter<LiveBean, BaseViewHolder>(R.layout.tv_item, channels) {
            @Override
            protected void convert(BaseViewHolder helper, LiveBean item) {
                helper.setText(R.id.tv_name, item.getTvName());
                Glide.with(TvChannelActivity.this)
                        .load("file:///android_asset/"+item.getTvImgUrl())
                        .into((ImageView) helper.getView(R.id.iv_img));

            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e("item被点击", "onItemClick: ");
                tvChannelPresenter.tvListItemClick(adapter, view, position);
            }
        });
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeActivity() {
        finish();
    }

    private void initView() {
        mTvList = (RecyclerView) findViewById(R.id.tv_list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tvChannelPresenter.onDestroy();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

    }
}
