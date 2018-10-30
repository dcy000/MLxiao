package com.zhang.hui.lib_recreation.tool.activtiy;

import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.lib_widget.VoiceLineView;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.recognition.MLRecognizerListener;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.zhang.hui.lib_recreation.R;
import com.zhang.hui.lib_recreation.tool.dialog.CalculationDialog;
import com.zhang.hui.lib_recreation.tool.other.StringUtil;
import com.zhang.hui.lib_recreation.tool.other.XFSkillApi;

public class CalculationActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 计算
     */
    private TextView mTvTitle;
    /**
     * 返回
     */
    private TextView mTvBack;
    /**
     * 5÷2等于多少？
     */
    private TextView mTvDemo5;
    /**
     * 2+2+2+2+2-3/4=？
     */
    private TextView mTvDemo4;
    /**
     * 数值计算
     */
    private TextView mTvNotice;
    /**
     * 您可以这样提问：
     */
    private TextView mTvNoticeDemo;
    /**
     * 45的三次方等于多少？
     */
    private TextView mTvDemo1;
    /**
     * 根号76等于多少？
     */
    private TextView mTvDemo2;
    /**
     * 45与53的差是多少？
     */
    private TextView mTvDemo3;
    private ImageView mIvYuyin;
    /**
     * 按下请说话
     */
    private TextView mTextView4;
    private VoiceLineView mVlWave;
    private ConstraintLayout mClStart;
    /**
     * 计算
     */
    private TextView tvTitle;
    /**
     * 返回
     */
    private TextView tvBack;
    /**
     * 5÷2等于多少？
     */
    private TextView tvDemo5;
    /**
     * 2+2+2+2+2-3/4=？
     */
    private TextView tvDemo4;
    /**
     * 数值计算
     */
    private TextView tvNotice;
    /**
     * 您可以这样提问：
     */
    private TextView tvNoticeDemo;
    /**
     * 45的三次方等于多少？
     */
    private TextView tvDemo1;
    /**
     * 根号76等于多少？
     */
    private TextView tvDemo2;
    /**
     * 45与53的差是多少？
     */
    private TextView tvDemo3;
    private ImageView ivYuyin;
    /**
     * 按下请说话
     */
    private TextView textView4;
    private VoiceLineView vlWave;
    private ConstraintLayout clStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);
        initView();
        initEvent();
    }

    private void initEvent() {
        tvBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人,欢迎来到计算", false);
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvBack = (TextView) findViewById(R.id.tv_back);
        tvDemo5 = (TextView) findViewById(R.id.tv_demo5);
        tvDemo5.setOnClickListener(this);
        tvDemo4 = (TextView) findViewById(R.id.tv_demo4);
        tvDemo4.setOnClickListener(this);
        tvNotice = (TextView) findViewById(R.id.tv_notice);
        tvNoticeDemo = (TextView) findViewById(R.id.tv_notice_demo);
        tvDemo1 = (TextView) findViewById(R.id.tv_demo1);
        tvDemo1.setOnClickListener(this);
        tvDemo2 = (TextView) findViewById(R.id.tv_demo2);
        tvDemo2.setOnClickListener(this);
        tvDemo3 = (TextView) findViewById(R.id.tv_demo3);
        tvDemo3.setOnClickListener(this);
        ivYuyin = (ImageView) findViewById(R.id.iv_yuyin);
        ivYuyin.setOnClickListener(this);
        textView4 = (TextView) findViewById(R.id.textView4);
        vlWave = (VoiceLineView) findViewById(R.id.vl_wave);
        clStart = (ConstraintLayout) findViewById(R.id.cl_start);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_demo5) {
            String demo5 = tvDemo5.getText().toString();
            getDreamData(demo5);
        } else if (i == R.id.tv_demo4) {
            String demo4 = tvDemo4.getText().toString();
            getDreamData(demo4);
        } else if (i == R.id.tv_demo1) {
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

    private void startListener() {
        MLVoiceRecognize.startRecognize(getApplicationContext(), new MLRecognizerListener() {
            @Override
            public void onMLVolumeChanged(int i, byte[] bytes) {
                vlWave.waveH = i / 6 + 2;
            }

            @Override
            public void onMLBeginOfSpeech() {
                showWave();
            }

            @Override
            public void onMLEndOfSpeech() {
                CalculationActivity.this.onEndOfSpeech();
                textView4.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMLResult(String result) {
                getDreamData(result);
            }

            @Override
            public void onMLError(SpeechError error) {
                MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人,我不会算了", false);
            }
        });
    }


    private void getDreamData(final String result) {
        XFSkillApi.getSkillData(result, new XFSkillApi.getDataListener() {

            @Override
            public void onSuccess(final Object anwser, final String anwserText, String service, String question) {
                if (!"calc".equals(service)) {
//                    ToastUtil.showShort(CalculationActivity.this, "主人,我不会算" + question);
                    MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人,我不会算了", false);
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        CalculationDialog calculationDialog = new CalculationDialog();
                        Bundle bundle = new Bundle();
                        bundle.putString("question", result);
                        bundle.putString("answer", anwserText);
                        calculationDialog.setArguments(bundle);
                        calculationDialog.show(getSupportFragmentManager(), "calculation");
                    }
                });

            }
        });


    }

    private boolean isStart;
    int recordTotalTime = 0;
    private Handler mainHandler = new Handler();

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
}


