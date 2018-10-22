package com.example.han.referralproject.tool;

import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.ToolBaseActivity;
import com.example.han.referralproject.tool.other.StringUtil;
import com.example.han.referralproject.tool.other.XFSkillApi;
import com.gcml.lib_widget.VoiceLineView;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DateInquireActivity extends ToolBaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_demo1)
    TextView tvDemo1;
    @BindView(R.id.tv_demo2)
    TextView tvDemo2;
    @BindView(R.id.tv_demo3)
    TextView tvDemo3;
    @BindView(R.id.iv_yuyin)
    ImageView ivYuyin;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.cl_start)
    ConstraintLayout clStart;
    @BindView(R.id.vl_wave)
    VoiceLineView vlWave;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.tv_back)
    TextView tvBack;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_date_inquire);
        ButterKnife.bind(this);
        speak("您好,欢迎来到日期查询");
        initEvent();
        getDateData(tvDemo1.getText().toString().trim());
    }

    private void initEvent() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R.id.tv_demo1, R.id.tv_demo2, R.id.tv_demo3, R.id.iv_yuyin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_demo1:
                getDateData(tvDemo1.getText().toString().trim());
                break;
            case R.id.tv_demo2:
                getDateData(tvDemo2.getText().toString().trim());
                break;
            case R.id.tv_demo3:
                getDateData(tvDemo3.getText().toString().trim());
                break;
            case R.id.iv_yuyin:
                //语音识别-->请求数据-->解析返回结果
                MLVoiceSynthetize.stop();
                onEndOfSpeech();
                startListener();
                break;
        }
    }

    private boolean isStart;
    int recordTotalTime = 0;
    private Handler mainHandler = new Handler();

    private void startListener() {
        MLVoiceRecognize.initSpeechRecognizer(this).startListening(new RecognizerListener() {
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
                DateInquireActivity.this.onEndOfSpeech();
                textView4.setVisibility(View.VISIBLE);

            }

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                dealData(recognizerResult, b);
            }

            @Override
            public void onError(SpeechError speechError) {
                speak("您好,我没有听清,你能再说一遍吗?");
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

    @Override
    public void getData(String s) {
        getDateData(s);
    }

    private void getDateData(final String result) {
        XFSkillApi.getSkillData(result, new XFSkillApi.getDataListener() {

            @Override
            public void onSuccess(Object anwser, final String briefly, String service, String question) {
                if (!"datetime".equals(service)) {
                    speak("您好,没有查到该日期");
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count == 0) {
                            tvNotice.setText(briefly.substring(3));
                        }
                        //跳转页面显示结果
                        if (count > 0) {
                            DateInquireResultActivity.startMe(DateInquireActivity.this, result, briefly);
                        }
                        count++;
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopListening();
        stopSpeaking();
        mainHandler.removeCallbacksAndMessages(null);
    }
}
