package com.example.han.referralproject.settting.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.ToolBaseActivity;
import com.example.han.referralproject.settting.SharedPreferencesUtils;
import com.example.han.referralproject.settting.adapter.KeyWordDifineRVAdapter;
import com.example.han.referralproject.settting.bean.KeyWordDefinevBean;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.tool.other.StringUtil;
import com.example.han.referralproject.tool.wrapview.VoiceLineView;
import com.gcml.common.utils.display.ToastUtils;
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
import butterknife.OnClick;

public class SetKeyWordActivity extends ToolBaseActivity implements KeyWordDifineRVAdapter.DeleteClickListener, View.OnClickListener {


    @BindView(R.id.iv_yuyin)
    ImageView ivYuyin;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.rv_keys)
    RecyclerView rvKeys;
    @BindView(R.id.vl_wave)
    VoiceLineView vlWave;
    private boolean flag;
    private String title;
    private List<KeyWordDefinevBean> data;
    private String titlePinyin;
    private KeyWordDifineRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_set_key_word);
        ButterKnife.bind(this);
        initTitle();
        initData();
        initRV();
        speak("你可以自定义" + title + "关键词");
    }

    private void initTitle() {
        title = getIntent().getStringExtra("title");
        titlePinyin = PinYinUtils.converterToSpell(this.title);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText(title + "自定义");
        mRightText.setText("编辑");
        mRightView.setVisibility(View.GONE);
        mRightText.setOnClickListener(this);
        speak("请录入您的关键词");
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

    @OnClick(R.id.iv_yuyin)
    public void onViewClicked() {
        MLVoiceSynthetize.stop();
        onEndOfSpeech();
        startListener();

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
                speak("我没听清,你能再说一遍吗");
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

    /**
     * 处理语音识别的数据
     *
     * @param recognizerResult
     */
    @Override
    public void getData(String recognizerResult) {
        KeyWordDefinevBean bean = new KeyWordDefinevBean();
        bean.name = recognizerResult;
        bean.show = false;
        data.add(bean);
        adapter.notifyDataSetChanged();
        SharedPreferencesUtils.setParam(this, titlePinyin, new Gson().toJson(data));
        ToastUtils.showShort( "保存:" + recognizerResult + "成功");
        speak("保存:" + recognizerResult + "关键词成功");

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
        KeyWordEditActivity.StartMe(this, data, titlePinyin);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopListening();
        stopSpeaking();
    }
}
