package com.zhang.hui.lib_recreation.tool.activtiy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.gcml.lib_utils.display.ToastUtils;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.recognition.LoopInterface;
import com.iflytek.recognition.MLRecognizerListener;
import com.iflytek.recognition.MLVoiceRecognize;
import com.zhang.hui.lib_recreation.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class LoopListenerActivity extends AppCompatActivity implements View.OnClickListener {


    /**
     * 开始听
     */
    private TextView mTextView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop_listener);
        initView();
        MLVoiceRecognize.startRecognize(getApplicationContext(), (RecognizerListener) null);
    }

    private void statListener() {
        MLVoiceRecognize.startLoopRecognize(getApplicationContext(), new LoopInterface() {
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
            public void onResult(String result) {
                ToastUtils.showShort(result);
            }

            @Override
            public void onError(SpeechError error) {
                ToastUtils.showShort(error.getErrorCode() + "");
            }
        });

    }


    private void initView() {
        mTextView2 = (TextView) findViewById(R.id.textView2);
        mTextView2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.textView2:
                try {
                    statListener();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


}
