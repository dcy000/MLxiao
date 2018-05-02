package com.example.han.referralproject.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.jipush.MyReceiver;
import com.example.han.referralproject.new_music.ScreenUtils;
import com.example.han.referralproject.speech.setting.IatSettings;
import com.example.han.referralproject.speech.setting.TtsSettings;
import com.example.han.referralproject.speech.util.JsonParser;
import com.example.han.referralproject.util.Utils;
import com.example.han.referralproject.util.WeakHandler;
import com.github.mmin18.widget.RealtimeBlurView;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.wakeup.WakeupHelper;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class BaseActivity extends AppCompatActivity {
    private static UpdateVolumeRunnable updateVolumeRunnable;
    protected Context mContext;
    protected Resources mResources;
    private ProgressDialog mDialog;
    protected LayoutInflater mInflater;
    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认发音人
    private static final String voicer = "nannan";
    // 引擎类型
    private static final String mEngineType = SpeechConstant.TYPE_CLOUD;
    private static SharedPreferences mTtsSharedPreferences;
    private SpeechRecognizer mIat;
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
    private static MediaRecorder mMediaRecorder;
    private boolean isAlive = true;
    public static SharedPreferences mIatPreferences;
    private SynthesizerInitListener synthesizerInitListener1;
    private SpeechSynthesizer synthesizer;
    private SynthesizerInitListener synthesizerInitListener2;
    private long lastTimeMillis = -1;
    private static final long DURATION = 500L;
    private ImpRecognizerListener recognizerListener;
    private ImpSynthesizerListener synthesizerListener1;
    private ImpSynthesizerListener synthesizerListener2;
    private ImpSynthesizerListener synthesizerListener3;
    protected VoiceLineView voiceLineView;
    protected FrameLayout mContentParent;
    private WeakHandler weakHandler;
    public void setEnableListeningLoop(boolean enable) {
        enableListeningLoop = enable;
        enableListeningLoopCache = enableListeningLoop;
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
            mIat = SpeechRecognizer.createRecognizer(this, new ImpInitListener());
        } else {
            mIat = recognizer;
        }
        mTtsSharedPreferences = getSharedPreferences(TtsSettings.PREFER_NAME, MODE_PRIVATE);
        mIatPreferences = getSharedPreferences(IatSettings.PREFER_NAME, MODE_PRIVATE);
        weakHandler=new WeakHandler();
    }

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

    //收到推送消息后显示Popwindow
    private static class JPushReceive implements MyReceiver.JPushLitener {
        private WeakReference<Activity> weakContext;

        public JPushReceive(Activity context) {
            weakContext = new WeakReference<Activity>(context);
        }

        @Override
        public void onReceive(String title, String message) {
//            ToastUtil.showShort(BaseActivity.this,message);
            // 利用layoutInflater获得View
            LayoutInflater inflater = (LayoutInflater) weakContext.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.jpush_popwin, null);
            PopupWindow window = new PopupWindow(view,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
            window.setFocusable(true);

            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0x00000000);
            window.setBackgroundDrawable(dw);
            backgroundAlpha(weakContext.get(), 1f);
            // 设置popWindow的显示和消失动画
            window.setAnimationStyle(R.style.mypopwindow_anim_style);
//            // 在底部显示

            window.showAtLocation(weakContext.get().getWindow().getDecorView(),
                    Gravity.TOP, 0, 148);

            //popWindow消失监听方法
            window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(weakContext.get(), 1f);
                }
            });
            TextView jpushText = view.findViewById(R.id.jpush_text);
            TextView jpushTitle = view.findViewById(R.id.jpush_title);
            TextView jpushTime = view.findViewById(R.id.jpush_time);
            if (!TextUtils.isEmpty(title)) {
                jpushTitle.setVisibility(View.VISIBLE);
                jpushTitle.setText(title);
            }
            jpushText.setText(message);
            jpushTime.setText(Utils.stampToDate2(System.currentTimeMillis()));

            final LinearLayout jpushLl = view.findViewById(R.id.jpush_ll);
            final RealtimeBlurView jpushRbv = view.findViewById(R.id.jpush_rbv);
            ViewTreeObserver vto = jpushLl.getViewTreeObserver();
            final ViewGroup.LayoutParams lp = jpushRbv.getLayoutParams();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    jpushLl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    int width=jpushLl.getMeasuredWidth();
                    int height = jpushLl.getMinimumHeight();
                    lp.height = height;
                    jpushRbv.setLayoutParams(lp);
                }
            });
            ((BaseActivity) weakContext.get()).speak("主人，新消息。" + message);
        }
    }

    /**
     * 调节屏幕透明度
     *
     * @param context
     * @param bgAlpha
     */
    protected static void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
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

    private static class ImpInitListener implements InitListener {
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
    }

