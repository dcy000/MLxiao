package com.medlink.danbogh.wakeup.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.gcml.lib_utils.data.StringUtil;
import com.gcml.lib_widget.VoiceLineView;
import com.iflytek.cloud.SpeechError;
import com.iflytek.recognition.MLRecognizerListener;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;

/**
 * Created by lenovo on 2018/8/29.
 */

public class VoiceDialog extends Dialog {
    private Context context;
    private ImageView yuYin;
    private TextView text;
    private VoiceLineView vlWave;

    public VoiceDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        addView();
    }

    private interface onResultListener {
        void onResult();
    }

    private onResultListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public VoiceDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        addView();
    }

    private void addView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_voice, null);
        initView(view);
        setContentView(view);

        setCancelable(true);
        setCanceledOnTouchOutside(true);

        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(params);
    }

    private void initView(View view) {
        yuYin = view.findViewById(R.id.iv_yuyin);
        text = view.findViewById(R.id.text);
        vlWave = view.findViewById(R.id.vl_voice_wave);

        yuYin.setOnClickListener(v -> {
            onYuyinClick();
        });

    }

    private void onYuyinClick() {
        MLVoiceRecognize.startRecognize(context, new MLRecognizerListener() {
            @Override
            public void onMLVolumeChanged(int i, byte[] bytes) {
                vlWave.waveH = i / 6 + 2;
            }

            @Override
            public void onMLBeginOfSpeech() {
                showWave();
            }

            @Override
            public void onMLEndOfSpeech() {
                VoiceDialog.this.onEndOfSpeech();
//                text.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMLResult(String result) {
                DataDealHelper helper = new DataDealHelper();
                helper.onDataAction(context, result);
                dismiss();
            }

            @Override
            public void onMLError(SpeechError error) {
                dismiss();
            }
        });
    }

    private void ondata(String result) {

    }

    private boolean isStart;
    int recordTotalTime = 0;
    private Handler mainHandler = new Handler();

    private void onEndOfSpeech() {
//        vlWave.setVisibility(View.GONE);
        vlWave.stopRecord();
        isStart = false;
        recordTotalTime = 0;
        mainHandler.removeCallbacksAndMessages(null);
    }


    private void showWave() {
        if (isStart) {
            return;
        }
        isStart = true;
//        text.setVisibility(View.GONE);
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

    @Override
    public void dismiss() {
        super.dismiss();
        MLVoiceRecognize.stop();
        MLVoiceSynthetize.stop();
        mainHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void show() {
        super.show();
        MLVoiceSynthetize.startSynthesize(context, "嗨我在~~", false);
        onYuyinClick();
    }
}
