package com.example.han.referralproject.tool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.speech.util.JsonParser;
import com.example.han.referralproject.tool.xfparsebean.DreamBean;
import com.example.han.referralproject.voice.SpeechRecognizerHelper;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalculationActivity extends AppCompatActivity {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_demo5)
    TextView tvDemo5;
    @BindView(R.id.tv_demo4)
    TextView tvDemo4;
    @BindView(R.id.tv_demo1)
    TextView tvDemo1;
    @BindView(R.id.tv_demo2)
    TextView tvDemo2;
    @BindView(R.id.tv_demo3)
    TextView tvDemo3;
    @BindView(R.id.iv_yuyin)
    ImageView ivYuyin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.tv_title, R.id.tv_demo5, R.id.tv_demo4, R.id.tv_demo1, R.id.tv_demo2, R.id.tv_demo3, R.id.iv_yuyin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title:
//                finish();
                startActivity(new Intent(CalculationActivity.this,RiddleActivity.class) );
                break;
            case R.id.tv_demo5:
                String demo5 = tvDemo5.getText().toString();
                getDreamData(demo5);
                break;
            case R.id.tv_demo4:
                String demo4 = tvDemo4.getText().toString();
                getDreamData(demo4);
                break;
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
        }
    }

    private void getDreamData(final String result) {
        XFSkillApi.getSkillData(result, new XFSkillApi.getDataListener() {
            @Override
            public void onSuccess(final Object anwser, final String briefly) {

            }

            @Override
            public void onSuccess(final Object anwser) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CalculationDialog calculationDialog = new CalculationDialog();
                        Bundle bundle = new Bundle();
                        bundle.putString("question", result);
                        bundle.putString("answer", (String) anwser);
                        calculationDialog.setArguments(bundle);
                        calculationDialog.show(getSupportFragmentManager(), "calculation");
                    }
                });

            }
        });
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
        StringBuffer stringBuffer = printResult(recognizerResult);
        if (isLast) {
            getDreamData(stringBuffer.toString());
        }
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
}
