package com.example.han.referralproject.tool;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.speech.util.JsonParser;
import com.example.han.referralproject.tool.dialog.RiddleDialog;
import com.example.han.referralproject.tool.other.StringUtil;
import com.example.han.referralproject.tool.other.XFSkillApi;
import com.example.han.referralproject.tool.wrapview.VoiceLineView;
import com.example.han.referralproject.tool.xfparsebean.RiddleBean;
import com.example.han.referralproject.voice.SpeechRecognizerHelper;
import com.example.han.referralproject.voice.SpeechSynthesizerHelper;
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

public class RiddleActivity extends BaseActivity implements RiddleDialog.ShowNextListener {

    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.tv_show_anwser)
    TextView tvShowAnwser;
    @BindView(R.id.tv_show_next)
    TextView tvShowNext;
    @BindView(R.id.iv_yuyin)
    ImageView ivYuyin;
    @BindView(R.id.vl_wave)
    VoiceLineView vlWave;
    @BindView(R.id.tv_press_notice)
    TextView tvPressNotice;

    private int index;
    private List<RiddleBean> data;
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riddle);
        ButterKnife.bind(this);
        initData();
        speak("主人,欢迎来到猜谜");
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
            public void onSuccess(final Object anwser) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        data = (List<RiddleBean>) anwser;
                        if (!data.isEmpty()) {
                            size = data.size();
                            String title = data.get(0).title;
                            tvQuestion.setText(title);
                            SpeechSynthesizerHelper.startSynthesize(getBaseContext(), title);
                        }
                    }
                });

            }
        });
    }

    @OnClick({R.id.tv_show_anwser, R.id.tv_show_next, R.id.iv_yuyin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_show_anwser:
                showAnswer();
                break;
            case R.id.tv_show_next:
                showNext();
                break;
            case R.id.iv_yuyin:
                startListener();
                break;

        }
    }

    private void showAnswer() {
        RiddleDialog riddleDialog = new RiddleDialog();
        Bundle bundle = new Bundle();
        bundle.putString("answer", data.get(index % size).answer);
        riddleDialog.setArguments(bundle);
        riddleDialog.setListener(this);
        riddleDialog.show(getSupportFragmentManager(), "riddleDialog");
    }

    private void showNext() {
        index++;
        String title = data.get(index % size).title;
        tvQuestion.setText(title);
        SpeechSynthesizerHelper.stop();
        SpeechSynthesizerHelper.startSynthesize(this, title);

    }

    private boolean isStart;
    int recordTotalTime = 0;
    private Handler mainHandler = new Handler();

    private void showWave() {
        if (isStart) {
            return;
        }
        isStart = true;
        tvPressNotice.setVisibility(View.GONE);
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
                vlWave.setVisibility(View.GONE);
                tvPressNotice.setVisibility(View.VISIBLE);
                vlWave.stopRecord();
                isStart = false;
                recordTotalTime = 0;
                mainHandler.removeCallbacksAndMessages(null);
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
                String answer = data.get(index % size).answer;
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

    @Override
    public void onNext() {
        showNext();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopListening();
        SpeechSynthesizerHelper.stop();
    }
}
