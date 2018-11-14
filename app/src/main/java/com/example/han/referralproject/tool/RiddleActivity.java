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
import com.example.han.referralproject.util.PinYinUtils;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.recognition.MLRecognizerListener;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;

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
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_back)
    TextView tvBack;

    private int index;
    private List<RiddleBean> data;
    private int size;
    private String resultPinYin;
    private String anwserPinyin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riddle);
        ButterKnife.bind(this);
        initData();
        speak("主人,欢迎来到猜谜");
        initEvent();
    }

    private void initEvent() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 网络获取谜语数据
     */
    private void initData() {
        String[] miti = {"猜谜语", "来一个字谜"};
        Random random = new Random();

        XFSkillApi.getSkillData(miti[random.nextInt(2)], new XFSkillApi.getDataListener() {

            public void onSuccess(final Object anwser, final String anwserText, String service, String question) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        data = (List<RiddleBean>) anwser;
                        if (data != null && !data.isEmpty()) {
                            size = data.size();
                            String title = data.get(0).title;
                            tvQuestion.setText(title);
                            MLVoiceSynthetize.startSynthesize(getBaseContext(), title, false);
                        } else {
                            MLVoiceSynthetize.startSynthesize(getBaseContext(), "主人,我暂时还没想到什么谜语", false);
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
                endOfSpeech();
                MLVoiceSynthetize.stop();
                startListener();
                break;

        }
    }

    private void showAnswer() {
        if (data == null || data.size() == 0) {
//            ToastUtil.showShort(this, "主人,网络异常,请稍后重试");
            return;
        }
        RiddleDialog riddleDialog = new RiddleDialog();
        Bundle bundle = new Bundle();
        bundle.putString("answer", data.get(index % size).answer);
        riddleDialog.setArguments(bundle);
        riddleDialog.setListener(this);
        riddleDialog.show(getSupportFragmentManager(), "riddleDialog");
    }

    private void showNext() {
        if (data == null || data.size() == 0) {
            return;
        }
        index++;
        String title = data.get(index % size).title;
        tvQuestion.setText(title);
        MLVoiceSynthetize.stop();
        MLVoiceSynthetize.startSynthesize(this, title,false);

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
//        SpeechRecognizer speechRecognizer = SpeechRecognizerHelper.initSpeechRecognizer(this);
//        speechRecognizer.startListening(new RecognizerListener() {
//            @Override
//            public void onVolumeChanged(int i, byte[] bytes) {
//                vlWave.waveH = i / 6 + 2;
//            }
//
//            @Override
//            public void onBeginOfSpeech() {
//                showWave();
//            }
//
//            @Override
//            public void onEndOfSpeech() {
//                endOfSpeech();
//                tvPressNotice.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onResult(RecognizerResult recognizerResult, boolean b) {
//                dealData(recognizerResult, b);
//            }
//
//            @Override
//            public void onError(SpeechError speechError) {
//                speak("主人,我没听清,您能再说一遍吗");
//            }
//
//            @Override
//            public void onEvent(int i, int i1, int i2, Bundle bundle) {
//
//            }
//        });

        MLVoiceRecognize.startRecognize(this, new MLRecognizerListener() {
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
                endOfSpeech();
                tvPressNotice.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMLResult(String result) {
                doData(result);

            }

            @Override
            public void onMLError(SpeechError error) {
                speak("主人,我没听清,您能再说一遍吗");
            }
        });
    }

    private void doData(String result) {

        if (data == null || data.size() == 0) {
            return;
        }

        if (TextUtils.isEmpty(result)) {
            speak("主人,我没听清,您能再说一遍吗");
            return;
        }

        String answer = data.get(index % size).answer;

        try {

            resultPinYin = PinYinUtils.converterToSpell(result);

            //拦截处理 下一题 显示答案
            if (resultPinYin.matches(".*(xiayiti|xiayinti|xiayitin).*")) {
                tvShowNext.performClick();
                return;
            }

            if (resultPinYin.matches(".*(xianshidaan|xiansidaan|xiangshidaan|xiansidaan|xiansidaang).*")) {
                tvShowAnwser.performClick();
                return;
            }

            if (result.equals("下一题") || result.contains("下一题")) {
                tvShowNext.performClick();
                return;
            }

            if (result.equals("显示答案") || result.contains("显示答案") || result.contains("看答案")) {
                tvShowAnwser.performClick();
                return;
            }

            anwserPinyin = PinYinUtils.converterToSpell(answer);

            if (anwserPinyin.equals(resultPinYin) || anwserPinyin.contains(resultPinYin)) {
                speak("恭喜主人答对了");
                return;
            }

            if (answer.equals(result) || answer.contains(result)) {
                speak("恭喜主人答对了");
                return;
            }

            speak("主人,您再猜一下!");


        } catch (Exception e) {


            if (result.equals("下一题") || result.contains("下一题")) {
                tvShowNext.performClick();
                return;
            }

            if (result.equals("显示答案") || result.contains("显示答案") || result.contains("看答案")) {
                tvShowAnwser.performClick();
                return;
            }


            if (answer.equals(result) || answer.contains(result)) {
                speak("恭喜主人答对了");
                return;
            }
            speak("主人,您再猜一下!");

        }
    }

    private void endOfSpeech() {
        stopListening();
        vlWave.setVisibility(View.GONE);

        vlWave.stopRecord();
        isStart = false;
        recordTotalTime = 0;
        mainHandler.removeCallbacksAndMessages(null);
    }

    private void dealData(RecognizerResult recognizerResult, boolean isLast) {
        StringBuffer stringBuffer = printResult(recognizerResult);
        String result = stringBuffer.toString();


        if (isLast) {

            if (data == null || data.size() == 0) {
                return;
            }

            if (TextUtils.isEmpty(result)) {
                speak("主人,我没听清,您能再说一遍吗");
                return;
            }

            String answer = data.get(index % size).answer;

            try {

                resultPinYin = PinYinUtils.converterToSpell(result);

                //拦截处理 下一题 显示答案
                if (resultPinYin.matches(".*(xiayiti|xiayinti|xiayitin).*")) {
                    tvShowNext.performClick();
                    return;
                }

                if (resultPinYin.matches(".*(xianshidaan|xiansidaan|xiangshidaan|xiansidaan|xiansidaang).*")) {
                    tvShowAnwser.performClick();
                    return;
                }

                if (result.equals("下一题") || result.contains("下一题")) {
                    tvShowNext.performClick();
                    return;
                }

                if (result.equals("显示答案") || result.contains("显示答案") || result.contains("看答案")) {
                    tvShowAnwser.performClick();
                    return;
                }

                anwserPinyin = PinYinUtils.converterToSpell(answer);

                if (anwserPinyin.equals(resultPinYin) || anwserPinyin.contains(resultPinYin)) {
                    speak("恭喜主人答对了");
                    return;
                }

                if (answer.equals(result) || answer.contains(result)) {
                    speak("恭喜主人答对了");
                    return;
                }

                speak("主人,您再猜一下!");


            } catch (Exception e) {


                if (result.equals("下一题") || result.contains("下一题")) {
                    tvShowNext.performClick();
                    return;
                }

                if (result.equals("显示答案") || result.contains("显示答案") || result.contains("看答案")) {
                    tvShowAnwser.performClick();
                    return;
                }


                if (answer.equals(result) || answer.contains(result)) {
                    speak("恭喜主人答对了");
                    return;
                }
                speak("主人,您再猜一下!");

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
        MLVoiceSynthetize.stop();
        mainHandler.removeCallbacksAndMessages(null);
    }
}
