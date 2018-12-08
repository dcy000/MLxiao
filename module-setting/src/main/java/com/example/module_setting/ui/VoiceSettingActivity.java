package com.example.module_setting.ui;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.module_setting.R;
import com.example.module_setting.R2;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoiceSettingActivity extends ToolbarBaseActivity implements SeekBar.OnSeekBarChangeListener {

    @BindView(R2.id.seekBar)
    SeekBar seekBar;
    @BindView(R2.id.iv_min)
    ImageView ivMin;
    @BindView(R2.id.iv_max)
    ImageView ivMax;
    private AudioManager mAudioManager;
    private int maxVolume;
    private int currentVolume;


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_voice_setting;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("音量设置");

        //初始化音频管理器
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //获取系统最大音量
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 获取设备当前音量
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        seekBar.setMax(maxVolume);
        seekBar.setProgress(currentVolume);
        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //设置声音
        setVoice(seekBar.getProgress());
    }

    private void setVoice(int value) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, value, AudioManager.FLAG_PLAY_SOUND);
    }

    @OnClick({R2.id.iv_min, R2.id.iv_max})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.iv_min) {
            clickMin();

        } else if (i == R.id.iv_max) {
            clickMax();


        }
    }

    private void clickMax() {
        if (seekBar.getProgress() == maxVolume) {
            return;
        }
        setVoice(maxVolume);
        seekBar.setProgress(maxVolume);
    }

    private void clickMin() {
        if (seekBar.getProgress() == 0) {
            return;
        }
        setVoice(0);
        seekBar.setProgress(0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        seekBar.setProgress(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }
}
