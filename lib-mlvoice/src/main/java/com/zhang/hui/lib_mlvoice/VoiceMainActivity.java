package com.zhang.hui.lib_mlvoice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.zhang.hui.lib_mlvoice.recognition.MLRecognizerListener;
import com.zhang.hui.lib_mlvoice.recognition.MLVoiceRecognize;
import com.zhang.hui.lib_mlvoice.synthetize.MLVoiceSynthetize;

public class VoiceMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_main);
        TextView read = findViewById(R.id.read);
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MLVoiceSynthetize.startSynthesize(VoiceMainActivity.this, "哈哈", false);
//                MLVoiceRecognize.startRecognize(VoiceMainActivity.this, new MLRecognizerListener() {
//                    @Override
//                    public void onMLVolumeChanged(int i, byte[] bytes) {
//
//                    }
//
//                    @Override
//                    public void onMLBeginOfSpeech() {
//
//                    }
//
//                    @Override
//                    public void onMLEndOfSpeech() {
//
//                    }
//
//                    @Override
//                    public void onMLResult(String result) {
//
//                    }
//
//                    @Override
//                    public void onMLError(SpeechError error) {
//
//                    }
//                });
            }
        });
    }
}
