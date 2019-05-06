package com.example.module_control_volume.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.module_control_volume.R;
import com.example.module_control_volume.adapter.KeyWordDifineRVAdapter;
import com.gcml.common.recommend.bean.get.KeyWordDefinevBean;
import com.gcml.common.utils.SharedPreferencesUtils;
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

import static com.iflytek.recognition.MLVoiceRecognize.stopListening;

public class SetKeyWordActivity extends ToolbarBaseActivity implements KeyWordDifineRVAdapter.DeleteClickListener, View.OnClickListener {


    ImageView ivYuyin;
    TextView tvNotice;
    TextView textView7;
    RecyclerView rvKeys;
    VoiceLineView vlWave;
    private boolean flag;
    private String title;
    private List<KeyWordDefinevBean> data;
    private String titlePinyin;
    private KeyWordDifineRVAdapter adapter;
    public HashMap<String, String> xfResult = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_key_word);
        ivYuyin=findViewById(R.id.iv_yuyin);
        tvNotice=findViewById(R.id.tv_notice);
        textView7=findViewById(R.id.textView7);
        rvKeys=findViewById(R.id.rv_keys);
        vlWave=findViewById(R.id.vl_wave);
        ivYuyin.setOnClickListener(this);
        initTitle();
        initData();
        initRV();
        MLVoiceSynthetize.startSynthesize(UM.getApp(),"你可以自定义" + title + "关键词");
    }


    private void initTitle() {
        title = getIntent().getStringExtra("title");
        titlePinyin = PinYinUtils.converterToSpell(this.title);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText(title + "自定义");
        mRightText.setText("编辑");
        mRightView.setVisibility(View.GONE);
        mRightText.setOnClickListener(this);
        MLVoiceSynthetize.startSynthesize(UM.getApp(),"请录入您的关键词");
    }

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

    private void initRV() {
        rvKeys.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new KeyWordDifineRVAdapter(R.layout.item_key_define, data);
        adapter.setListener(this);
        rvKeys.setAdapter(adapter);
    }


    private boolean isStart;
    int recordTotalTime = 0;
    private Handler mainHandler = new Handler();

    private void showWave() {
        if (isStart) {
            return;
        }
        isStart = true;
        tvNotice.setVisibility(View.GONE);
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
                SetKeyWordActivity.this.onEndOfSpeech();
                tvNotice.setVisibility(View.VISIBLE);
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

    private void onEndOfSpeech() {
        vlWave.setVisibility(View.GONE);
        vlWave.stopRecord();
        isStart = false;
        recordTotalTime = 0;
        mainHandler.removeCallbacksAndMessages(null);
    }


    public void dealData(RecognizerResult recognizerResult, boolean isLast) {
        StringBuffer stringBuffer = printResult(recognizerResult);
        if (isLast) {
            getData(stringBuffer.toString());
        }
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
    /**
     * 处理语音识别的数据
     *
     * @param recognizerResult
     */
    public void getData(String recognizerResult) {
        KeyWordDefinevBean bean = new KeyWordDefinevBean();
        bean.name = recognizerResult;
        bean.show = false;
        data.add(bean);
        adapter.notifyDataSetChanged();
        SharedPreferencesUtils.setParam(this, titlePinyin, new Gson().toJson(data));
        ToastUtils.showShort( "保存:" + recognizerResult + "成功");
        MLVoiceSynthetize.startSynthesize(UM.getApp(),"保存:" + recognizerResult + "关键词成功");
//        flag = (Boolean) SharedPreferencesUtils.getParam(SetKeyWordActivity.this, "yuyin", false);
//        if (flag) {
//            SharedPreferencesUtils.setParam(SetKeyWordActivity.this, "yuyin", false);
//        } else {
//            SharedPreferencesUtils.setParam(SetKeyWordActivity.this, "yuyin", true);
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        data.clear();
        refreshData();
        adapter.notifyDataSetChanged();
    }

    public static void StartMe(Context context, String title) {
        Intent intent = new Intent(context, SetKeyWordActivity.class);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    public void onDeleteClick(int position) {
        data.remove(position);
        //点击删除回调
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
//        TextView view = (TextView) v;
//        String right = view.getText().toString().trim();
//        if ("编辑".equals(right)) {
//            view.setText("完成");
//            mTitleText.setText("关键字编辑");
//            //点击编辑回调
//            for (int i = 0; i < data.size(); i++) {
//                data.get(i).showShort = false;
//            }
//            adapter.notifyDataSetChanged();
//
//        } else {
//            view.setText("编辑");
//            mTitleText.setText(title + "自定义");
//            finish();
//        }
        super.onClick(v);
        if (v.getId()==R.id.iv_yuyin){
            MLVoiceSynthetize.stop();
            onEndOfSpeech();
            startListener();
        }
        KeyWordEditActivity.StartMe(this, data, titlePinyin);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopListening();
        MLVoiceSynthetize.stop();
    }
}
