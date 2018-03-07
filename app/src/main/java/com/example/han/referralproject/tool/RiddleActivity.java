package com.example.han.referralproject.tool;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.speech.util.JsonParser;
import com.example.han.referralproject.tool.xfparsebean.RiddleBean;
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
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RiddleActivity extends BaseActivity {

    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.tv_show_anwser)
    TextView tvShowAnwser;
    @BindView(R.id.tv_show_next)
    TextView tvShowNext;
    @BindView(R.id.iv_yuyin)
    ImageView ivYuyin;

    private int index;
    private List<RiddleBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riddle);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 网络获取谜语数据
     */
    private void initData() {
        String[] miti = {"猜谜语", "来一个字谜"};
        Random random = new Random();

        XFSkillApi.getSkillData(miti[random.nextInt(2)], new XFSkillApi.getDataListener() {

            @Override
            public void onSuccess(Object anwser, String briefly) {

            }

            @Override
            public void onSuccess(Object anwser) {
                data = (List<RiddleBean>) anwser;
                tvQuestion.setText(data.get(0).title);
            }
        });
    }

    @OnClick({R.id.tv_show_anwser, R.id.tv_show_next, R.id.iv_yuyin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_show_anwser:

                break;
            case R.id.tv_show_next:
                index++;
                tvQuestion.setText(data.get(index).title);
                break;
            case R.id.iv_yuyin:
                startListener();
                break;
        }
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
        String result = stringBuffer.toString();

        if (isLast) {

            if (TextUtils.isEmpty(result)) {
                speak("主人,我没听清,您能再说一遍吗");
                return;
            }

            if (!data.isEmpty()) {
                String answer = data.get(index).answer;
                if (answer.equals(result) || answer.contains(result)) {
                    speak("答对了!,您答对了");
                } else {
                    speak("主人,您再猜一下哦!");
                }
            }
        }
    }

    private HashMap<String, String> xfResult = new LinkedHashMap<>();

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
