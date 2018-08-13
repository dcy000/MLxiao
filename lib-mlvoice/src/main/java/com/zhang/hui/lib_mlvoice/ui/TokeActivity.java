package com.zhang.hui.lib_mlvoice.ui;

import android.animation.Animator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.han.referralproject.new_music.HttpCallback;
import com.example.han.referralproject.new_music.HttpClient;
import com.example.han.referralproject.new_music.Music;
import com.example.han.referralproject.new_music.MusicPlayActivity;
import com.example.han.referralproject.new_music.PlaySearchedMusic;
import com.example.han.referralproject.new_music.SearchMusic;
import com.example.lenovo.rto.accesstoken.AccessToken;
import com.example.lenovo.rto.http.HttpListener;
import com.example.lenovo.rto.sharedpreference.EHSharedPreferences;
import com.example.lenovo.rto.unit.Unit;
import com.example.lenovo.rto.unit.UnitModel;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.zhang.hui.lib_mlvoice.R;
import com.zhang.hui.lib_mlvoice.constant.ConstantData;
import com.zhang.hui.lib_mlvoice.constant.TtsSettings;
import com.zhang.hui.lib_mlvoice.other.QaApi;
import com.zhang.hui.lib_mlvoice.other.StringUtil;
import com.zhang.hui.lib_mlvoice.recognition.JsonParser;
import com.zhang.hui.lib_mlvoice.synthetize.MLVoiceSynthetize;
import com.zhang.hui.lib_mlvoice.utils.PinYinUtils;
import com.zhang.hui.lib_mlvoice.utils.SharedPreferencesUtils;
import com.zhang.hui.lib_mlvoice.utils.ToastTool;
import com.zhang.hui.lib_mlvoice.wrap.VoiceLineView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.lenovo.rto.Constans.ACCESSTOKEN_KEY;
import static com.example.lenovo.rto.Constans.SCENE_Id;

public class TokeActivity extends AppCompatActivity implements View.OnClickListener {


    private static String TAG = TokeActivity.class.getSimpleName();
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private Toast mToast;
    private SharedPreferences mIatPreferences;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private StringBuffer resultBuffer;
    private RelativeLayout mRelativeLayout;
    //    private AnimationDrawable faceAnim;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //startSynthesis(str1);
                    speak(str1, isDefaultParam);
                    startAnim();

                    break;

                case 1:

                    findViewById(R.id.iat_recognizes).performClick();
                    break;
                case 2:
                    // 显示听写对话框
                    break;
            }
            super.handleMessage(msg);
        }


    };


    int maxVolume = 0;
    int volume = 0;
    AudioManager mAudioManager;
    public ImageView ivBack;
    Random rand;


    SharedPreferences sharedPreferences;

    ImageView mImageView;
    private LottieAnimationView mLottieView;
    private static final int TO_MUSICPLAY = 1;
    private static final int TO_STORY = 2;
    private static final int TO_PING_SHU = 3;
    private TextView voiceNormal;
    private TextView voiceWhine;
    private boolean isDefaultParam = true;
    private HashMap<String, String> results;
    private ImageView yuyin;
    private VoiceLineView lineWave;
    private Boolean yuyinFlag;
    private boolean isStart;
    private TextView notice;
    private Gson gson;

    private AccessToken data;
    private String sessionId = "";
    private StringBuilder sb;
    private ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toke);
        rand = new Random();
        sharedPreferences = getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE);
        mImageView = findViewById(R.id.iat_recognizes);


        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initLayout();

        //初始化音频管理器
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        mRelativeLayout = findViewById(R.id.Rela);
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
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);

        // 初始化听`写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源

        mIatPreferences = getSharedPreferences(TtsSettings.PREFER_NAME, Activity.MODE_PRIVATE);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mEngineType = SpeechConstant.TYPE_CLOUD;