//    private InitListener mTtsInitListener = new InitListener() {
//        @Override
//        public void onInit(int code) {
//            if (code != ErrorCode.SUCCESS) {
//                //    showTip("初始化失败,错误码：" + code);
//            } else {
//                // 设置参数
//                setRecognizerParams();
//                // 初始化成功，之后可以调用startSpeaking方法
//                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
//                // 正确的做法是将onCreate中的startSpeaking调用移至这里
//            }
//        }
//    };

    public void speak(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        stopListening();
        synthesizer = SpeechSynthesizer.getSynthesizer();
        if (synthesizer == null) {
            synthesizerInitListener1 = new SynthesizerInitListener(text, this);
            synthesizer = SpeechSynthesizer.createSynthesizer(this, synthesizerInitListener1);
            return;
        }
        setSynthesizerParams(this);
        synthesizerListener1 = new ImpSynthesizerListener();
        synthesizer.startSpeaking(text, synthesizerListener1);
    }

    protected void speak(String text, boolean isDefaultParam) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        stopListening();
        synthesizer = SpeechSynthesizer.getSynthesizer();
        if (synthesizer == null) {
            synthesizerInitListener2 = new SynthesizerInitListener(text, this);
            synthesizer = SpeechSynthesizer.createSynthesizer(this, synthesizerInitListener2);
            return;
        }
        if (isDefaultParam) {
            setSynthesizerParams(this);
        }

        synthesizerListener2 = new ImpSynthesizerListener();
        synthesizer.startSpeaking(text, synthesizerListener2);
    }

    private class SynthesizerInitListener implements InitListener {
        private String mText;
        private Activity activity;

        SynthesizerInitListener(String text, Activity activity) {
            mText = text;
            this.activity = activity;
        }

        @Override
        public void onInit(int code) {
            if (code == ErrorCode.SUCCESS) {
                setSynthesizerParams(activity);
                if (!TextUtils.isEmpty(mText)) {
                    SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
                    if (synthesizer != null) {
                        synthesizerListener3 = new ImpSynthesizerListener();
                        synthesizer.startSpeaking(mText, synthesizerListener3);
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

    protected void speak(int resId) {
        speak(getString(resId));
    }

    protected void startListening() {
        SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
        if (recognizer == null) {
            SpeechRecognizer.createRecognizer(this.getApplicationContext(), new ImpInitListener());
            recognizer = SpeechRecognizer.getRecognizer();
        }
        setRecognizerParams();
        SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
        if (enableListeningLoop && recognizer != null && !recognizer.isListening() && synthesizer != null && !synthesizer.isSpeaking()) {
            recognizerListener = new ImpRecognizerListener();
            recognizer.startListening(recognizerListener);
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

    private static class UpdateVolumeRunnable implements Runnable {
        private WeakReference<VoiceLineView> weakVoiceline;

        public UpdateVolumeRunnable(VoiceLineView voiceLineView) {
            weakVoiceline = new WeakReference<VoiceLineView>(voiceLineView);
        }

        @Override
        public void run() {
            if (mMediaRecorder == null) return;
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / 100;
            if (ratio > 1) {
                volume = (int) (20 * Math.log10(ratio));
            }
            weakVoiceline.get().setVolume(volume);
        }
    }

    private static volatile int volume;

    private class ImpRecognizerListener implements RecognizerListener {

        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            updateVolume(voiceLineView);
        }

        @Override
        public void onBeginOfSpeech() {
            showWaveView(true);
        }

        @Override
        public void onEndOfSpeech() {
            showWaveView(false);
        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
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

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    }

    protected void showWaveView(boolean visible) {
        if (voiceLineView != null) {
            voiceLineView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    protected void updateVolume(VoiceLineView voiceLineView) {
        if (isShowVoiceView) {
            updateVolumeRunnable = new UpdateVolumeRunnable(voiceLineView);
            weakHandler.postDelayed(updateVolumeRunnable,100);
//            Handlers.ui().postDelayed(updateVolumeRunnable, 100);
        }
    }

    protected void setShowVoiceView(boolean showVoiceView) {
        isShowVoiceView = showVoiceView;
    }

    //    private static class ImpSynthesizerListener implements SynthesizerListener{
//
//        @Override
//        public void onSpeakBegin() {
//            showWaveView(false);
//        }
//
//        @Override
//        public void onBufferProgress(int i, int i1, int i2, String s) {
//
//        }
//
//        @Override
//        public void onSpeakPaused() {
//
//        }
//
//        @Override
//        public void onSpeakResumed() {
//
//        }
//
//        @Override
//        public void onSpeakProgress(int i, int i1, int i2) {
//
//        }
//
//        @Override
//        public void onCompleted(SpeechError speechError) {
//            if (isShowVoiceView) {
//                updateVolume();
//            }
//
//            onActivitySpeakFinish();
//        }
//
//        @Override
//        public void onEvent(int i, int i1, int i2, Bundle bundle) {
//
//        }
//    }
    private class ImpSynthesizerListener implements SynthesizerListener {

        @Override
        public void onSpeakBegin() {
            showWaveView(false);
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {

        }

        @Override
        public void onSpeakPaused() {

        }

        @Override
        public void onSpeakResumed() {

        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {

        }

        @Override
        public void onCompleted(SpeechError speechError) {
            if (isShowVoiceView) {
                updateVolume(voiceLineView);
            }
            onActivitySpeakFinish();
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    }

//    private SynthesizerListener mTtsListener = new SynthesizerListener() {
//
//        @Override
//        public void onSpeakBegin() {
//            showWaveView(false);
//        }
//
//        @Override
//        public void onSpeakPaused() {
//
//        }
//
//        @Override
//        public void onSpeakResumed() {
//        }
//
//        @Override
//        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
//        }
//
//        @Override
//        public void onSpeakProgress(int percent, int beginPos, int endPos) {
//        }
//
//        @Override
//        public void onCompleted(SpeechError error) {
//            if (isShowVoiceView) {
//                updateVolume(voiceLineView);
//            }
//
//            onActivitySpeakFinish();
//            if (error == null) {
//            } else if (error != null) {
//            }
//        }
//
//        @Override
//        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
//        }
//    };

    protected void onActivitySpeakFinish() {

    }


    private static void setRecognizerParams() {
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

//    private static String[] voicers;
//
//    public static String[] voicers() {
//        if (voicers != null) {
//            return voicers;
//        }
//        voicers = getResources().getStringArray(R.array.voicer_values);
//        return voicers;
//    }

    /**
     * 参数设置
     */
    private static void setSynthesizerParams(Activity activity) {
        SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
        if (synthesizer != null) {
            // 清空参数
            synthesizer.setParameter(SpeechConstant.PARAMS, null);
            // 根据合成引擎设置相应参数
            if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
                synthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
                // 设置在线合成发音人
                String[] voicers = activity.getResources().getStringArray(R.array.voicer_values);
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
        MyReceiver.jPushLitener = new JPushReceive(this);
        enableListeningLoop = enableListeningLoopCache;
        setDisableGlobalListen(disableGlobalListen);
        if (enableListeningLoop) {
            handler.postDelayed(mListening, 200);
        }
    }

    @Override
    protected void onPause() {
        synthesizerInitListener1 = null;
        synthesizerInitListener2 = null;
        SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
        if (synthesizer != null && synthesizer.isSpeaking()) {
            synthesizer.stopSpeaking();
            synthesizerListener1=null;
            synthesizerListener2=null;
            synthesizerListener3=null;
        }
        enableListeningLoopCache = enableListeningLoop;
        enableListeningLoop = false;
        handler.removeCallbacks(mListening);
        SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
        if (recognizer != null && recognizer.isListening()) {
            recognizer.stopListening();
            recognizerListener = null;
        }
        if (mMediaRecorder != null) {
            isAlive = false;
            mMediaRecorder.release();
            mMediaRecorder = null;
            isShowVoiceView=false;
        }
        //释放通知消息的资源
        weakHandler.removeCallbacksAndMessages(null);
        if (MyReceiver.jPushLitener != null) {
            MyReceiver.jPushLitener = null;

        }
        MobclickAgent.onPause(this);
        super.onPause();
    }

    public void showLoadingDialog(String message) {
        if (mDialog == null) {
            mDialog = new ProgressDialog(mContext);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(message);
        }
        mDialog.show();
    }


    public void hideLoadingDialog() {
        if (mDialog == null) {
            return;
        }
        mDialog.dismiss();
    }
}