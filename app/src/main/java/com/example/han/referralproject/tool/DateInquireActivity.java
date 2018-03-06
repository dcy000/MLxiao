package com.example.han.referralproject.tool;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.speech.util.JsonParser;
import com.example.han.referralproject.voice.SpeechRecognizerHelper;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DateInquireActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_inquire);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_title, R.id.tv_demo1, R.id.tv_demo2, R.id.tv_demo3, R.id.iv_yuyin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title:
                startActivity(new Intent(this, CookBookActivity.class));
                break;
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
                startListener();
                break;
        }
    }

    private void startListener() {
        SpeechRecognizerHelper.initSpeechRecognizer(this).startListening(new RecognizerListener() {
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

    private HashMap<String, String> xfResult = new LinkedHashMap<>();

    private void dealData(RecognizerResult recognizerResult, boolean isLast) {
        if (isLast) {
            getDateData(printResult(recognizerResult).toString());
        }

    }

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

    private void getDateData(final String result) {
        XFSkillApi.getSkillData(result, new XFSkillApi.getDataListener() {
            @Override
            public void onSuccess(final Object anwser, final String briefly) {
            }

            @Override
            public void onSuccess(final Object briefly) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        tvNotice.setText((String) briefly);
                        //跳转页面显示结果
                        DateInquireResultActivity.startMe(DateInquireActivity.this,result, (String) briefly);
                    }
                });
            }
        });
    }
}
