package com.example.han.referralproject.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.new_music.ScreenUtils;
import com.example.han.referralproject.speech.setting.IatSettings;
import com.example.han.referralproject.speech.setting.TtsSettings;
import com.example.han.referralproject.speech.util.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.wakeup.WakeupHelper;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected Resources mResources;
    private ProgressDialog mDialog;
    protected LayoutInflater mInflater;
    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认发音人
    private String voicer = "nannan";
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private SharedPreferences mTtsSharedPreferences;
    private SpeechRecognizer mIat;
    private Handler mDelayHandler = new Handler();
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private boolean enableListeningLoop = true;
    private boolean enableListeningLoopCache = enableListeningLoop;
    private LinearLayout rootView;
    private View mTitleView;
    protected TextView mTitleText;
    protected TextView mRightText;
    protected ImageView mLeftView;
    protected ImageView mRightView;
    protected TextView mLeftText;
    protected RelativeLayout mToolbar;
    protected LinearLayout mllBack;
    protected boolean isShowVoiceView = false;//是否显示声音录入图像
    private MediaRecorder mMediaRecorder;
    private boolean isAlive = true;
    public SharedPreferences mIatPreferences;


    public void setEnableListeningLoop(boolean enable) {
        enableListeningLoop = enable;
        enableListeningLoopCache = enableListeningLoop;
    }

    SpeechSynthesizer synthesizer;

    public void mlSpeak(String text) {
        MLVoiceSynthetize.startSynthesize(this, text, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mResources = getResources();
        mInflater = LayoutInflater.from(this);
        rootView = new LinearLayout(this);
        rootView.setOrientation(LinearLayout.VERTICAL);
        mTitleView = mInflater.inflate(R.layout.custom_title_layout, null);

        rootView.addView(mTitleView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (70 * mResources.getDisplayMetrics().density)));
        initToolbar();
        SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
        if (recognizer == null) {
            mIat = SpeechRecognizer.createRecognizer(this, mTtsInitListener);
        } else {
            mIat = recognizer;
        }
        mTtsSharedPreferences = getSharedPreferences(TtsSettings.PREFER_NAME, MODE_PRIVATE);
        mIatPreferences = getSharedPreferences(IatSettings.PREFER_NAME, MODE_PRIVATE);
    }


    private long lastTimeMillis = -1;
    private static final long DURATION = 500L;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            long currentTimeMillis = System.currentTimeMillis();
            if (lastTimeMillis != -1) {
                long elapsedTime = currentTimeMillis - lastTimeMillis;
                if (elapsedTime < DURATION) {
                    lastTimeMillis = currentTimeMillis;
                    return true;
                }
            }
            lastTimeMillis = currentTimeMillis;
        }
        return super.dispatchTouchEvent(ev);
    }


    private void initToolbar() {
        mllBack = (LinearLayout) mTitleView.findViewById(R.id.ll_back);
        mToolbar = (RelativeLayout) mTitleView.findViewById(R.id.toolbar);
        mTitleText = (TextView) mTitleView.findViewById(R.id.tv_top_title);
        mLeftText = (TextView) mTitleView.findViewById(R.id.tv_top_left);
        mRightText = (TextView) mTitleView.findViewById(R.id.tv_top_right);
        mLeftView = (ImageView) mTitleView.findViewById(R.id.iv_top_left);
        mRightView = (ImageView) mTitleView.findViewById(R.id.iv_top_right);
        mllBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backLastActivity();
            }
        });
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backMainActivity();
            }
        });
    }

    /**
     * 返回上一页
     */
    protected void backLastActivity() {
        finish();
    }

    /**
     * 返回到主页面
     */
    protected void backMainActivity() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    protected VoiceLineView voiceLineView;

    protected FrameLayout mContentParent;

    protected int provideWaveViewWidth() {
        return ScreenUtils.dp2px(450);
    }

    protected int provideWaveViewHeight() {
        return ScreenUtils.dp2px(120);
    }

    @Override
    public void setContentView(int layoutResID) {
        mInflater.inflate(layoutResID, rootView);
        super.setContentView(rootView);
        if (isShowVoiceView) {
            mContentParent = (FrameLayout) findViewById(android.R.id.content);
            voiceLineView = new VoiceLineView(this);
            voiceLineView.setBackgroundColor(Color.parseColor("#00000000"));
            voiceLineView.setAnimation(AnimationUtils.loadAnimation(BaseActivity.this, R.anim.popshow_anim));
            int width = provideWaveViewWidth();
            int height = provideWaveViewHeight();
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
            params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            mContentParent.addView(voiceLineView, params);
            mContentParent.bringToFront();
            voiceLineView.setVisibility(View.GONE);
        }

    }

    @Override
    public void setContentView(View view) {
        rootView.addView(view);
        super.setContentView(rootView);
        if (isShowVoiceView) {
            mContentParent = (FrameLayout) findViewById(android.R.id.content);
            voiceLineView = new VoiceLineView(this);
            voiceLineView.setBackgroundColor(Color.parseColor("#00000000"));
            int width = ScreenUtils.dp2px(450);
            int height = ScreenUtils.dp2px(120);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
            params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            params.bottomMargin = 20;
            mContentParent.addView(voiceLineView, params);
            mContentParent.bringToFront();
            voiceLineView.setVisibility(View.GONE);
        }
    }

    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                //    showTip("初始化失败,错误码：" + code);
            } else {
                // 设置参数
                setRecognizerParams();
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    public void speak(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        stopListening();
        synthesizer = SpeechSynthesizer.getSynthesizer();
        if (synthesizer == null) {
            synthesizer = SpeechSynthesizer.createSynthesizer(this, new SynthesizerInitListener(text));
            return;
        }
        setSynthesizerParams();
        synthesizer.startSpeaking(text, mTtsListener);
    }

    protected void speak(String text, boolean isDefaultParam) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        stopListening();
        synthesizer = SpeechSynthesizer.getSynthesizer();
        if (synthesizer == null) {
            synthesizer = SpeechSynthesizer.createSynthesizer(this, new SynthesizerInitListener(text));
            return;
        }
        if (isDefaultParam) {
            setSynthesizerParams();
        }

        synthesizer.startSpeaking(text, mTtsListener);
    }

    private class SynthesizerInitListener implements InitListener {
        private String mText;

        SynthesizerInitListener(String text) {
            mText = text;
        }

        @Override
        public void onInit(int code) {
            if (code == ErrorCode.SUCCESS) {
                setSynthesizerParams();
                if (!TextUtils.isEmpty(mText)) {
                    SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
                    if (synthesizer != null) {
                        synthesizer.startSpeaking(mText, mTtsListener);
                    }
                }
            }
        }
    }


    public void stopSpeaking() {

        if (synthesizer != null) {
            synthesizer.stopSpeaking();
        }


    }

    public void speak(int resId) {
        speak(getString(resId));
    }

    protected void startListening() {
        SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
        if (recognizer == null) {
            SpeechRecognizer.createRecognizer(this.getApplicationContext(), mTtsInitListener);
            recognizer = SpeechRecognizer.getRecognizer();
        }
        setRecognizerParams();
        SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
        if (enableListeningLoop && recognizer != null && !recognizer.isListening() && synthesizer != null && !synthesizer.isSpeaking()) {
            recognizer.startListening(mIatListener);
        }
    }

    protected void stopListening() {
        SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
        if (recognizer != null && recognizer.isListening()) {
            recognizer.stopListening();
            recognizer.cancel();
        }
    }

    protected void onSpeakListenerResult(String result) {
        //Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
//        T.show(result);
    }

    private boolean disableGlobalListen;

    public boolean isDisableGlobalListen() {
        return disableGlobalListen;
    }


    public void setDisableGlobalListen(boolean disableGlobalListen) {
        this.disableGlobalListen = disableGlobalListen;
        WakeupHelper.getInstance().enableWakeuperListening(!disableGlobalListen);
    }


    private Runnable updateVolumeAction = new Runnable() {
        @Override
        public void run() {
            if (mMediaRecorder == null) return;
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / 100;
            if (ratio > 1) {
                volume = (int) (20 * Math.log10(ratio));
            }
            voiceLineView.setVolume(volume);

        }
    };

    private volatile int volume;

    private RecognizerListener mIatListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            updateVolume();
        }

        @Override
        public void onBeginOfSpeech() {
//            showWaveView(true);
        }

        @Override
        public void onEndOfSpeech() {
//            showWaveView(false);
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean isLast) {
//            if (!isLast){
//                return;
//            }

            mIatResults.clear();
            String text = JsonParser.parseIatResult(recognizerResult.getResultString());
            String sn = null;
            // 读取json结果中的sn字段
            try {
                JSONObject resultJson = new JSONObject(recognizerResult.getResultString());
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mIatResults.put(sn, text);
            StringBuffer resultBuffer = new StringBuffer();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }
            String result = resultBuffer.toString();
            if (!TextUtils.isEmpty(result)) {
                onSpeakListenerResult(result);
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            Log.i("speak", "error          " + speechError.getErrorDescription());
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
        }
    };

    protected void showWaveView(boolean visible) {
        if (voiceLineView != null) {
            voiceLineView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    protected void updateVolume() {
        if (isShowVoiceView) {
            Handlers.ui().postDelayed(updateVolumeAction, 100);
        }
    }

    protected void setShowVoiceView(boolean showVoiceView) {
        isShowVoiceView = showVoiceView;
    }

    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            showWaveView(false);
        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (isShowVoiceView) {
                updateVolume();
            }

            onActivitySpeakFinish();
            if (error == null) {
            } else if (error != null) {
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    protected void onActivitySpeakFinish() {

    }


    private void setRecognizerParams() {
        SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
        if (recognizer != null) {
            // 清空参数
            recognizer.setParameter(SpeechConstant.PARAMS, null);

            // 设置听写引擎
            recognizer.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
            // 设置返回结果格式
            recognizer.setParameter(SpeechConstant.RESULT_TYPE, "json");

            String lag = mTtsSharedPreferences.getString("iat_language_preference", "mandarin");
            if (lag.equals("en_us")) {
                // 设置语言
                recognizer.setParameter(SpeechConstant.LANGUAGE, "en_us");
            } else {
                // 设置语言
                recognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
                // 设置语言区域
                recognizer.setParameter(SpeechConstant.ACCENT, lag);
            }

            // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
            recognizer.setParameter(SpeechConstant.VAD_BOS, mTtsSharedPreferences.getString("iat_vadbos_preference", "5000"));

            // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
            recognizer.setParameter(SpeechConstant.VAD_EOS, mTtsSharedPreferences.getString("iat_vadeos_preference", "500"));

            // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
            recognizer.setParameter(SpeechConstant.ASR_PTT, mTtsSharedPreferences.getString("iat_punc_preference", "0"));

            // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
            // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
            recognizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
            recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
        }
    }

    private String[] voicers;

    public String[] voicers() {
        if (voicers != null) {
            return voicers;
        }
        voicers = getResources().getStringArray(R.array.voicer_values);
        return voicers;
    }

    /**
     * 参数设置
     */
    private void setSynthesizerParams() {
        SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
        if (synthesizer != null) {
            // 清空参数
            synthesizer.setParameter(SpeechConstant.PARAMS, null);
            // 根据合成引擎设置相应参数
            if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
                synthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
                // 设置在线合成发音人
                String[] voicers = voicers();
                int index = mIatPreferences.getInt("language_index", 0);
                if (index >= voicers.length || index < 0) {
                    mIatPreferences.edit().putInt("language_index", 0).apply();
                    index = 0;
                }
                synthesizer.setParameter(SpeechConstant.VOICE_NAME, voicers[index]);
                //设置合成语速
                synthesizer.setParameter(SpeechConstant.SPEED, mTtsSharedPreferences.getString("speed_preference", "50"));
                //设置合成音调
                synthesizer.setParameter(SpeechConstant.PITCH, mTtsSharedPreferences.getString("pitch_preference", "50"));
                //设置合成音量
                synthesizer.setParameter(SpeechConstant.VOLUME, mTtsSharedPreferences.getString("volume_preference", "50"));
                synthesizer.setParameter(SpeechConstant.SAMPLE_RATE, mTtsSharedPreferences.getString("rate_preference", "16000"));
            } else {
                synthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
                // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
                synthesizer.setParameter(SpeechConstant.VOICE_NAME, voicer);
                /**
                 * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
                 * 开发者如需自定义参数，请参考在线合成参数设置
                 */
            }
            //设置播放器音频流类型
            synthesizer.setParameter(SpeechConstant.STREAM_TYPE, mTtsSharedPreferences.getString("stream_preference", "3"));
            // 设置播放合成音频打断音乐播放，默认为true
            synthesizer.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

            // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
            // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
            synthesizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
            synthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
        }
    }

    Handler handler = MyApplication.getInstance().getBgHandler();
    public Runnable mListening = new Runnable() {
        @Override
        public void run() {
            startListening();
            if (enableListeningLoop) {
                handler.postDelayed(mListening, 200);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        enableListeningLoop = enableListeningLoopCache;
        setDisableGlobalListen(disableGlobalListen);
        if (enableListeningLoop) {
            handler.postDelayed(mListening, 200);
        }
    }

    @Override
    protected void onPause() {
        SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
        if (synthesizer != null && synthesizer.isSpeaking()) {
            synthesizer.stopSpeaking();
        }
        enableListeningLoopCache = enableListeningLoop;
        enableListeningLoop = false;
        handler.removeCallbacks(mListening);
        SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
        if (recognizer != null && recognizer.isListening()) {
            recognizer.stopListening();
        }
        if (mMediaRecorder != null) {
            isAlive = false;
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        //释放通知消息的资源
        Handlers.ui().removeCallbacks(updateVolumeAction);
        MobclickAgent.onPause(this);
        super.onPause();
    }

    public void showLoadingDialog(String message) {
        try {
            if (mDialog == null) {
                mDialog = new ProgressDialog(mContext);
//            mDialog.setCanceledOnTouchOutside(false);
                mDialog.setCanceledOnTouchOutside(true);
                mDialog.setIndeterminate(true);
                mDialog.setMessage(message);
            }
            mDialog.show();
        } catch (Exception e) {

        }
    }


    public void hideLoadingDialog() {
        if (mDialog == null) {
            return;
        }
        mDialog.dismiss();
    }

}