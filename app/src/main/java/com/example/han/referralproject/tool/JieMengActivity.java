package com.example.han.referralproject.tool;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.ToolBaseActivity;
import com.example.han.referralproject.speech.util.JsonParser;
import com.example.han.referralproject.tool.other.StringUtil;
import com.example.han.referralproject.tool.other.XFSkillApi;
import com.example.han.referralproject.tool.wrapview.VoiceLineView;
import com.example.han.referralproject.tool.xfparsebean.DreamBean;
import com.example.han.referralproject.voice.SpeechRecognizerHelper;
import com.example.han.referralproject.voice.SpeechSynthesizerHelper;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JieMengActivity extends ToolBaseActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.tv_notice_demo)
    TextView tvNoticeDemo;
    @BindView(R.id.tv_demo1)
    TextView tvDemo1;
    @BindView(R.id.tv_demo2)
    TextView tvDemo2;
    @BindView(R.id.tv_demo3)
    TextView tvDemo3;
    @BindView(R.id.iv_yuyin)
    ImageView ivYuyin;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.cl_start)
    ConstraintLayout clStart;
    @BindView(R.id.vl_wave)
    VoiceLineView vlWave;


    private boolean isStart;
    int recordTotalTime = 0;
    private Handler mainHandler = new Handler();

    private List<DreamBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_jiemeng);
        ButterKnife.bind(this);
        speak("主人,欢迎来到周公姐梦!");
        initEvent();
    }

    private void initEvent() {
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void startListener() {
        SpeechRecognizer speechRecognizer = SpeechRecognizerHelper.initSpeechRecognizer(this);
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
                JieMengActivity.this.onEndOfSpeech();
                textView4.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                dealData(recognizerResult, b);
            }

            @Override
            public void onError(SpeechError speechError) {

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


    private void updateTimerUI(int recordTotalTime) {
        String string = String.format("%s", StringUtil.formatTime(recordTotalTime));
        vlWave.setText(string);
    }


    @Override
    public void getData(String s) {
        getDreamData(s);
    }
    private void getDreamData(final String result) {
        XFSkillApi.getSkillData(result, new XFSkillApi.getDataListener() {
            @Override
            public void onSuccess(final Object anwser, final String anwserText, String service, String question) {
                if (!"dream".equals(service)) {
                    speak("主人,这个梦我还不会解了");
                    return;
                }
                try {
                    data.clear();
                    data.addAll((List<DreamBean>) anwser);
                    JieMengRetultActivity.startMe(JieMengActivity.this, data, result, anwserText);
                } catch (Exception e) {
                    speak("主人,这个梦我还不会解了");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopListening();
        SpeechSynthesizerHelper.stop();
    }

    @OnClick({R.id.tv_demo1, R.id.tv_demo2, R.id.tv_demo3, R.id.iv_yuyin})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_demo1:
                String demo1 = tvDemo1.getText().toString();
                getDreamData(demo1);
                break;
            case R.id.tv_demo2:
                String demo2 = tvDemo2.getText().toString();
                getDreamData(demo2);
                break;
            case R.id.tv_demo3:
                String demo3 = tvDemo3.getText().toString();
                getDreamData(demo3);
                break;
            case R.id.iv_yuyin:
                SpeechSynthesizerHelper.stop();
                onEndOfSpeech();
                startListener();
                break;

        }
    }

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

}

