package com.example.module_control_volume.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.module_control_volume.R;
import com.iflytek.settting.IatSettings;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.iflytek.synthetize.MLVoiceSynthetize;

public class VoicerSettingDemoActivity extends ToolbarBaseActivity {

    SeekBar sbYusu;
    SeekBar sbYudiao;
    SeekBar sbRate;
    TextView tvYusu;
    TextView tvYudiao;
    TextView tvRate;
    TextView tvShuo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voicer_setting_demo);
        sbYusu=findViewById(R.id.sb_yusu);
        sbYudiao=findViewById(R.id.sb_yudiao);
        sbRate=findViewById(R.id.sb_rate);
        tvYusu=findViewById(R.id.tv_yusu);
        tvYudiao=findViewById(R.id.tv_yudiao);
        tvRate=findViewById(R.id.tv_rate);
        tvShuo=findViewById(R.id.tv_shuo);
        tvShuo.setOnClickListener(this);
        initTitle();
        initView();
        MLVoiceSynthetize.startSynthesize(this, "请你选出一种声音", false);
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
                getSharedPreferences(IatSettings.PREFER_NAME, Context.MODE_PRIVATE).edit()
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
                getSharedPreferences(IatSettings.PREFER_NAME, Context.MODE_PRIVATE).edit()
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
                getSharedPreferences(IatSettings.PREFER_NAME, Context.MODE_PRIVATE).edit()
                        .putString("rate_preference", progress + "")
                        .commit();
            }
        });


    }

    private void initTitle() {
        mTitleText.setText("发音人调试");
        mToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId()==R.id.tv_shuo){
            MLVoiceSynthetize.startSynthesize(this, "选择的音调是"+tvYudiao.getText().toString()+"", false);
        }
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
