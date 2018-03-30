package com.example.han.referralproject.settting.activity;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoiceSettingActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.iv_min)
    ImageView ivMin;
    @BindView(R.id.iv_max)
    ImageView ivMax;
    private AudioManager mAudioManager;
    private int maxVolume;
    private int currentVolume;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("音量设置");

        //初始化音频管理器
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        //获取系统最大音量
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // 获取设备当前音量
        currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        seekBar.setMax(maxVolume);
        seekBar.setProgress(currentVolume);
        seekBar.setOnSeekBarChangeListener(this);
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

    @OnClick({R.id.iv_min, R.id.iv_max})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_min:
                clickMin();
                break;
            case R.id.iv_max:
                clickMax();

                break;
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
