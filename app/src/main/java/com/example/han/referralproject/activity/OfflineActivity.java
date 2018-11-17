package com.example.han.referralproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.util.PinYinUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.cloud.SpeechError;
import com.iflytek.recognition.MLRecognizerListener;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;

public class OfflineActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        mToolbar.setVisibility(View.VISIBLE);
        mLeftText.setText("线下签约");
        mRightText.setText("暂不签约");
        mRightView.setVisibility(View.GONE);
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OfflineActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        listener();
    }

    private void listener() {
        MLVoiceRecognize.startRecognize(new MLRecognizerListener() {
            @Override
            public void onMLVolumeChanged(int i, byte[] bytes) {

            }

            @Override
            public void onMLBeginOfSpeech() {

            }

            @Override
            public void onMLEndOfSpeech() {

            }

            @Override
            public void onMLResult(String result) {
                ToastUtils.showShort(result);
                String inSpell = PinYinUtils.converterToSpell(result);

                if (!TextUtils.isEmpty(inSpell) && inSpell.matches(REGEX_BACK)) {
                    finish();
                }
            }

            @Override
            public void onMLError(SpeechError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getString(R.string.user_help));
    }


    public static final String REGEX_BACK = ".*(fanhui|shangyibu).*";

}
