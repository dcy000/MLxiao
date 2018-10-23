package com.zhang.hui.lib_recreation.tool.activtiy;

import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.lib_widget.VoiceLineView;
import com.iflytek.cloud.SpeechError;
import com.iflytek.recognition.MLRecognizerListener;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.zhang.hui.lib_recreation.R;
import com.zhang.hui.lib_recreation.tool.other.StringUtil;
import com.zhang.hui.lib_recreation.tool.other.XFSkillApi;
import com.zhang.hui.lib_recreation.tool.xfparsebean.CookbookBean;

import java.util.List;

public class CookBookActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 菜谱
     */
    private TextView tvTitle;
    /**
     * 返回
     */
    private TextView tvBack;
    /**
     * 菜谱查询
     */
    private TextView tvNotice;
    /**
     * 您可以这样提问：
     */
    private TextView tvNoticeDemo;
    /**
     * 茄子怎么烧？
     */
    private TextView tvDemo1;
    /**
     * 干锅土豆。
     */
    private TextView tvDemo2;
    /**
     * 尖椒炒鸡蛋怎么做?
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
        setContentView(R.layout.activity_cook_book);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人,欢迎来到菜谱", false);
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvBack = (TextView) findViewById(R.id.tv_back);
        tvBack.setOnClickListener(this);
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
        if (i == R.id.tv_back) {
            finish();
        } else if (i == R.id.tv_demo1) {
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
                CookBookActivity.this.onEndOfSpeech();
                textView4.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMLResult(String result) {
                getDateData(result);
            }

            @Override
            public void onMLError(SpeechError error) {
                MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人,我不会算了", false);
            }
        });
    }

    private void getDateData(final String result) {
        XFSkillApi.getSkillData(result, new XFSkillApi.getDataListener() {
            @Override
            public void onSuccess(final Object anwser, final String anwserText, String service, String question) {
                if (!"cookbook".equals(service)) {
                    MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人,没有找到该菜谱", false);
                    return;
                }
                try {
                    CookBookResultActivity.StartMe(CookBookActivity.this, (List<CookbookBean>) anwser, result);
                } catch (Exception e) {
                    MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人,没有找到该菜谱", false);
                }
            }
        });
    }
}

