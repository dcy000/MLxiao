package com.example.han.referralproject.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.han.referralproject.speech.setting.TtsSettings;
import com.example.han.referralproject.speech.util.JsonParser;
import com.example.han.referralproject.util.Utils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.medlink.danbogh.wakeup.WakeupHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class BaseActivity extends AppCompatActivity implements Runnable{
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
    private boolean enableListeningLoop;
    private LinearLayout rootView;
    private View mTitleView;
    protected TextView mTitleText;
    protected TextView mRightText;
    protected ImageView mLeftView;
    protected ImageView mRightView;
    protected TextView mLeftText;
    protected RelativeLayout mToolbar;
    protected LinearLayout mllBack;
    private boolean isShowVoiceView=false;//是否显示声音录入图像
    private MediaRecorder mMediaRecorder;
    private boolean isAlive=true;
    private VoiceLineView voiceLineView;
    private View mFootView;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    public void setEnableListeningLoop(boolean enable) {
        enableListeningLoop = enable;
    }

    SpeechSynthesizer synthesizer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mResources = getResources();
        mInflater = LayoutInflater.from(this);
        rootView = new LinearLayout(this);
        rootView.setOrientation(LinearLayout.VERTICAL);
        mTitleView = mInflater.inflate(R.layout.custom_title_layout, null);
        mFootView=mInflater.inflate(R.layout.voiceinput_popwindow,null);

        rootView.addView(mTitleView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (70 * mResources.getDisplayMetrics().density)));
        initToolbar();
        enableListeningLoop = true;
        SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
        if (recognizer == null) {
            mIat = SpeechRecognizer.createRecognizer(this, mTtsInitListener);
        } else {
            mIat = recognizer;
        }
        SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
        if (synthesizer == null) {
            mTts = SpeechSynthesizer.createSynthesizer(mContext, mTtsInitListener);
        } else {
            mTts = synthesizer;
        }
        mTtsSharedPreferences = getSharedPreferences(TtsSettings.PREFER_NAME, MODE_PRIVATE);

        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        File file = new File(Environment.getExternalStorageDirectory().getPath(), "hello.log");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mMediaRecorder.setOutputFile(file.getAbsolutePath());
        mMediaRecorder.setMaxDuration(1000 * 60 * 10);
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaRecorder.start();
    }


    private void initToolbar() {
        mllBack= (LinearLayout) mTitleView.findViewById(R.id.ll_back);
        mToolbar= (RelativeLayout) mTitleView.findViewById(R.id.toolbar);
        mTitleText = (TextView) mTitleView.findViewById(R.id.tv_top_title);
        mLeftText= (TextView) mTitleView.findViewById(R.id.tv_top_left);
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
    protected void backLastActivity(){
        finish();
    }

    /**
     * 返回到主页面
     */
    protected void backMainActivity(){
        startActivity(new Intent(mContext,MainActivity.class));
        finish();
    }
    @Override
    public void setContentView(int layoutResID) {
        mInflater.inflate(layoutResID, rootView);
        super.setContentView(rootView);
    }

    @Override
    public void setContentView(View view) {
        rootView.addView(view);
        super.setContentView(rootView);
//        FrameLayout mContentParent = (FrameLayout) findViewById(android.R.id.content);
//        View view1 = new View(this);
//        view1.setBackgroundColor(Color.parseColor("#ff0000"));
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(500, 120);
//
//        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
//        params.bottomMargin =10;
//        mContentParent.addView(view1,params);
    }

    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                //    showTip("初始化失败,错误码：" + code);
            } else {
                // 设置参数
                setSynthesizerParams();
                setRecognizerParams();
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };


    protected void speak(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        stopListening();
        synthesizer = SpeechSynthesizer.getSynthesizer();
        if (synthesizer == null) {
            synthesizer = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        }
        setSynthesizerParams();

        synthesizer.startSpeaking(text, mTtsListener);
    }

    public void stopSpeaking() {

        if (synthesizer != null) {
            synthesizer.stopSpeaking();
        }


    }

    protected void speak(int resId) {
        speak(getString(resId));
//        stopListening();
//        setSynthesizerParams();
//        mTts.startSpeaking(getString(resId), mTtsListener);

//        mDelayHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 500);
    }

    protected void startListening() {
        setRecognizerParams();
        SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
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

    private RecognizerListener mIatListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {
            if(isShowVoiceView){
                showPopwindow();
            }

        }

        @Override
        public void onBeginOfSpeech() {

        }

        @Override
        public void onEndOfSpeech() {
            if(dialog!=null){
                dialog.dismiss();
            }
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
                if(result.matches(".*(冰镇|病症|自查|制茶|只差|直插|之差).*")){
                    startActivity(new Intent(BaseActivity.this,BodychartActivity.class));
                    return;
                }
                if(result.matches(".*(返回|反悔).*")){
                    finish();
                    return;
                }
                if(result.matches(".*(首页|守夜|授业).*")){
                    startActivity(new Intent(BaseActivity.this,MainActivity.class));
                    finish();
                    return;
                }
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

    protected void showPopwindow() {

        if(dialog==null){
            builder=new AlertDialog.Builder(this);
            // 利用layoutInflater获得View
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.voiceinput_popwindow, null);
            builder.setView(view);
            dialog=builder.create();
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);
            dialog.getWindow().setDimAmount(0f);
            Window window = dialog.getWindow();  // 获取Dialog的Window对象
            WindowManager.LayoutParams lp = window.getAttributes(); // 这个是主要设置对象
            window.setGravity(Gravity.BOTTOM); // 设置窗体位置，还可以有LEFT,TOP,RIGHT,BOTTOM
            window.setAttributes(lp); // 最后重新设置
            dialog.show();

            voiceLineView= (VoiceLineView) view.findViewById(R.id.voicLine);
            Thread thread = new Thread(this);
            thread.start();

        }else{
            if(!dialog.isShowing()){
                dialog.show();
            }
        }
    }

    protected void hidePopwindow(){
        if (dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    protected void setShowVoiceView(boolean showVoiceView) {
        isShowVoiceView = showVoiceView;
    }

    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            if (dialog != null) {
                dialog.dismiss();
            }
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
            if(isShowVoiceView){
                showPopwindow();
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
                synthesizer.setParameter(SpeechConstant.VOICE_NAME, voicer);
                //设置合成语速
                synthesizer.setParameter(SpeechConstant.SPEED, mTtsSharedPreferences.getString("speed_preference", "50"));
                //设置合成音调
                synthesizer.setParameter(SpeechConstant.PITCH, mTtsSharedPreferences.getString("pitch_preference", "50"));
                //设置合成音量
                synthesizer.setParameter(SpeechConstant.VOLUME, mTtsSharedPreferences.getString("volume_preference", "50"));
            } else {
                synthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
                // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
                synthesizer.setParameter(SpeechConstant.VOICE_NAME, "");
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
        enableListeningLoop = true;
        setDisableGlobalListen(false);
        handler.postDelayed(mListening, 200);
    }

    @Override
    protected void onPause() {
        SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
        if (synthesizer != null && synthesizer.isSpeaking()) {
            synthesizer.stopSpeaking();
        }
        enableListeningLoop = false;
        handler.removeCallbacks(mListening);
        SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
        if (recognizer != null && recognizer.isListening()) {
            recognizer.stopListening();
        }
        super.onPause();
    }

    protected void showLoadingDialog(String message) {
        if (mDialog == null) {
            mDialog = new ProgressDialog(mContext);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setIndeterminate(true);
            mDialog.setMessage(message);
        }
        mDialog.show();
    }


    protected void hideLoadingDialog() {
        if (mDialog == null) {
            return;
        }
        mDialog.dismiss();
    }

    @Override
    public void run() {
        while (isAlive) {
            handler1.sendEmptyMessage(0);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        isAlive = false;
        mMediaRecorder.release();
        mMediaRecorder = null;
        super.onDestroy();

    }
    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mMediaRecorder==null) return;
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / 100;
            Log.e("音量大小",ratio+"");
            double db = 0;// 分贝
            //默认的最大音量是100,可以修改，但其实默认的，在测试过程中就有不错的表现
            //你可以传自定义的数字进去，但需要在一定的范围内，比如0-200，就需要在xml文件中配置maxVolume
            //同时，也可以配置灵敏度sensibility
            if (ratio > 1)
                db = 20 * Math.log10(ratio);
            //只要有一个线程，不断调用这个方法，就可以使波形变化
            //主要，这个方法必须在ui线程中调用
            voiceLineView.setVolume((int) (db));
        }
    };
}