//        speak("主人,来和我聊天吧", isDefaultParam);
        //默认是时时聊天
        yuyinFlag = (Boolean) SharedPreferencesUtils.getParam(this, "yuyin", true);
        if (yuyinFlag) {
            mHandler.sendEmptyMessageDelayed(1, 3000);
            yuyin.setVisibility(View.GONE);
            notice.setVisibility(View.GONE);
        } else {
            mImageView.setVisibility(View.GONE);
        }
        findViewById(R.id.tv_setup_language).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChoiceLanguages();
            }
        });
    }


    private void onChoiceLanguages() {
        String[] languages = languages();
        int index = mIatPreferences.getInt("language_index", 0);
        new AlertDialog.Builder(this)
                .setTitle("设置语言")
                .setSingleChoiceItems(
                        languages,
                        index,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String[] languageValues = languageValues();
                                if (which >= languageValues.length || which < 0) {
                                    which = 0;
                                }
                                mIatPreferences.edit()
                                        .putString("iat_language_preference", languageValues[which])
                                        .putInt("language_index", which)
                                        .commit();
                                dialog.dismiss();
                            }
                        }
                )
                .create()
                .show();
    }


    private String[] languages;

    private String[] languages() {
        if (languages != null) {
            return languages;
        }

        languages = getResources().getStringArray(R.array.languages);
        return languages;
    }

    private String[] languageValues;

    private String[] languageValues() {
        if (languageValues != null) {
            return languageValues;
        }

        languageValues = getResources().getStringArray(R.array.language_values_iat);
        return languageValues;
    }

    private boolean isStoped = false;

    @Override
    protected void onStart() {
        super.onStart();

        isStoped = false;
    }

    @Override
    protected void onResume() {
//        setDisableGlobalListen(true);
        super.onResume();
        speak("主人,来和我聊天吧", isDefaultParam);
//        setEnableListeningLoop(false);
        mLottieView.resumeAnimation();
    }

    @Override
    protected void onPause() {
        mLottieView.pauseAnimation();
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

    private List<SearchMusic.Song> mSearchMusicList = new ArrayList<>();


    private void searchMusic(String keyword) {

        HttpClient.searchMusic(keyword, new HttpCallback<SearchMusic>() {

            @Override
            public void onSuccess(SearchMusic response) {
                if (isStoped) {
                    return;
                }
                if (response == null || response.getSong() == null) {
                    speak("抱歉，没找到这首歌", isDefaultParam);
                    mHandler.sendEmptyMessageDelayed(1, 3000);
                    return;
                }
                mSearchMusicList.clear();
                mSearchMusicList.addAll(response.getSong());


                new PlaySearchedMusic(TokeActivity.this, mSearchMusicList.get(0)) {
                    @Override
                    public void onPrepare() {

                    }

                    @Override
                    public void onExecuteSuccess(Music music) {
                        if (isStoped) {
                            return;
                        }
                        //跳转到音乐播放界面去
                        startActivityForResult(new Intent(TokeActivity.this, MusicPlayActivity.class)
                                .putExtra("music", music), TO_MUSICPLAY);
                    }

                    @Override
                    public void onExecuteFail(Exception e) {
                        ToastTool.showShort("暂时无法播放");
                    }
                }.execute();

            }

            @Override
            public void onFail(Exception e) {
            }
        });
    }


    /**
     * 初始化Layout。
     */
    private void initLayout() {
        findViewById(R.id.iat_recognizes).setOnClickListener(this);
        findViewById(R.id.iat_stop).setOnClickListener(this);
        findViewById(R.id.iat_cancel).setOnClickListener(this);
        voiceNormal = findViewById(R.id.tv_normal);
        voiceWhine = findViewById(R.id.tv_whine);
        yuyin = findViewById(R.id.iv_yuyin);
        notice = findViewById(R.id.tv_notice);
        lineWave = findViewById(R.id.vl_wave);
        voiceNormal.setOnClickListener(this);
        voiceWhine.setOnClickListener(this);
        yuyin.setOnClickListener(this);
        lineWave.setOnClickListener(this);
    }

    int ret = 0; // 函数调用返回值

    @Override
    public void onClick(View view) {

        if (null == mIat) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            this.showTip("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return;
        }

        int i = view.getId();
        if (i == R.id.iat_recognizes) {
            mIatResults.clear();
            // 设置参数
            setParam();
            boolean isShowDialog = false;
            if (isShowDialog) {
                stopSpeaking();
                mHandler.sendEmptyMessageDelayed(2, 500);

            } else {
                // 不显示听写对话框
                stopSpeaking();
                ret = mIat.startListening(mRecognizerListener);
                if (ret != ErrorCode.SUCCESS) {
                    showTip("听写失败,错误码：" + ret);
                }
            }

        } else if (i == R.id.tv_normal) {
            isDefaultParam = true;

        } else if (i == R.id.tv_whine) {
            MLVoiceSynthetize.setRandomParam();
            isDefaultParam = false;

        } else if (i == R.id.iv_yuyin) {
            onEndOfSpeech();
            notice.setVisibility(View.GONE);
            mImageView.performClick();

        } else {
        }

    }

    private void stopSpeaking() {
        MLVoiceSynthetize.stop();
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

//
//    @Override
//    protected void onActivitySpeakFinish() {
//        super.onActivitySpeakFinish();
//        if (!TextUtils.isEmpty(mAudioPath)) {
//
//            int tag = TO_STORY;
//            String service = results.get("service");
//            if ("storyTelling".equals(service)) {
//                tag = TO_PING_SHU;
//            }
//            onPlayAudio(mAudioPath, tag);
//            mAudioPath = null;
//            return;
//        }
////        if (faceAnim != null && faceAnim.isRunning()) {
////            faceAnim.stop();
////        }
//        if (yuyinFlag) {
//            findViewById(R.id.iat_recognizes).performClick();
//        }
//    }

    private void onPlayAudio(String audioPath, int tag) {
        Music music = new Music(audioPath);
        startActivityForResult(new Intent(TokeActivity.this, MusicPlayActivity.class)
                .putExtra("music", music), tag);
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
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            //   showTip("开始说话");
            if (yuyinFlag) {
                showWaveView(true);
            } else {
                //直方图波形
                showWave();
            }
        }

        @Override
        public void onError(SpeechError error) {
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            if (yuyinFlag) {
                findViewById(R.id.iat_recognizes).performClick();
            } else {
                speak("主人,我没听清您能再说一遍吗", isDefaultParam);
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            //  showTip("结束说话");
            if (yuyinFlag) {
                showWaveView(false);
            } else {
                TokeActivity.this.onEndOfSpeech();
            }
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            //  Logg.d(TAG, results.getResultString());
            dealData(results, isLast);
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            //    showTip("当前正在说话，音量大小：" + volume);
            //   Logg.d(TAG, "返回音频数据：" + data.length);
            if (yuyinFlag) {
//                updateVolume(voiceLineView);

            } else {
                lineWave.waveH = volume / 6 + 2;
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

        }
    };

    private void showWaveView(boolean b) {
    }

    private void dealData(RecognizerResult results, boolean isLast) {
        printResult(results);
        if (isLast) {
            String result = resultBuffer.toString();
            ToastTool.showShort(result);
            String inSpell = PinYinUtils.converterToSpell(result);

            Pattern patternWhenAlarm = Pattern.compile(REGEX_SET_ALARM_WHEN);
            Matcher matcherWhenAlarm = patternWhenAlarm.matcher(inSpell);
            new SpeechTask().execute();
        }
    }


    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    //public boolean sign = true;

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }


    }

    String str1;
    private volatile String mAudioPath;


    private void post(String str) {
        if (str.matches("(.*)是谁")) {
            str = "百科" + str.substring(0, str.length() - 2);
        }

        results = QaApi.getQaFromXf(str);
        String audiopath = results.get("audiopath");
        String text = results.get("text");
        boolean empty = TextUtils.isEmpty(text);
        if (!TextUtils.isEmpty(audiopath)) {
            mAudioPath = audiopath;
            if (!empty) {
                speak(text, isDefaultParam);
                return;
            }

            if (isStoped) {
                return;
            }

            if ("musicX".equals(results.get("service")) && TextUtils.isEmpty(audiopath)) {
                mAudioPath = audiopath;
                int index = text.indexOf("的歌曲");
                if (index == -1) {
                    index = text.indexOf("的");
                    index += 1;
                } else {
                    index += 3;
                }
                String music = "";
                if (index != -1) {
                    music = text.substring(index);
                    searchMusic(music);
                }
                return;
            }

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
    }


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
                        speak(resultBuffer.toString(), isDefaultParam);
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

    private File file;//要播放的文件


    public static final String REGEX_SET_ALARM = ".*((ding|she|shezhi|)naozhong|tixing|chiyao|fuyao).*";
    public static final String REGEX_SET_ALARM_WHEN = ".*tixing.*(shangwu|xiawu).*(\\d{1,2}):(\\d{1,2}).*yao.*";
    public static final String REGEX_SEE_DOCTOR = ".*(bushufu|touteng|fa(sao|shao)|duziteng|nanshou).*";


    class SpeechTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                post(resultBuffer.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    /**
     * 参数设置
     *
     * @param
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        String lag = mIatPreferences.getString("iat_language_preference", "mandarin");

        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mIatPreferences.getString("iat_vadbos_preference", "5000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mIatPreferences.getString("iat_vadeos_preference", "500"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mIatPreferences.getString("iat_punc_preference", "0"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
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
        if (null != mIat) {
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
        //退出页面不再说话
        SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
        if (synthesizer != null) {
            synthesizer.stopSpeaking();
        }
    }


    private void speak(String content, boolean isDefaultParam) {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), content, isDefaultParam);
    }

    public void showLoadingDialog(String message) {
        if (mDialog == null) {
            mDialog = new ProgressDialog(getApplicationContext());
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
