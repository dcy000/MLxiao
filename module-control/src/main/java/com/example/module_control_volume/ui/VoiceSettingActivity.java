package com.example.module_control_volume.ui;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.module_control_volume.R;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.sjtu.yifei.annotation.Route;

@Route(path = "module/control/voice/setting/activity")
public class VoiceSettingActivity extends ToolbarBaseActivity implements SeekBar.OnSeekBarChangeListener {

    SeekBar seekBar;
    ImageView ivMin;
    ImageView ivMax;
    private AudioManager mAudioManager;
    private int maxVolume;
    private int currentVolume;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_setting);
        seekBar= findViewById(R.id.seekBar);
        ivMin= findViewById(R.id.iv_min);
        ivMin.setOnClickListener(this);
        ivMax= findViewById(R.id.iv_max);
        ivMax.setOnClickListener(this);
        initView();
    }

    private void initView() {
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId()==R.id.iv_min){
            clickMin();
        }else if (v.getId()==R.id.iv_max){
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
