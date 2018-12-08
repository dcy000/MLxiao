package com.example.module_tools.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.module_tools.R;
import com.example.module_tools.R2;
import com.example.module_tools.dialog.CalculationDialog;
import com.example.module_tools.service.XFSkillApi;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gcml.lib_widget.voiceline.VoiceLineView;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.utils.StringUtil;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.recognition.JsonParser;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalculationActivity extends ToolbarBaseActivity {


    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_demo5)
    TextView tvDemo5;
    @BindView(R2.id.tv_demo4)
    TextView tvDemo4;
    @BindView(R2.id.tv_demo1)
    TextView tvDemo1;
    @BindView(R2.id.tv_demo2)
    TextView tvDemo2;
    @BindView(R2.id.tv_demo3)
    TextView tvDemo3;
    @BindView(R2.id.iv_yuyin)
    ImageView ivYuyin;
    @BindView(R2.id.textView4)
    TextView textView4;
    @BindView(R2.id.vl_wave)
    VoiceLineView vlWave;
    @BindView(R2.id.tv_back)
    TextView tvBack;
    @BindView(R2.id.cl_start)
    ConstraintLayout clStart;


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_calculation;
    }

    @Override
    public void initParams(Intent intentArgument) {
        MLVoiceSynthetize.startSynthesize("主人,欢迎来到计算");
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        initEvent();
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }

    private void initEvent() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @OnClick({R2.id.tv_demo5, R2.id.tv_demo4, R2.id.tv_demo1, R2.id.tv_demo2, R2.id.tv_demo3, R2.id.iv_yuyin})
    public void onViewClicked(View view) {
        int i = view.getId();
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

    private void getDreamData(final String result) {
        XFSkillApi.getSkillData(result, new XFSkillApi.getDataListener() {
//            @Override
//            public void onSuccess(final Object anwser, final String briefly) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ToastUtil.showShort(CalculationActivity.this,(String) anwser);
//                        speak((String) anwser);
//                    }
//                });
//
//            }

            @Override
            public void onSuccess(final Object anwser, final String anwserText, String service, String question) {
                if (!"calc".equals(service)) {
//                    ToastUtil.showShort(CalculationActivity.this, "主人,我不会算" + question);
                    MLVoiceSynthetize.startSynthesize("主人,我不会算了");
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
        SpeechRecognizer speechRecognizer = MLVoiceRecognize.initSpeechRecognizer();
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
                CalculationActivity.this.onEndOfSpeech();
                textView4.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                dealData(recognizerResult, b);
            }

            @Override
            public void onError(SpeechError speechError) {
                MLVoiceSynthetize.startSynthesize("主人,我没有听清,你能再说一遍吗?");
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }

    private void onEndOfSpeech() {
        vlWave.setVisibility(View.GONE);
        vlWave.stopRecord();
        isStart = false;
        recordTotalTime = 0;
        mainHandler.removeCallbacksAndMessages(null);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLVoiceRecognize.stop();
        MLVoiceSynthetize.stop();
        mainHandler.removeCallbacksAndMessages(null);
    }
}
