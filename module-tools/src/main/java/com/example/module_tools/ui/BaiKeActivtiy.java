package com.example.module_tools.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.module_tools.R;
import com.example.module_tools.R2;
import com.example.module_tools.bean.BaiKeBean;
import com.example.module_tools.service.XFSkillApi;
import com.gcml.lib_widget.voiceline.VoiceLineView;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.utils.StringUtil;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BaiKeActivtiy extends ToolBaseActivity {

    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_demo1)
    TextView tvDemo1;
    @BindView(R2.id.tv_demo2)
    TextView tvDemo2;
    @BindView(R2.id.tv_demo3)
    TextView tvDemo3;
    @BindView(R2.id.iv_yuyin)
    ImageView ivYuyin;
    @BindView(R2.id.textView4)
    TextView textView4;
    @BindView(R2.id.vl_wave)
    VoiceLineView vlWave;
    @BindView(R2.id.tv_back)
    TextView tvBack;
    @BindView(R2.id.cl_start)
    ConstraintLayout clStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_bai_ke_activtiy;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        MLVoiceSynthetize.startSynthesize("主人,欢迎来到百科");
        initEvent();
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }

    private void initEvent() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @OnClick({R2.id.tv_demo1, R2.id.tv_demo2, R2.id.tv_demo3, R2.id.iv_yuyin})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.tv_demo1) {
            String demo1 = tvDemo1.getText().toString();
            getDreamData(demo1);

        } else if (i == R.id.tv_demo2) {
            String demo2 = tvDemo2.getText().toString();
            getDreamData(demo2);

        } else if (i == R.id.tv_demo3) {
            String demo3 = tvDemo3.getText().toString();
            getDreamData(demo3);

        } else if (i == R.id.iv_yuyin) {
            MLVoiceSynthetize.stop();
            onEndOfSpeech();
            startListener();

        }
    }

    private boolean isStart;
    int recordTotalTime = 0;
    private Handler mainHandler = new Handler();

    private void showWave() {
        if (isStart) {
            return;
        }
        isStart = true;
        textView4.setVisibility(View.GONE);
        vlWave.setVisibility(View.VISIBLE);
        vlWave.setText("00:00");
        vlWave.startRecord();
        mainHandler.removeCallbacksAndMessages(null);

        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recordTotalTime += 1000;
                updateTimerUI(recordTotalTime);
                mainHandler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void updateTimerUI(int recordTotalTime) {
        String string = String.format("%s", StringUtil.formatTime(recordTotalTime));
        vlWave.setText(string);
    }

    private void startListener() {
        SpeechRecognizer speechRecognizer = MLVoiceRecognize.initSpeechRecognizer();
        speechRecognizer.startListening(new RecognizerListener() {
            @Override
            public void onVolumeChanged(int i, byte[] bytes) {
                vlWave.waveH = i / 6 + 2;
            }

            @Override
            public void onBeginOfSpeech() {
                showWave();
            }

            @Override
            public void onEndOfSpeech() {
                BaiKeActivtiy.this.onEndOfSpeech();
                textView4.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                dealData(recognizerResult, b);
            }

            @Override
            public void onError(SpeechError speechError) {
                MLVoiceSynthetize.startSynthesize("主人,我没有听清,你能再说一遍吗?");
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }

    private void onEndOfSpeech() {
        vlWave.setVisibility(View.GONE);
        vlWave.stopRecord();
        isStart = false;
        recordTotalTime = 0;
        mainHandler.removeCallbacksAndMessages(null);
    }


    @Override
    public void getData(String s) {
        getDreamData(s);
    }

    private void getDreamData(final String result) {
        XFSkillApi.getSkillData(result, new XFSkillApi.getDataListener() {

            @Override
            public void onSuccess(final Object anwser, final String anwserText, String service, String question) {
                if (!"baike".equals(service)) {
                    MLVoiceSynthetize.startSynthesize("主人,没有找到" + result);
                    return;
                }
                try {
                    List<BaiKeBean> data = (List<BaiKeBean>) anwser;
                    BaikeResultActivity.startMe(BaiKeActivtiy.this, data, result);
                } catch (Exception e) {
                    MLVoiceSynthetize.startSynthesize("主人,没有找到" + result);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceRecognize.stop();
        MLVoiceSynthetize.stop();
        mainHandler.removeCallbacksAndMessages(null);
    }
}
