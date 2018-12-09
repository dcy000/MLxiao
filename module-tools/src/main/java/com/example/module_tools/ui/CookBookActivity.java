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
import com.example.module_tools.bean.CookbookBean;
import com.example.module_tools.service.XFSkillApi;
import com.gcml.lib_widget.voiceline.VoiceLineView;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.utils.StringUtil;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CookBookActivity extends ToolBaseActivity {

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
        return R.layout.activity_cook_book;
    }

    @Override
    protected boolean isShowToolbar() {
        return false;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        MLVoiceSynthetize.startSynthesize("主人,欢迎来到菜谱");
        initEvent();
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
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
            getDateData(tvDemo1.getText().toString().trim());

        } else if (i == R.id.tv_demo2) {
            getDateData(tvDemo2.getText().toString().trim());

        } else if (i == R.id.tv_demo3) {
            getDateData(tvDemo3.getText().toString().trim());

        } else if (i == R.id.iv_yuyin) {
            MLVoiceSynthetize.stop();
            onEndOfSpeech();
            startListener();

        }
    }


    private void getDateData(final String result) {
        XFSkillApi.getSkillData(result, new XFSkillApi.getDataListener() {
            @Override
            public void onSuccess(final Object anwser, final String anwserText, String service, String question) {
                if (!"cookbook".equals(service)) {
                    MLVoiceSynthetize.startSynthesize("主人,没有找到该菜谱");
                    return;
                }
                try {
                    CookBookResultActivity.StartMe(CookBookActivity.this, (List<CookbookBean>) anwser, result);
                } catch (Exception e) {
                    MLVoiceSynthetize.startSynthesize("主人,没有找到该菜谱");
                }
            }
        });
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
        MLVoiceRecognize.initSpeechRecognizer().startListening(new RecognizerListener() {
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
                CookBookActivity.this.onEndOfSpeech();
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
        getDateData(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceRecognize.stop();
        MLVoiceSynthetize.stop();
        mainHandler.removeCallbacksAndMessages(null);
    }
}
