package com.example.han.referralproject.settting.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.ml.brightness.BrightnessHelper;

public class BrightnessSettingActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {

    private SeekBar seekBar;

    private Window mWindow;
    private WindowManager.LayoutParams mAttributes;
    private float mScreenBrightness;

    private BrightnessHelper mBrightnessHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brightness_setting);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("亮度设置");

        mWindow = getWindow();
        mAttributes = mWindow.getAttributes();
        mScreenBrightness = mAttributes.screenBrightness;
        mBrightnessHelper = new BrightnessHelper(this);

        seekBar = (SeekBar) findViewById(R.id.seekBar);

        seekBar.setMax(mBrightnessHelper.getMaxBrightness());
        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    protected void onResume() {
        seekBar.setProgress(mBrightnessHelper.getSystemBrightness());
        setDisableGlobalListen(true);
        setEnableListeningLoop(false);
        super.onResume();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mBrightnessHelper.setSystemBrightness(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mBrightnessHelper.setSystemBrightness(seekBar.getProgress());
    }
}
