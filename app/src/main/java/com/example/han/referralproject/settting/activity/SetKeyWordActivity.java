package com.example.han.referralproject.settting.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.ToolBaseActivity;
import com.example.han.referralproject.settting.SharedPreferencesUtils;
import com.example.han.referralproject.settting.bean.KeyWordBean;
import com.example.han.referralproject.tool.CookBookResultActivity;
import com.example.han.referralproject.tool.xfparsebean.CookbookBean;
import com.example.han.referralproject.util.ToastUtil;
import com.example.han.referralproject.voice.SpeechRecognizerHelper;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;

import java.io.Serializable;
import java.security.Key;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetKeyWordActivity extends ToolBaseActivity {

    @BindView(R.id.et_keyword)
    EditText etKeyword;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_set_key_word);
        ButterKnife.bind(this);
        initTitle();
    }

    private void initTitle() {
        String title = getIntent().getStringExtra("title");
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText(title+"自定义");
        speak("主人,请录入您的关键词");
    }

    @OnClick(R.id.tv_confirm)
    public void onViewClicked() {
        //开始识别
        SpeechRecognizerHelper.initSpeechRecognizer(this).startListening(new RecognizerListener() {
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

    public void dealData(RecognizerResult recognizerResult, boolean isLast) {
        StringBuffer stringBuffer = printResult(recognizerResult);
        if (isLast) {
            getData(stringBuffer.toString());
        }
    }

    @Override
    public void getData(String recognizerResult) {
        etKeyword.setText(recognizerResult);
        KeyWordBean bean = new KeyWordBean();
        bean.yueya = recognizerResult;
        SharedPreferencesUtils.setParam(this, "keyword", bean);
        ToastUtil.showShort(this, recognizerResult);

        flag = (Boolean) SharedPreferencesUtils.getParam(SetKeyWordActivity.this, "yuyin", false);
        if (flag) {
            SharedPreferencesUtils.setParam(SetKeyWordActivity.this, "yuyin", false);
        } else {
            SharedPreferencesUtils.setParam(SetKeyWordActivity.this, "yuyin", true);
        }

    }

    public static void StartMe(Context context, String title) {
        Intent intent = new Intent(context, SetKeyWordActivity.class);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }


}
