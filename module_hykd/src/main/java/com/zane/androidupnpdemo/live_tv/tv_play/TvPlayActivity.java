package com.zane.androidupnpdemo.live_tv.tv_play;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ksyun.media.player.KSYTextureView;
import com.zane.androidupnpdemo.R;
import com.zane.androidupnpdemo.connect_tv.ui.TVConnectMainActivity;

/**
 * Created by gzq on 2018/3/26.
 */

public class TvPlayActivity extends AppCompatActivity implements View.OnClickListener,ITvPlayView {
    private LinearLayout mVideoPlay;
    private TextView mConnectTv;
    private ImageView mLivemediaBack;
    private TextView mLiveTitle;
    private RelativeLayout mLiveControl;
    private LinearLayout mLoaddingView;
    private String url;
    private ITvPlayPresenter tvPlayPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_display);
        initView();
        tvPlayPresenter=new TvPlayPresenterImp(this);
        url=getIntent().getStringExtra("url");
        tvPlayPresenter.startPlay(url);
    }

    public static void startTvPlayActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }

    private void initView() {
        mVideoPlay = (LinearLayout) findViewById(R.id.video_play);
        mConnectTv = (TextView) findViewById(R.id.connectTv);
        mConnectTv.setOnClickListener(this);
        mLivemediaBack = (ImageView) findViewById(R.id.livemedia_back);
        mLivemediaBack.setOnClickListener(this);
        mLiveTitle = (TextView) findViewById(R.id.live_title);
        mLiveControl = (RelativeLayout) findViewById(R.id.live_control);
        mLoaddingView = (LinearLayout) findViewById(R.id.loadding_view);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.connectTv) {
            connectTv();

        } else if (i == R.id.livemedia_back) {
            closeTv();

        }
    }

    @Override
    public void showLoadingDialog() {
        mLoaddingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingDialog() {
        mLoaddingView.setVisibility(View.GONE);
    }

    @Override
    public void showControlBar() {
        mLiveControl.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideControlBar() {
        mLiveControl.setVisibility(View.GONE);
    }

    @Override
    public void closeTv() {
        finish();
    }

    @Override
    public void connectTv() {
        startActivity(new Intent(this, TVConnectMainActivity.class).putExtra("url",url));
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvPlayPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tvPlayPresenter.onPause();
    }

    @Override
    public void showStatusBar() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(params);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public void hideStatusBar() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public void addVideoView(KSYTextureView ksyTextureView) {
        mVideoPlay.addView(ksyTextureView);
    }

    @Override
    public void removeVideoView(KSYTextureView ksyTextureView) {
        mVideoPlay.removeView(ksyTextureView);
    }

    @Override
    public int getControlBarVisibility() {
        return mLiveControl.getVisibility();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tvPlayPresenter.onDestroy();
    }
}
