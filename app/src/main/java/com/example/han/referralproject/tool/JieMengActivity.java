package com.example.han.referralproject.tool;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.speech.util.JsonParser;
import com.example.han.referralproject.tool.adapter.DreamRVadapter;
import com.example.han.referralproject.tool.xfparsebean.DreamBean;
import com.example.han.referralproject.voice.SpeechRecognizerHelper;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JieMengActivity extends BaseActivity {


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
    private List<DreamBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiemeng);
        ButterKnife.bind(this);
        speak("主人,欢迎来到周公解梦!");
    }


    private void startListener() {
        SpeechRecognizer speechRecognizer = SpeechRecognizerHelper.initSpeechRecognizer(this);
        speechRecognizer.startListening(new RecognizerListener() {
            @Override
            public void onVolumeChanged(int i, byte[] bytes) {

            }

            @Override
            public void onBeginOfSpeech() {

            }

            @Override
            public void onEndOfSpeech() {

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

    private void dealData(RecognizerResult recognizerResult, boolean isLast) {
        if (isLast) {
            getDreamData(printResult(recognizerResult).toString());
        }
    }

    private void getDreamData(final String result) {
        XFSkillApi.getSkillData(result, new XFSkillApi.getDataListener() {
            @Override
            public void onSuccess(final Object anwser, final String briefly) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        data.clear();
                        data.addAll((List<DreamBean>) anwser);
                        JieMengRetultActivity.startMe(JieMengActivity.this, data, result, briefly);
                    }
                });
            }

            @Override
            public void onSuccess(Object anwser) {

            }
        });
    }

    private HashMap<String, String> xfResult = new LinkedHashMap<String, String>();

    private StringBuffer printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        xfResult.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : xfResult.keySet()) {
            resultBuffer.append(xfResult.get(key));
        }
        return resultBuffer;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopListening();
    }

    @OnClick({R.id.tv_demo1, R.id.tv_demo2, R.id.tv_demo3, R.id.iv_yuyin, R.id.tv_title})
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
                startListener();
                break;
            case R.id.tv_title:
//                clStart.setVisibility(View.VISIBLE);
//                clDreamResult.setVisibility(View.GONE);
//                data.clear();
                startActivity(new Intent(this, DateInquireActivity.class));
                break;
        }
    }
}
