package com.zhang.hui.lib_mlvoice.ui;

import android.animation.Animator;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.example.lenovo.rto.accesstoken.AccessToken;
import com.example.lenovo.rto.accesstoken.AccessTokenModel;
import com.example.lenovo.rto.http.HttpListener;
import com.example.lenovo.rto.sharedpreference.EHSharedPreferences;
import com.example.lenovo.rto.unit.Unit;
import com.example.lenovo.rto.unit.UnitModel;
import com.gcml.lib_utils.ui.ScreenUtils;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.zhang.hui.lib_mlvoice.R;
import com.zhang.hui.lib_mlvoice.other.QaApi;
import com.zhang.hui.lib_mlvoice.other.StringUtil;
import com.zhang.hui.lib_mlvoice.recognition.MLRecognizerListener;
import com.zhang.hui.lib_mlvoice.recognition.MLVoiceRecognize;
import com.zhang.hui.lib_mlvoice.synthetize.MLVoiceSynthetize;
import com.zhang.hui.lib_mlvoice.utils.SharedPreferencesUtils;
import com.zhang.hui.lib_mlvoice.utils.ToastTool;
import com.zhang.hui.lib_mlvoice.wrap.LineView;

import java.util.HashMap;
import java.util.Random;

import static com.example.lenovo.rto.Constans.ACCESSTOKEN_KEY;
import static com.example.lenovo.rto.Constans.SCENE_Id;

public class TokeActivity extends AppCompatActivity implements View.OnClickListener, HttpListener<AccessToken> {
    private SpeechRecognizer mIat;
    private RelativeLayout mRelativeLayout;
    private AudioManager mAudioManager;
    private ImageView ivBack;
    private Random rand;
    private ImageView mImageView;
    private LottieAnimationView mLottieView;
    private TextView voiceNormal;
    private TextView voiceWhine;
    private boolean isDefaultParam = false;
    private HashMap<String, String> results;
    private ImageView yuyin;
    private LineView lineWave;
    private Boolean yuyinFlag;
    private boolean isStart;
    private TextView notice;
    private AccessToken data;
    private String sessionId = "";
    private StringBuilder sb;

