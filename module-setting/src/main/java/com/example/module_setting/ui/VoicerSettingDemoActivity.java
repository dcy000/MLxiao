package com.example.module_setting.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.module_setting.R;
import com.example.module_setting.R2;
import com.example.module_setting.setting.IatSettings;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VoicerSettingDemoActivity extends ToolbarBaseActivity {

    @BindView(R2.id.sb_yusu)
    SeekBar sbYusu;
    @BindView(R2.id.sb_yudiao)
    SeekBar sbYudiao;
    @BindView(R2.id.sb_rate)
    SeekBar sbRate;
    @BindView(R2.id.tv_yusu)
    TextView tvYusu;
    @BindView(R2.id.tv_yudiao)
    TextView tvYudiao;
    @BindView(R2.id.tv_rate)
    TextView tvRate;
    @BindView(R2.id.tv_shuo)
    TextView tvShuo;

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_voicer_setting_demo;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        initTitle();
        initView();
        MLVoiceSynthetize.startSynthesize("主人,请你选出一种声音");
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

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }

    private void initTitle() {
        mTitleText.setText("发音人调试");
        mToolbar.setVisibility(View.VISIBLE);
    }

    @OnClick(R2.id.tv_shuo)
    public void onViewClicked() {
        MLVoiceSynthetize.startSynthesize("选择的音调是"+tvYudiao.getText().toString()+"");
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
