package com.example.han.referralproject.settting.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.speech.setting.IatSettings;
import com.iflytek.synthetize.MLVoiceSynthetize;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoicerSettingDemoActivity extends BaseActivity {

    @BindView(R.id.sb_yusu)
    SeekBar sbYusu;
    @BindView(R.id.sb_yudiao)
    SeekBar sbYudiao;
    @BindView(R.id.sb_rate)
    SeekBar sbRate;
    @BindView(R.id.tv_yusu)
    TextView tvYusu;
    @BindView(R.id.tv_yudiao)
    TextView tvYudiao;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.tv_shuo)
    TextView tvShuo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voicer_setting_demo);
        ButterKnife.bind(this);
        initTitle();
        initView();
        MLVoiceSynthetize.startSynthesize(this, "主人,请你选出一种声音", false);
    }

    private void initView() {
        //语速
        sbYusu.setMax(100);
        sbYusu.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                tvYusu.setText("语速:" + progress);
                getSharedPreferences(IatSettings.PREFER_NAME, MODE_PRIVATE).edit()
                        .putString("speed_preference", progress + "")
                        .commit();

            }
        });

        //语调

        sbYudiao.setMax(100);
        sbYudiao.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                tvYudiao.setText("语调:" + progress);
                getSharedPreferences(IatSettings.PREFER_NAME, MODE_PRIVATE).edit()
                        .putString("pitch_preference", progress + "")
                        .commit();
            }
        });

        //采样率
//        sbRate.setMin(8000);
        sbRate.setMax(16000);
        sbRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                tvRate.setText("采样率:" + progress);
                getSharedPreferences(IatSettings.PREFER_NAME, MODE_PRIVATE).edit()
                        .putString("rate_preference", progress + "")
                        .commit();
            }
        });


    }

    private void initTitle() {
        mTitleText.setText("发音人调试");
        mToolbar.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_shuo)
    public void onViewClicked() {
        MLVoiceSynthetize.startSynthesize(this, "选择的音调是"+tvYudiao.getText().toString()+"", false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.stop();
    }

//    //设置合成语速
//                synthesizer.setParameter(SpeechConstant.SPEED, mTtsSharedPreferences.getString("speed_preference", "50"));
//    //设置合成音调
//                synthesizer.setParameter(SpeechConstant.PITCH, mTtsSharedPreferences.getString("pitch_preference", "50"));
//    //设置合成音量
//                synthesizer.setParameter(SpeechConstant.VOLUME, mTtsSharedPreferences.getString("volume_preference", "50"));
}
