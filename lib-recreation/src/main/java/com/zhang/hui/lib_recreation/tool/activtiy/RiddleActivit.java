package com.zhang.hui.lib_recreation.tool.activtiy;

import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gcml.lib_utils.PinYinUtils;
import com.gcml.lib_utils.data.StringUtil;
import com.gcml.lib_widget.VoiceLineView;
import com.iflytek.cloud.SpeechError;
import com.iflytek.recognition.MLRecognizerListener;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.zhang.hui.lib_recreation.R;
import com.zhang.hui.lib_recreation.tool.dialog.RiddleDialog;
import com.zhang.hui.lib_recreation.tool.other.XFSkillApi;
import com.zhang.hui.lib_recreation.tool.xfparsebean.RiddleBean;

import java.util.List;
import java.util.Random;

public class RiddleActivit extends AppCompatActivity implements View.OnClickListener, RiddleDialog.ShowNextListener {

    private TextView tvQuestion;
    /**
     * 显示答案
     */
    private TextView tvShowAnwser;
    /**
     * 下一题
     */
    private TextView tvShowNext;
    /**
     * 请按下回答
     */
    private TextView tvPressNotice;
    private ImageView ivYuyin;
    private ConstraintLayout constraintLayout2;
    /**
     * 猜谜语
     */
    private TextView tvTitle;
    /**
     * 返回
     */
    private TextView tvBack;
    private VoiceLineView vlWave;

    private int index;
    private List<RiddleBean> data;
    private int size;
    private String resultPinYin;
    private String anwserPinyin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riddle);
        initView();
        initData();
    }

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
                            MLVoiceSynthetize.startSynthesize(getBaseContext(), "您好,我暂时还没想到什么谜语", false);
                        }
                    }
                });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "您好,欢迎来到猜谜", false);
    }

    private void initView() {
        tvQuestion = (TextView) findViewById(R.id.tv_question);
        tvShowAnwser = (TextView) findViewById(R.id.tv_show_anwser);
        tvShowAnwser.setOnClickListener(this);
        tvShowNext = (TextView) findViewById(R.id.tv_show_next);
        tvShowNext.setOnClickListener(this);
        tvPressNotice = (TextView) findViewById(R.id.tv_press_notice);
        ivYuyin = (ImageView) findViewById(R.id.iv_yuyin);
        ivYuyin.setOnClickListener(this);
        constraintLayout2 = (ConstraintLayout) findViewById(R.id.constraintLayout2);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvBack = (TextView) findViewById(R.id.tv_back);
        vlWave = (VoiceLineView) findViewById(R.id.vl_wave);

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_show_anwser) {
            showAnswer();
        } else if (i == R.id.tv_show_next) {
            showNext();
        } else if (i == R.id.iv_yuyin) {
            endOfSpeech();
            MLVoiceSynthetize.stop();
            startListener();
        }
    }

    private void endOfSpeech() {
        MLVoiceRecognize.stop();
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
                MLVoiceSynthetize.startSynthesize(getApplicationContext(), "您好,我没听清,您能再说一遍吗", false);
            }
        });
    }

    private boolean isStart;
    int recordTotalTime = 0;
    private Handler mainHandler = new Handler();



    private void doData(String result) {

        if (data == null || data.size() == 0) {
            return;
        }

        if (TextUtils.isEmpty(result)) {
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "您好,我没听清,您能再说一遍吗", false);
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
                speak("恭喜你答对了");
                return;
            }

            if (answer.equals(result) || answer.contains(result)) {
                speak("恭喜你答对了");
                return;
            }

            speak("您好,您再猜一下!");


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
                speak("恭喜你答对了");
                return;
            }
            speak("您好,您再猜一下!");

        }
    }

    private void speak(String text) {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), text, false);
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
    private void showAnswer() {
        if (data == null || data.size() == 0) {
//            ToastUtil.showShort(this, "您好,网络异常,请稍后重试");
            return;
        }
        RiddleDialog riddleDialog = new RiddleDialog();
        Bundle bundle = new Bundle();
        bundle.putString("answer", data.get(index % size).answer);
        riddleDialog.setArguments(bundle);
        riddleDialog.setListener(this);
        riddleDialog.show(getSupportFragmentManager(), "riddleDialog");
    }

    @Override
    public void onNext() {
        showNext();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceSynthetize.stop();
        MLVoiceRecognize.stop();
        mainHandler.removeCallbacksAndMessages(null);
    }
}