    private int maxVolume = 0;
    private int volume = 0;
    private VoiceLineView voiceLineView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolBar();
        setContentView(R.layout.activity_toke);
        initLayout();
        initAudio();
        mIat = MLVoiceRecognize.initSpeechRecognizer(getApplicationContext());
        initBDToken();
        yuyinFlag = (Boolean) SharedPreferencesUtils.getParam(this, "yuyin", true);
        if (yuyinFlag) {
            mHandler.sendEmptyMessageDelayed(1, 3000);
            yuyin.setVisibility(View.GONE);
            notice.setVisibility(View.GONE);
        } else {
            mImageView.setVisibility(View.GONE);
        }
    }

    private void initAudio() {
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        rand = new Random();
    }

    private void initToolBar() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initBDToken() {
        AccessTokenModel tokenModel = new AccessTokenModel();
        tokenModel.getAccessToken(this);
    }

    @Override
    public void onSuccess(AccessToken data) {
        EHSharedPreferences.WriteInfo(ACCESSTOKEN_KEY, data);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onComplete() {

    }

    private boolean isStoped = false;
    private boolean loopListen = false;

    @Override
    protected void onStart() {
        super.onStart();
        isStoped = false;
    }

    public String recogerResult;

    @Override
    protected void onResume() {
        super.onResume();
        speak("主人,来和我聊天吧", isDefaultParam);
        addWaveView();
        mLottieView.resumeAnimation();
        if (yuyinFlag) {
            //正在说不识别
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    stopSpeaking();
                    SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
                    if (!(synthesizer != null && synthesizer.isSpeaking())) {
                        MLVoiceRecognize.startRecognize(getApplicationContext(), mRecognizerListener);
                    }
                    mHandler.postDelayed(this, 300);
                }
            }, 300);
        }
    }

    @Override
    protected void onPause() {
        mLottieView.pauseAnimation();
        mHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }


    private void startAnim() {
        mRelativeLayout.post(action);
    }

    private int animationType;

    Runnable action = new Runnable() {

        Random mRandom = new Random();

        @Override
        public void run() {
            mLottieView.clearAnimation();
            int i = mRandom.nextInt(3);
            switch (animationType) {
                case -1:
                    // no answer
                    switch (i) {
                        case 0:
                            mLottieView.setAnimation("no_answer.json");
                            break;
                        case 1:
                            mLottieView.setAnimation("angry.json");
                            break;
                        case 2:
                            mLottieView.setAnimation("shy.json");
                            break;
                        default:
                            mLottieView.setAnimation("no_answer.json");
                            break;
                    }
                    break;
                default:
                    // animationType = 0
                    mLottieView.setAnimation("default.json");
                    break;
            }
            mLottieView.playAnimation();
        }
    };

    /**
     * 初始化Layout
     */
    private void initLayout() {
        mRelativeLayout = findViewById(R.id.Rela);
        mImageView = findViewById(R.id.iat_recognizes);
        voiceNormal = findViewById(R.id.tv_normal);
        voiceWhine = findViewById(R.id.tv_whine);
        yuyin = findViewById(R.id.iv_yuyin);
        notice = findViewById(R.id.tv_notice);
        lineWave = findViewById(R.id.vl_wave);
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        voiceNormal.setOnClickListener(this);
        voiceWhine.setOnClickListener(this);
        yuyin.setOnClickListener(this);
        lineWave.setOnClickListener(this);
        mLottieView = findViewById(R.id.animation_view);
        mLottieView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (animationType != 0) {
                    mLottieView.clearAnimation();
                    mLottieView.setAnimation("default.json");
                    animationType = 0;
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                animationType = 0;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.iat_recognizes) {
            clickRecognize();
        } else if (viewId == R.id.tv_normal) {
            MLVoiceSynthetize.setRandomParam();
            isDefaultParam = false;
        } else if (viewId == R.id.tv_whine) {
            isDefaultParam = true;
        } else if (viewId == R.id.iv_yuyin) {
            onEndOfSpeech();
            notice.setVisibility(View.GONE);
            clickRecognize();
        } else if (viewId == R.id.iv_back) {
            finish();
        }

    }

    private void clickRecognize() {
        MLVoiceSynthetize.stop();
        setParam();
        MLVoiceRecognize.startRecognize(getApplicationContext(), mRecognizerListener);
    }

    int recordTotalTime = 0;

    private void onEndOfSpeech() {
        lineWave.setVisibility(View.GONE);
        lineWave.stopRecord();
        notice.setVisibility(View.VISIBLE);
        isStart = false;
        recordTotalTime = 0;
        mHandler.removeCallbacksAndMessages(null);
    }


    private void showWave() {
        if (isStart) {
            return;
        }
        isStart = true;
        lineWave.setVisibility(View.VISIBLE);
        lineWave.setText("00:00");
        lineWave.startRecord();
        mHandler.removeCallbacksAndMessages(null);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recordTotalTime += 1000;
                updateTimerUI(recordTotalTime);
                mHandler.postDelayed(this, 1000);
            }
        }, 1000);

    }

    private void updateTimerUI(int recordTotalTime) {
        String string = String.format("%s", StringUtil.formatTime(recordTotalTime));
        lineWave.setText(string);
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new MLRecognizerListener() {
        @Override
        public void onMLVolumeChanged(int i, byte[] bytes) {
            if (yuyinFlag) {
                updateVolume();
            } else {
                lineWave.waveH = volume / 6 + 2;
            }
        }

        @Override
        public void onMLBeginOfSpeech() {
            if (yuyinFlag) {
                showWaveView(true);
            } else {
                showWave();
            }
        }

        @Override
        public void onMLEndOfSpeech() {
            if (yuyinFlag) {
                showWaveView(false);
            } else {
                TokeActivity.this.onEndOfSpeech();
            }
        }

        @Override
        public void onMLResult(String result) {
            recogerResult = result;
            ToastTool.showShort(result);
            new SayTask().execute();
        }

        @Override
        public void onMLError(SpeechError error) {
            if (yuyinFlag) {
                findViewById(R.id.iat_recognizes).performClick();
            } else {
                speak("主人,我没听清您能再说一遍吗", isDefaultParam);
            }

        }
    };

    private void updateVolume() {

    }


    private void showWaveView(boolean showWaveView) {
        if (voiceLineView != null) {
            voiceLineView.setVisibility(showWaveView ? View.VISIBLE : View.GONE);
        }
    }


    String str1;

    private void post(String str) {
        if (str.matches("(.*)是谁")) {
            str = "百科" + str.substring(0, str.length() - 2);
        }

        results = QaApi.getQaFromXf(str);
        String text = results.get("text");
        boolean empty = TextUtils.isEmpty(text);
        if (!TextUtils.isEmpty(text)) {
            speak(text, isDefaultParam);
            return;
        }
        str1 = empty ? "我真的不知道了" : text;
        try {
            str1 = sendMessage(str);
        } catch (Exception e) {
            defaultToke();
        }

    }


    private String sendMessage(final String request) {
        if (TextUtils.isEmpty(request)) {
            defaultToke();
            return str1;
        }
        data = EHSharedPreferences.ReadAccessToken(ACCESSTOKEN_KEY);
        if (data == null) {
            return str1;
        }
        UnitModel model = new UnitModel();
        model.getUnit(data.getAccessToken(), SCENE_Id, request, sessionId, new HttpListener<Unit>() {

            @Override
            public void onSuccess(Unit data) {
                if (data != null)
                    sessionId = data.getSession_id();
                for (Unit.ActionListBean action : data.getAction_list()) {
                    if (!TextUtils.isEmpty(action.getSay())) {
                        sb = new StringBuilder();
                        sb.append(action.getSay());
                    }
                }
                str1 = sb.toString().replace("<USER-NAME>", "");
                defaultToke();
            }

            @Override
            public void onError() {
                defaultToke();
            }

            @Override
            public void onComplete() {

            }
        });
        return str1;
    }


    class SayTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                post(recogerResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    /**
     * 参数设置
     *
     * @param
     * @return
     */
    public void setParam() {
        MLVoiceRecognize.setParam(getApplicationContext(), mIat);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isStoped = true;
        if (mIat != null) {
            mIat.stopListening();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLottieView != null) {
            mLottieView.cancelAnimation();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        MLVoiceRecognize.destroy();
        MLVoiceSynthetize.stop();
    }


    private void speak(String content, boolean isDefaultParam) {
        MLVoiceRecognize.stop();
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), content, isDefaultParam);
    }

    private boolean showWaveView = true;

    private void addWaveView() {
        if (!showWaveView) {
            return;
        }
        FrameLayout contentView = findViewById(android.R.id.content);
        voiceLineView = new VoiceLineView(this);
        voiceLineView.setBackgroundColor(Color.parseColor("#00000000"));
        voiceLineView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.common_popshow_anim));

        int width = provideWaveViewWidth();
        int height = provideWaveViewHeight();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        contentView.addView(voiceLineView, params);
        contentView.bringToFront();
        voiceLineView.setVisibility(View.GONE);

    }

    private int provideWaveViewHeight() {
        return ScreenUtils.dip2px(450);
    }

    private int provideWaveViewWidth() {
        return ScreenUtils.dip2px(120);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    speak(str1, isDefaultParam);
                    startAnim();
                    break;
                case 1:
                    clickRecognize();
                    break;
            }
            super.handleMessage(msg);
        }

    };

    private void defaultToke() {
        if (str1 != null) {

            if ("我真的不知道了".equals(str1)) {
                animationType = -1;
                startAnim();
                int randNum = rand.nextInt(30) + 1;

                switch (randNum) {

                    case 1:
                        speak(getString(R.string.speak_1), isDefaultParam);
                        break;
                    case 2:
                        speak(getString(R.string.speak_2), isDefaultParam);
                        break;
                    case 3:
                        speak(getString(R.string.speak_3), isDefaultParam);

                        break;
                    case 4:
                        speak(getString(R.string.speak_4), isDefaultParam);

                        break;
                    case 5:
                        speak(getString(R.string.speak_5), isDefaultParam);

                        break;
                    case 6:
                        speak(getString(R.string.speak_6), isDefaultParam);

                        break;
                    case 7:
                        speak(getString(R.string.speak_7), isDefaultParam);

                        break;
                    case 8:
                        speak(getString(R.string.speak_8), isDefaultParam);

                        break;
                    case 9:
                        speak(getString(R.string.speak_9), isDefaultParam);

                        break;
                    case 10:
                        speak(getString(R.string.speak_10), isDefaultParam);
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 30:
                        //变声学舌
                        MLVoiceSynthetize.setRandomParam();
                        isDefaultParam = false;
                        speak(recogerResult, isDefaultParam);
                        isDefaultParam = true;
                        break;
                    default:
                        break;
                }

            } else {
                if (isStoped) {
                    return;
                }
                mHandler.sendEmptyMessage(0);

            }
        }
    }


}
