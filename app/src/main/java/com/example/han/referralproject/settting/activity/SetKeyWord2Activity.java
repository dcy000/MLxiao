package com.example.han.referralproject.settting.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.ToolBaseActivity;
import com.example.han.referralproject.settting.SharedPreferencesUtils;
import com.example.han.referralproject.settting.bean.KeyWordDefinevBean;
import com.example.han.referralproject.settting.wrap.FlowLayout;
import com.example.han.referralproject.settting.wrap.ItemView;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.tool.other.StringUtil;
import com.example.han.referralproject.tool.wrapview.VoiceLineView;
import com.gcml.lib_utils.display.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetKeyWord2Activity extends ToolBaseActivity implements View.OnClickListener, ItemView.IcClickListener {

    @BindView(R.id.flow)
    FlowLayout flow;
    @BindView(R.id.vl_wave)
    VoiceLineView vlWave;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.textView)
    TextView textView;
    private String title;
    private String titlePinyin;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_set_key_word2);
        ButterKnife.bind(this);
        initTitle();
        initData();
        initFlowLayout();
    }

    @Override
    public void getData(String s) {

        if (TextUtils.isEmpty(s)) {
            speak("主人,我没有听清你能再说一遍吗?");
            return;
        }
        if (containKeyWord(s, data)) {
            speak("主人,你已添加了这个关键词");
            return;
        }
        saveData(s);
        SharedPreferencesUtils.setParam(this, titlePinyin, new Gson().toJson(data));
        ToastUtils.showShort("主人,保存关键词:" + s + "成功");
    }

    private void saveData(String s) {
        KeyWordDefinevBean bean = new KeyWordDefinevBean();
        bean.name = s;
        bean.show = false;
        bean.pinyin = PinYinUtils.converterToSpell(s);
        bean.pinyin = bean.pinyin == null ? "" : bean.pinyin;
        data.add(bean);

        ItemView view = new ItemView(this);
        view.setText(s);
        view.showICon(false);
        view.setListener(this);
        flow.addView(view);
    }

    private boolean containKeyWord(String s, List<KeyWordDefinevBean> data) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).name.equals(s)) {
                return true;
            }
        }
        return false;
    }

    private void initFlowLayout() {
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                ItemView view = new ItemView(this);
                view.setText(data.get(i).name);
                view.showICon(false);
                view.setListener(this);
                flow.addView(view);
            }
        }
    }

    private List<KeyWordDefinevBean> data;

    private List<KeyWordDefinevBean> initData() {
        data = new ArrayList<>();
        refreshData();
        return data;
    }

    private void refreshData() {
        String jsonData = (String) SharedPreferencesUtils.getParam(this, titlePinyin, "");
        List<KeyWordDefinevBean> list = new Gson().fromJson(jsonData, new TypeToken<List<KeyWordDefinevBean>>() {
        }.getType());
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).show = false;
            }
            data.addAll(list);
        }
    }

    private void initTitle() {
        title = getIntent().getStringExtra("title");
        titlePinyin = PinYinUtils.converterToSpell(this.title);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText(title + "自定义");
        mRightText.setText("编辑");
        mRightView.setVisibility(View.GONE);
        mRightText.setOnClickListener(this);
        imageView.setOnClickListener(this);
        speak("主人,请录入您的关键词");
    }

    @Override
    public void onClick(View v) {
        if (v == mRightText) {
            KeyWordEdit2Activity.StartMe(this, data, titlePinyin);
        } else if (v == imageView) {
            MLVoiceSynthetize.stop();
            onEndOfSpeech();
            startListener();
        }

    }

    private void startListener() {
        //开始识别
        MLVoiceRecognize.initSpeechRecognizer(this).startListening(new RecognizerListener() {
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
                SetKeyWord2Activity.this.onEndOfSpeech();
                textView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                dealData(recognizerResult, b);
            }

            @Override
            public void onError(SpeechError speechError) {
                speak("主人,我没听清,你能再说一遍吗");
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

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
        textView.setVisibility(View.GONE);
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

    private void onEndOfSpeech() {
        vlWave.setVisibility(View.GONE);
        vlWave.stopRecord();
        isStart = false;
        recordTotalTime = 0;
        mainHandler.removeCallbacksAndMessages(null);
    }

    public static void StartMe(Context context, String title) {
        Intent intent = new Intent(context, SetKeyWord2Activity.class);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    public void onIcClick(View v) {
        flow.removeView(v);
    }

    @Override
    protected void onResume() {
        super.onResume();
        data.clear();
        flow.removeAllViews();
        refreshData();
        initFlowLayout();
    }
}
