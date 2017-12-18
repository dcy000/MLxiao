package com.example.han.referralproject.speechsynthesis;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.WelcomeActivity;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.BodychartActivity;
import com.example.han.referralproject.activity.DetectActivity;
import com.example.han.referralproject.activity.MyBaseDataActivity;
import com.example.han.referralproject.activity.SymptomAnalyseResultActivity;
import com.example.han.referralproject.bean.Receive1;
import com.example.han.referralproject.bean.RobotContent;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.music.AppCache;
import com.example.han.referralproject.music.HttpCallback;
import com.example.han.referralproject.music.HttpClient;
import com.example.han.referralproject.music.Music;
import com.example.han.referralproject.music.OnPlayerEventListener;
import com.example.han.referralproject.music.PlayFragment;
import com.example.han.referralproject.music.PlaySearchedMusic;
import com.example.han.referralproject.music.PlayService;
import com.example.han.referralproject.music.SearchMusic;
import com.example.han.referralproject.music.ToastUtils;
import com.example.han.referralproject.recharge.PayActivity;
import com.example.han.referralproject.recyclerview.DoctorappoActivity;
import com.example.han.referralproject.shopping.ShopListActivity;
import com.example.han.referralproject.recyclerview.DoctorAskGuideActivity;
import com.example.han.referralproject.recyclerview.OnlineDoctorListActivity;
import com.example.han.referralproject.speech.setting.IatSettings;
import com.example.han.referralproject.speech.util.JsonParser;
import com.example.han.referralproject.temperature.TemperatureActivity;
import com.example.han.referralproject.video.VideoListActivity;
import com.example.han.referralproject.xuetang.XuetangActivity;
import com.example.han.referralproject.xueya.XueyaActivity;
import com.example.han.referralproject.xueyang.XueyangActivity;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.medlink.danbogh.alarm.AlarmHelper;
import com.medlink.danbogh.alarm.AlarmList2Activity;
import com.medlink.danbogh.healthdetection.HealthRecordActivity;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.wakeup.MlRecognizerDialog;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpeechSynthesisActivity extends BaseActivity implements View.OnClickListener, OnPlayerEventListener {

    private static String TAG = SpeechSynthesisActivity.class.getSimpleName();
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private EditText mResultText;
    private Toast mToast;
    private SharedPreferences mSharedPreferences;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    StringBuffer resultBuffer;

    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;
    // 默认发音人
    private String voicer = "nannan";

    private Toast mToast1;
    RelativeLayout mRelativeLayout;


    private PlayFragment mPlayFragment;
    private AudioManager mAudioManagers;
    private ComponentName mRemoteReceiver;
    private boolean isPlayFragmentShow = false;
    private AnimationDrawable faceAnim;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //startSynthesis(str1);
                    speak(str1);
                    startAnim();

                    break;

                case 1:

                    findViewById(R.id.iat_recognizes).performClick();
                    break;
                case 2:
                    // 显示听写对话框
                    if (mIatDialog == null) {
                        return;
                    }
                    mIatDialog.setListener(mRecognizerDialogListener);
                    mIatDialog.show();
                    showTip(getString(R.string.text_begin));
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_synthesis);



        rand = new Random();

        sharedPreferences = getSharedPreferences(ConstantData.DOCTOR_MSG, Context.MODE_PRIVATE);
        mImageView = (ImageView) findViewById(R.id.iat_recognizes);


        ivBack = (ImageView) findViewById(R.id.iv_back);
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

        mRelativeLayout = (RelativeLayout) findViewById(R.id.Rela);

        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);

        // 初始化听`写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new MlRecognizerDialog(this, mInitListener);

        mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME, Activity.MODE_PRIVATE);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        //mResultText = ((EditText) findViewById(R.id.iat_text));

        // 初始化合成对象
        // mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        mToast1 = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mEngineType = SpeechConstant.TYPE_CLOUD;


        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                findViewById(R.id.iat_recognizes).performClick();
            }
        });


        if (!checkServiceAlive()) {
            return;
        }

        getPlayService().setOnPlayEventListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        speak("主人,来和我聊天吧");

        mHandler.sendEmptyMessageDelayed(1, 3000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setEnableListeningLoop(false);

    }

    @Override
    public void onChange(Music music) {
        if (mPlayFragment != null) {
            mPlayFragment.onChange(music);
        }
    }

    @Override
    public void onPlayerStart() {
        if (mPlayFragment != null) {
            mPlayFragment.onPlayerStart();
        }
    }

    @Override
    public void onPlayerPause() {
        if (mPlayFragment != null) {
            mPlayFragment.onPlayerPause();
        }
    }


    @Override
    public void onBackPressed() {
        if (mPlayFragment != null && isPlayFragmentShow) {
            if (getPlayService().isPlaying()) {
                getPlayService().playPause();
            }
            hidePlayingFragment();

            mImageView.setClickable(true);

            //   finish();
            return;
        }

        super.onBackPressed();
    }

    private void hidePlayingFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(0, R.anim.fragment_slide_down);
        ft.hide(mPlayFragment);
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = false;
    }

    /**
     * 更新播放进度
     */
    @Override
    public void onPublish(int progress) {
        if (mPlayFragment != null) {
            mPlayFragment.onPublish(progress);
        }
    }

    @Override
    public void onBufferingUpdate(int percent) {
        if (mPlayFragment != null) {
            mPlayFragment.onBufferingUpdate(percent);
        }
    }

    @Override
    public void onTimer(long remain) {
    }

    private void startAnim() {
        mRelativeLayout.post(action);
    }

    Runnable action = new Runnable() {
        @Override
        public void run() {
            AnimationDrawable anim = new AnimationDrawable();
            anim.addFrame(mResources.getDrawable(R.drawable.icon_face_one), 50);
            anim.addFrame(mResources.getDrawable(R.drawable.icon_face_two), 50);
            anim.addFrame(mResources.getDrawable(R.drawable.icon_face_three), 50);
            anim.addFrame(mResources.getDrawable(R.drawable.icon_face_two), 50);
            anim.addFrame(mResources.getDrawable(R.drawable.icon_face_one), 50);
            anim.setOneShot(true);
            mRelativeLayout.setBackground(anim);
            anim.start();
        }
    };

    @Override
    public void onMusicListUpdate() {

    }

    private List<SearchMusic.Song> mSearchMusicList = new ArrayList<>();


    private void searchMusic(String keyword) {


        //   mSearchMusicList.clear();


        Music music = new Music(keyword);
        getPlayService().play(music);
        showPlayingFragment();

       /* HttpClient.searchMusic(keyword, new HttpCallback<SearchMusic>() {

            @Override
            public void onSuccess(SearchMusic response) {
                if (response == null || response.getSong() == null) {
                    speak("抱歉，没找到这首歌");
                    mHandler.sendEmptyMessageDelayed(1, 3000);

                    return;
                }
                mSearchMusicList.clear();
                mSearchMusicList.addAll(response.getSong());


                new PlaySearchedMusic(SpeechSynthesisActivity.this, mSearchMusicList.get(0)) {
                    @Override
                    public void onPrepare() {
                    }

                    @Override
                    public void onExecuteSuccess(Music music) {
                        getPlayService().play(music);
                        showPlayingFragment();

                    }

                    @Override
                    public void onExecuteFail(Exception e) {
                        //   mProgressDialog.cancel();
                        ToastUtils.show(R.string.unable_to_play);
                    }
                }.execute();

            }

            @Override
            public void onFail(Exception e) {
            }
        });*/
    }


    private void showPlayingFragment() {
        if (isPlayFragmentShow) {
            return;
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_up, 0);
        if (mPlayFragment == null) {
            mPlayFragment = new PlayFragment();
            ft.replace(android.R.id.content, mPlayFragment);
        } else {
            ft.show(mPlayFragment);
        }
        ft.commitAllowingStateLoss();
        isPlayFragmentShow = true;
    }


    public PlayService getPlayService() {
        PlayService playService = AppCache.getPlayService();
        if (playService == null) {
            throw new NullPointerException("play service is null");
        }
        return playService;
    }

    protected boolean checkServiceAlive() {
        if (AppCache.getPlayService() == null) {
            startActivity(new Intent(this, WelcomeActivity.class));
            AppCache.clearStack();
            return false;
        }
        return true;
    }


    /**
     * 初始化Layout。
     */
    private void initLayout() {
        findViewById(R.id.iat_recognizes).setOnClickListener(this);

        findViewById(R.id.iat_stop).setOnClickListener(this);
        findViewById(R.id.iat_cancel).setOnClickListener(this);

    }

    int ret = 0; // 函数调用返回值

    @Override
    public void onClick(View view) {

        if (null == mIat) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            this.showTip("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return;
        }

        switch (view.getId()) {
            // 开始听写
            // 如何判断一次听写结束：OnResult isLast=true 或者 onError
            case R.id.iat_recognizes:
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                //mResultText.setText(null);// 清空显示内容
                mIatResults.clear();
                // 设置参数
                setParam();
                boolean isShowDialog = mSharedPreferences.getBoolean(getString(R.string.pref_key_iat_show), true);
                if (isShowDialog) {

                    stopSpeaking();
                    mHandler.sendEmptyMessageDelayed(2, 500);

                } else {
                    // 不显示听写对话框
                    ret = mIat.startListening(mRecognizerListener);
                    if (ret != ErrorCode.SUCCESS) {
                        showTip("听写失败,错误码：" + ret);
                    } else {
                        showTip(getString(R.string.text_begin));
                    }
                }
                break;

           /* // 停止听写
            case R.id.iat_stop:
                mIat.stopListening();
                showTip("停止听写");
                break;
            // 取消听写
            case R.id.iat_cancel:
                mIat.cancel();
                showTip("取消听写");
                break;*/

            default:
                break;
        }

    }

    @Override
    protected void onActivitySpeakFinish() {
        super.onActivitySpeakFinish();
        if (!TextUtils.isEmpty(mAudioPath)) {
            T.show(mAudioPath);
            onPlayAudio(mAudioPath);
            mAudioPath = null;
            return;
        }
        if (faceAnim != null && faceAnim.isRunning()) {
            faceAnim.stop();
        }
        findViewById(R.id.iat_recognizes).performClick();
    }

    private void onPlayAudio(String audioPath) {

        Log.e("================", audioPath);
        searchMusic(audioPath);

    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            //   showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            //  showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            //  Log.d(TAG, results.getResultString());
            printResult(results);

            if (isLast) {
                new Thread(new Runnable() {
                    public void run() {
                        String inSpell = PinYinUtils.converterToSpell(resultBuffer.toString());


                        if (resultBuffer.toString().matches(".*测.*血压.*")) {
                            if (sign == true) {
                                sign = false;
                                mIatDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), XueyaActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            if (inSpell.contains("xueyang")) {
                                if (sign == true) {
                                    sign = false;
                                    mIatDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), XueyangActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } else if (resultBuffer.toString().matches(".*测.*血糖.*")) {
                                if (sign == true) {
                                    sign = false;
                                    mIatDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), XuetangActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } else if (resultBuffer.toString().matches(".*测.*温度.*")) {
                                if (sign == true) {
                                    sign = false;
                                    mIatDialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), TemperatureActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } else if (resultBuffer.toString().matches(".*歌.*")) {
                                file = new File(Environment.getExternalStorageDirectory() + File.separator + getPackageName() + "/qfdy.mp3");
                                //    mediaPlayer = MediaPlayer.create(this, R.raw.yeah);
                                if (file.exists()) {
                                    try {
                                        mediaPlayer.reset();//从新设置要播放的音乐
                                        mediaPlayer.setDataSource(file.getAbsolutePath());
                                        mediaPlayer.prepare();//预加载音频
                                        mediaPlayer.start();//播放音乐
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else if (resultBuffer.toString().matches(".*歌.*")) {
                                file = new File(Environment.getExternalStorageDirectory() + File.separator + getPackageName() + "/qfdy.mp3");
                                //    mediaPlayer = MediaPlayer.create(this, R.raw.yeah);
                                if (file.exists()) {
                                    try {
                                        mediaPlayer.reset();//从新设置要播放的音乐
                                        mediaPlayer.setDataSource(file.getAbsolutePath());
                                        mediaPlayer.prepare();//预加载音频
                                        mediaPlayer.start();//播放音乐
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                try {
                                    post(resultBuffer + "");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                }).start();

            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            //    showTip("当前正在说话，音量大小：" + volume);
            //   Log.d(TAG, "返回音频数据：" + data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

        }
    };


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

    public boolean sign = true;

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


//        mResultText.setText(resultBuffer.toString());
//        mResultText.setSelection(mResultText.length());

    }

    String str1;
    private volatile String mAudioPath;


    private void post(String str) throws Exception {
        HashMap<String, String> results = QaApi.getQaFromXf(str);
        String audiopath = results.get("audiopath");
        String text = results.get("text");
        boolean empty = TextUtils.isEmpty(text);
        if (!TextUtils.isEmpty(audiopath)) {
            mAudioPath = audiopath;
            if (!empty) {
                speak(text);
            } else {
                onActivitySpeakFinish();
            }
            return;
        }
        str1 = empty ? "我真的不知道了" : text;
        if (("我真的不知道了").equals(str1)) {
            URL url = new URL("http://api.aicyber.com/passive_chat");
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");

            conn.setDoOutput(true);
            conn.setDoInput(true);

            PrintWriter pw = new PrintWriter(conn.getOutputStream());

            Gson gson = new Gson();

            RobotContent robot = new RobotContent("gh_1822e89468ba", str, "ml05120568675", "3e809a3d90398631ad4b291aadf0f230");

            pw.print(gson.toJson(robot));

            pw.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String lineContent = null;
            String content = null;

            //    Log.e("+++++++++++++", resultBuffer.toString());
            while ((lineContent = br.readLine()) != null) {
                content = lineContent;
            }


            Receive1 string = gson.fromJson(content, Receive1.class);

            str1 = string.getReceive().getOutput();
        }
        if (str1 != null) {

            if (getString(R.string.speak_null).equals(str1)) {
                startAnim();
                int randNum = rand.nextInt(10) + 1;

                switch (randNum) {

                    case 1:
                        speak(R.string.speak_1);
                        break;
                    case 2:
                        speak(R.string.speak_2);
                        break;
                    case 3:
                        speak(R.string.speak_3);

                        break;
                    case 4:
                        speak(R.string.speak_4);

                        break;
                    case 5:
                        speak(R.string.speak_5);

                        break;
                    case 6:
                        speak(R.string.speak_6);

                        break;
                    case 7:
                        speak(R.string.speak_7);

                        break;
                    case 8:
                        speak(R.string.speak_8);

                        break;
                    case 9:
                        speak(R.string.speak_9);

                        break;
                    case 10:
                        speak(R.string.speak_10);
                        break;
                    default:
                        break;
                }

            } else {
                mHandler.sendEmptyMessage(0);

            }
        } else {
        }
//        pw.close();
//        br.close();


    }

    private static String parseXffunQAResponse(String text) {
        try {
            Log.i("mylog", text);
            JSONObject apiResponseObj = new JSONObject(text);
            text = apiResponseObj.optString("data");
            JSONObject qaResponseObj = new JSONObject(text);
            String code = qaResponseObj.optString("code");
            if (text.equals("1")) {
                return "我真的不知道了";
            }
            if (code == null || !code.equals("00000")) {
                return "我真的不知道了";
            }
            JSONObject dataObj = qaResponseObj.optJSONObject("data");
            if (dataObj == null) {
                return "我真的不知道了";
            }
            JSONObject answerObj = dataObj.optJSONObject("answer");
            if (answerObj == null) {
                return "我真的不知道了";
            }
            String answer = answerObj.optString("text");
            if (answer == null) {
                return "我真的不知道了";
            }
            return answer;
        } catch (JSONException e) {
            Log.i("mylog", e.getMessage());
            e.printStackTrace();
            return "我真的不知道了";
        }
    }


    public void startSynthesis(String str) {

        //  mTts = SpeechSynthesizer.createSynthesizer(IatDemo.this, mTtsInitListener);

        // 设置参数
        setParams();
        mTts.startSpeaking(str, mTtsListener);


    }


    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };


    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            // 合成进度
            mPercentForBuffering = percent;
            //    showTip(String.format(getString(R.string.tts_toast_format), mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
            //    showTip(String.format(getString(R.string.tts_toast_format), mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip("播放完成");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
            findViewById(R.id.iat_recognizes).performClick();
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };


    private void setParams() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, mSharedPreferences.getString("speed_preference", "50"));
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, mSharedPreferences.getString("pitch_preference", "50"));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, mSharedPreferences.getString("volume_preference", "50"));
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mSharedPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }


    private MediaPlayer mediaPlayer;//MediaPlayer对象
    private File file;//要播放的文件


    public static final String REGEX_SET_ALARM = ".*((ding|she|shezhi|)naozhong|tixing|chiyao|fuyao).*";
    public static final String REGEX_SET_ALARM_WHEN = ".*tixing.*(shangwu|xiawu).*(\\d{1,2}):(\\d{1,2}).*yao.*";
    public static final String REGEX_SEE_DOCTOR = ".*(bushufu|touteng|fa(sao|shao)|duziteng|nanshou).*";


    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
            if (isLast) {
                String inSpell = PinYinUtils.converterToSpell(resultBuffer.toString());

                Pattern patternWhenAlarm = Pattern.compile(REGEX_SET_ALARM_WHEN);
                Matcher matcherWhenAlarm = patternWhenAlarm.matcher(inSpell);
                if (matcherWhenAlarm.find()) {
                    String am = matcherWhenAlarm.group(1);
                    String hourOfDay = matcherWhenAlarm.group(2);
                    String minute = matcherWhenAlarm.group(3);
                    AlarmHelper.setupAlarm(SpeechSynthesisActivity.this.getApplicationContext(),
                            am.equals("shangwu") ? Integer.valueOf(hourOfDay) : Integer.valueOf(hourOfDay) + 12,
                            Integer.valueOf(minute));
                    String tip = String.format(Locale.CHINA,
                            "主人，小易将在%s:%s提醒您吃药", hourOfDay, minute);
                    speak(tip);
                    return;
                }

                if (inSpell.matches(REGEX_SET_ALARM)) {
                    Intent intent = AlarmList2Activity.newLaunchIntent(SpeechSynthesisActivity.this);
                    startActivity(intent);
                    return;
                }
                if (inSpell.matches(REGEX_SEE_DOCTOR)) {
                    Intent intent1 = new Intent(SpeechSynthesisActivity.this, BodychartActivity.class);
                    startActivity(intent1);
                    return;
                }
                if (inSpell.matches(REGEX_SEE_DOCTOR)) {
                    Intent intent1 = new Intent(SpeechSynthesisActivity.this, BodychartActivity.class);
                    startActivity(intent1);
                    return;
                }

                if (inSpell.matches(".*(jiankangketang|jiemu|shipin|dianshi).*")) {
                    VideoListActivity.launch(SpeechSynthesisActivity.this, 0);
                    return;
                }
                if (inSpell.matches(".*(jinju|jingju|yueju|xiju).*")) {
                    VideoListActivity.launch(SpeechSynthesisActivity.this, 1);
                    return;
                }
                if (inSpell.matches(".*(shenghuozhushou).*")) {
                    VideoListActivity.launch(SpeechSynthesisActivity.this, 2);
                    return;
                }
                if (inSpell.matches(".*(donghuapian|dongman).*")) {
                    VideoListActivity.launch(SpeechSynthesisActivity.this, 3);
                    return;
                }
                if (inSpell.matches(".*(qianyueyisheng|jiatingyisheng|yuyue).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, DoctorappoActivity.class));
                    return;
                }
                if (inSpell.matches(".*(zaixianyi(shen|sheng|seng)).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, OnlineDoctorListActivity.class));
                    return;
                }
                if (inSpell.matches(".*(yi(shen|sheng|seng)|dadianhua|(zi|zhi)xun).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, DoctorAskGuideActivity.class));
                    return;
                }
                if (inSpell.matches(".*(gaoxueya).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, SymptomAnalyseResultActivity.class)
                            .putExtra("type", "高血压"));
                }
                if (inSpell.matches(".*(guanxin(bin|bing)).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, SymptomAnalyseResultActivity.class)
                            .putExtra("type", "冠心病"));
                }
                if (inSpell.matches(".*(zhiqiguanxiaochuan).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, SymptomAnalyseResultActivity.class)
                            .putExtra("type", "支气管哮喘"));
                }
                if (inSpell.matches(".*(gan(yin|ying)hua).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, SymptomAnalyseResultActivity.class)
                            .putExtra("type", "肝硬化"));
                }
                if (inSpell.matches(".*(tang(niao|liao)(bin|bing)).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, SymptomAnalyseResultActivity.class)
                            .putExtra("type", "糖尿病"));
                }
                if (inSpell.matches(".*(tongfeng).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, SymptomAnalyseResultActivity.class)
                            .putExtra("type", "痛风"));
                }
                if (inSpell.matches(".*(changweiyan).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, SymptomAnalyseResultActivity.class)
                            .putExtra("type", "肠胃炎"));
                }
                if (inSpell.matches(".*(ji(xin|xing)(sang|shang)huxidaoganran).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, SymptomAnalyseResultActivity.class)
                            .putExtra("type", "急性上呼吸道感染"));
                }
                if (inSpell.matches(".*(xinbaoyan).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, SymptomAnalyseResultActivity.class)
                            .putExtra("type", "心包炎"));
                }
                if (inSpell.matches(".*((pin|ping)(xie|xue)).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, SymptomAnalyseResultActivity.class)
                            .putExtra("type", "贫血"));
                }
                if (inSpell.matches(".*(feiyan).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, SymptomAnalyseResultActivity.class)
                            .putExtra("type", "肺炎"));
                }
                if (inSpell.matches(".*(di(xie|xue)tang).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, SymptomAnalyseResultActivity.class)
                            .putExtra("type", "低血糖"));
                }
                if (inSpell.matches(".*((nao|lao)chu(xie|xue)).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, SymptomAnalyseResultActivity.class)
                            .putExtra("type", "脑出血"));
                }
                if (inSpell.matches(".*(fei(suan|shuan)sai).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, SymptomAnalyseResultActivity.class)
                            .putExtra("type", "肺栓塞"));
                }
                if (inSpell.matches(".*(dianxian).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, SymptomAnalyseResultActivity.class)
                            .putExtra("type", "癫痫"));
                }


                if (resultBuffer.toString().matches(".*测.*血压.*") || inSpell.matches(".*liang.*xueya.*")) {
                    if (sign == true) {
                        sign = false;
                        mIatDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), DetectActivity.class);
                        intent.putExtra("type", "xueya");
                        startActivity(intent);
                        finish();
                    }

                } else if (inSpell.matches(".*ce.*xueyang.*") || inSpell.matches(".*liang.*xueyang.*") || inSpell.matches(".*ce.*baohedu.*")) {
                    if (sign == true) {
                        sign = false;
                        mIatDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), DetectActivity.class);
                        intent.putExtra("type", "xueyang");
                        startActivity(intent);
                        finish();
                    }

                } else if (resultBuffer.toString().matches(".*测.*血糖.*") || inSpell.matches(".*liang.*xuetang.*")) {
                    if (sign == true) {
                        sign = false;
                        mIatDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), DetectActivity.class);
                        intent.putExtra("type", "xuetang");
                        startActivity(intent);
                        finish();
                    }

                } else if (resultBuffer.toString().matches(".*测.*体温.*") || resultBuffer.toString().matches(".*测.*温度.*") || inSpell.matches(".*liang.*tiwen.*") || inSpell.matches(".*liang.*wendu.*")) {
                    if (sign == true) {
                        sign = false;
                        mIatDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), DetectActivity.class);
                        intent.putExtra("type", "wendu");
                        startActivity(intent);
                        finish();
                    }

                } else if (resultBuffer.toString().matches(".*视频.*") || inSpell.matches(".*jiankang.*jiangtan.*")) {
                    if (sign == true) {
                        sign = false;
                        mIatDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), VideoListActivity.class);
                        startActivity(intent);
                        finish();
                    }

                /*}
                else if (resultBuffer.toString().matches(".*歌.*") || resultBuffer.toString().matches(".*音乐.*")) {
                    file = new File(Environment.getExternalStorageDirectory() + File.separator + getPackageName() + "/qfdy.mp3");
                    //    mediaPlayer = MediaPlayer.create(this, R.raw.yeah);
                    if (file.exists()) {
                        try {
                            mediaPlayer.reset();//从新设置要播放的音乐
                            mediaPlayer.setDataSource(file.getAbsolutePath());
                            mediaPlayer.prepare();//预加载音频
                            mediaPlayer.start();//播放音乐
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } else if (PinYinUtils.converterToSpell(resultBuffer.toString()).contains("jingju")) {
                    file = new File(Environment.getExternalStorageDirectory() + File.separator + getPackageName() + "/jingju.mp3");
                    //    mediaPlayer = MediaPlayer.create(this, R.raw.yeah);
                    if (file.exists()) {
                        try {
                            mediaPlayer.reset();//从新设置要播放的音乐
                            mediaPlayer.setDataSource(file.getAbsolutePath());
                            mediaPlayer.prepare();//预加载音频
                            mediaPlayer.start();//播放音乐
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
*/

                } else if (resultBuffer.toString().matches(".*打.*电话.*") || inSpell.matches(".*zixun.*yisheng.*")) {

                    if ("".equals(sharedPreferences.getString("name", ""))) {
                        T.show("请先查看是否与签约医生签约成功");
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), DoctorappoActivity.class);
                        startActivity(intent);
                        finish();
                    }
                   /* if (sign == true) {
                        sign = false;
                        mIatDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), MainVideoActivity.class);
                        startActivity(intent);
                        finish();
                    }*/

                } else if (inSpell.matches(".*da.*shengyin.*") || inSpell.matches(".*da.*yinliang.*")
                        || inSpell.matches(".*yinliang.*da.*") || inSpell.matches(".*shengyin.*da.*")
                        || inSpell.matches(".*tigao.*shengyin.*") || inSpell.matches(".*shengyin.*tigao.*")
                        || inSpell.matches(".*yinliang.*shenggao.*") || inSpell.matches(".*shenggao.*yinliang.*")) {
                    volume += 3;
                    if (volume < maxVolume) {
                        speak(getString(R.string.add_volume));
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
                        mHandler.sendEmptyMessageDelayed(1, 2000);
                    } else {
                        speak(getString(R.string.max_volume));
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_PLAY_SOUND);
                        mHandler.sendEmptyMessageDelayed(1, 3000);


                    }


                } else if (inSpell.matches(".*xiao.*shengyin.*") || inSpell.matches(".*xiao.*yinliang.*")
                        || inSpell.matches(".*shengyin.*xiao.*") || inSpell.matches(".*yinliang.*xiao.*")
                        || inSpell.matches(".*yinliang.*jiangdi.*") || inSpell.matches(".*jiangdi.*yinliang.*")
                        || inSpell.matches(".*jiangdi.*shengyin.*") || inSpell.matches(".*shengyin.*jiangdi.*")) {

                    volume -= 3;
                    if (volume > 3) {
                        speak(getString(R.string.reduce_volume));
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
                        mHandler.sendEmptyMessageDelayed(1, 2000);


                    } else {
                        speak(getString(R.string.min_volume));
                        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 3, AudioManager.FLAG_PLAY_SOUND);
                        mHandler.sendEmptyMessageDelayed(1, 3000);

                    }


                } /*else if (inSpell.contains("ting") || resultBuffer.toString().contains("播放") || inSpell.contains("fangyishou")) {

                    mImageView.setClickable(false);

                    try {
                        if (resultBuffer.toString().matches(".*听.*的.*")) {
                            searchMusic(StringUtils.substringAfter(resultBuffer.toString(), "的"));
                        } else if (resultBuffer.toString().contains("听")) {
                            searchMusic(StringUtils.substringAfter(resultBuffer.toString(), "听"));
                        }
                        if (resultBuffer.toString().matches(".*播放.*的.*")) {
                            searchMusic(StringUtils.substringAfter(resultBuffer.toString(), "的"));
                        } else if (resultBuffer.toString().contains("播放")) {
                            searchMusic(StringUtils.substringAfter(resultBuffer.toString(), "放"));
                        }
                        if (resultBuffer.toString().matches(".*放.*首.*的.*")) {
                            searchMusic(StringUtils.substringAfter(resultBuffer.toString(), "的"));
                        } else if (resultBuffer.toString().contains("放一首")) {
                            searchMusic(StringUtils.substringAfter(resultBuffer.toString(), "首"));
                        }


                        // searchMusic(StringUtils.substringAfter(resultBuffer.toString(), "的"));

                    } catch (Exception e) {
                        runOnUiThread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        speak(R.string.speak_no_result);
                                        //   findViewById(R.id.iat_recognizes).performClick();
                                    }
                                }
                        );
                        e.printStackTrace();
                    }


                } */ else if (inSpell.matches(".*bu.*liao.*") || resultBuffer.toString().contains("退出")
                        || resultBuffer.toString().contains("返回") || resultBuffer.toString().contains("再见")
                        || resultBuffer.toString().contains("闭嘴") || inSpell.matches(".*baibai.*")) {

                    finish();
                } else if (inSpell.matches(".*((bin|bing)(zheng|zhen|zen|zeng)|(zi|zhi)(ca|cha)).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, BodychartActivity.class));
                } else if (inSpell.matches(".*chong.*qian.*") || resultBuffer.toString().contains("钱不够") || resultBuffer.toString().contains("没钱")) {
                    Intent intent = new Intent(getApplicationContext(), PayActivity.class);
                    startActivity(intent);
                    finish();
                } else if (inSpell.matches(".*mai.*dongxi") || inSpell.matches(".*mai.*shizhi") || inSpell.matches(".*mai.*xueyaji") || inSpell.matches(".*mai.*xuetangyi") ||
                        inSpell.matches(".*mai.*erwenqiang") || inSpell.matches(".*mai.*xueyangyi") || inSpell.matches(".*mai.*xindianyi") ||
                        inSpell.matches(".*shizhi.*yongwan") || inSpell.matches(".*shizhi.*meiyou") || inSpell.matches(".*huai.*") ||
                        resultBuffer.toString().contains("找不到") || resultBuffer.toString().contains("丢") || resultBuffer.toString().contains("不能用")) {

                    Intent intent = new Intent(getApplicationContext(), ShopListActivity.class);
                    startActivity(intent);
                    finish();


                } else if (inSpell.matches(".*((bin|bing)(zheng|zhen|zen|zeng)|(zi|zhi)(ca|cha)|(lan|nan)(shou|sou)).*")) {//症状自查
                    startActivity(new Intent(SpeechSynthesisActivity.this, BodychartActivity.class));
                } else if (inSpell.matches(".*(li(si|shi)|(shu|su)ju|jilu).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, HealthRecordActivity.class));
                } else if (inSpell.matches(".*(dangan).*")) {
                    startActivity(new Intent(SpeechSynthesisActivity.this, MyBaseDataActivity.class));
                } else {
                    new SpeechTask().execute();
                }
            }

        }


        class SpeechTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    post(resultBuffer.toString());
                } catch (Exception e) {
//                    runOnUiThread(
//                            new Runnable() {
//                                @Override
//                                public void run() {
//                                    //speak(R.string.speak_no_result);
//                                    findViewById(R.id.iat_recognizes).performClick();
//                                }
//                            }
//                    );
                    e.printStackTrace();
                }
                return null;
            }
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));

            findViewById(R.id.iat_recognizes).performClick();

        }

    };

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

        String lag = mSharedPreferences.getString("iat_language_preference", "mandarin");
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
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "5000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "500"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "0"));

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    @Override
    protected void onStop() {
        super.onStop();

//        if (null != mIat) {
//            // 退出时释放连接
//            mIat.cancel();
//            mIat.destroy();
//        }
//
//        if (null != mTts) {
//            mTts.stopSpeaking();
//            // 退出时释放连接
//            mTts.destroy();
//        }
//
//        if (mediaPlayer.isPlaying()) {
//            mediaPlayer.stop();
//        }
//        mediaPlayer.release();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIatDialog = null;
        mHandler.removeMessages(1);
        mHandler.removeMessages(0);
        if (null != mIat) {
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }

        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();


        if (mRemoteReceiver != null) {
            mAudioManagers.unregisterMediaButtonEventReceiver(mRemoteReceiver);
        }
        PlayService service = AppCache.getPlayService();
        if (service != null) {
            service.setOnPlayEventListener(null);
        }
    }
}
