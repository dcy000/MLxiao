package com.example.han.referralproject.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.speech.util.JsonParser;
import com.example.han.referralproject.voice.SpeechRecognizerHelper;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class ToolBaseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    protected abstract void initView();

    public void dealData(RecognizerResult recognizerResult, boolean isLast) {
        StringBuffer stringBuffer = printResult(recognizerResult);
        if (isLast) {
            getData(stringBuffer.toString());
        }
    }

    public abstract void getData(String s);

    public HashMap<String, String> xfResult = new LinkedHashMap<>();

    public StringBuffer printResult(RecognizerResult results) {
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
