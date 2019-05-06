package com.example.module_control_volume.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.module_control_volume.R;
import com.gcml.common.recommend.bean.get.KeyWordDefinevBean;
import com.gcml.common.utils.SharedPreferencesUtils;
import com.example.module_control_volume.wrap.FlowLayout;
import com.example.module_control_volume.wrap.ItemView;
import com.gcml.common.utils.PinYinUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.data.StringUtil;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.lib_widget.VoiceLineView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.recognition.JsonParser;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class SetKeyWord2Activity extends ToolbarBaseActivity implements View.OnClickListener, ItemView.IcClickListener {

    FlowLayout flow;
    VoiceLineView vlWave;
    ImageView imageView;
    TextView textView;
    private String title;
    private String titlePinyin;
    public HashMap<String, String> xfResult = new LinkedHashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_key_word2);
        flow=findViewById(R.id.flow);
        vlWave=findViewById(R.id.vl_wave);
        imageView=findViewById(R.id.imageView);
        textView=findViewById(R.id.textView);
        initTitle();
        initData();
        initFlowLayout();
    }
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
    public void dealData(RecognizerResult recognizerResult, boolean isLast) {
        StringBuffer stringBuffer = printResult(recognizerResult);
        if (isLast) {
            getData(stringBuffer.toString());
        }
    }
    public void getData(String s) {

        if (TextUtils.isEmpty(s)) {
            MLVoiceSynthetize.startSynthesize(UM.getApp(),"我没有听清你能再说一遍吗?");
            return;
        }
        if (containKeyWord(s, data)) {
            MLVoiceSynthetize.startSynthesize(UM.getApp(),"你已添加了这个关键词");
            return;
        }
        saveData(s);
        SharedPreferencesUtils.setParam(this, titlePinyin, new Gson().toJson(data));
        ToastUtils.showShort("保存关键词:" + s + "成功");
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
        MLVoiceSynthetize.startSynthesize(UM.getApp(),"请录入您的关键词");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
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
                MLVoiceSynthetize.startSynthesize(UM.getApp(),"我没听清,你能再说一遍吗");
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
