package com.example.module_doctor_advisory.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.module_doctor_advisory.R;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.utils.PinYinUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.cloud.SpeechError;
import com.iflytek.recognition.MLRecognizerListener;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;

public class OfflineActivity extends ToolbarBaseActivity {

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_offline;
    }

    @Override
    public void initParams(Intent intentArgument) {
        MLVoiceRecognize.startRecognize(recognizerListener);
    }

    @Override
    public void initView() {
        mLeftText.setText("线下签约");
        mRightText.setText("暂不签约");
        mRightView.setVisibility(View.GONE);
        mRightText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backMainActivity();
                finish();
            }
        });
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getString(R.string.mp_user_help));
    }


    public static final String REGEX_BACK = ".*(fanhui|shangyibu).*";
    private MLRecognizerListener recognizerListener=new MLRecognizerListener() {
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
    };
}
