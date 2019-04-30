package com.example.han.referralproject.speechsynthesis;

import android.animation.Animator;
import android.app.Activity;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.DiseaseDetailsActivity;
import com.example.han.referralproject.activity.MarketActivity;
import com.example.han.referralproject.activity.MessageActivity;
import com.example.han.referralproject.bean.DiseaseUser;
import com.example.han.referralproject.bean.Receive1;
import com.example.han.referralproject.bean.RobotContent;
import com.example.han.referralproject.bean.UserInfo;
import com.example.han.referralproject.bean.VersionInfoBean;
import com.example.han.referralproject.children.ChildEduHomeActivity;
import com.example.han.referralproject.children.entertainment.ChildEduJokesActivity;
import com.example.han.referralproject.children.entertainment.ChildEduSheetDetailsActivity;
import com.example.han.referralproject.children.study.ChildEduPoemListActivity;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.hypertensionmanagement.activity.SlowDiseaseManagementActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.new_music.HttpCallback;
import com.example.han.referralproject.new_music.HttpClient;
import com.example.han.referralproject.new_music.Music;
import com.example.han.referralproject.new_music.MusicPlayActivity;
import com.example.han.referralproject.new_music.PlaySearchedMusic;
import com.example.han.referralproject.new_music.SearchMusic;
import com.example.han.referralproject.radio.RadioActivity;
import com.example.han.referralproject.recharge.PayActivity;
import com.example.han.referralproject.recyclerview.CheckContractActivity;
import com.example.han.referralproject.recyclerview.DoctorAskGuideActivity;
import com.example.han.referralproject.recyclerview.DoctorappoActivity2;
import com.example.han.referralproject.recyclerview.OnlineDoctorListActivity;
import com.example.han.referralproject.service_package.ServicePackageActivity;
import com.example.han.referralproject.settting.SharedPreferencesUtils;
import com.example.han.referralproject.settting.activity.SettingActivity;
import com.example.han.referralproject.settting.bean.KeyWordDefinevBean;
import com.example.han.referralproject.shopping.OrderListActivity;
import com.example.han.referralproject.speech.setting.IatSettings;
import com.example.han.referralproject.speech.util.JsonParser;
import com.example.han.referralproject.tcm.activity.OlderHealthManagementSerciveActivity;
import com.example.han.referralproject.tool.other.StringUtil;
import com.example.han.referralproject.tool.wrapview.VoiceLineView;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.UpdateAppManager;
import com.example.han.referralproject.video.VideoListActivity;
import com.example.lenovo.rto.accesstoken.AccessToken;
import com.example.lenovo.rto.http.HttpListener;
import com.example.lenovo.rto.sharedpreference.EHSharedPreferences;
import com.example.lenovo.rto.unit.Unit;
import com.example.lenovo.rto.unit.UnitModel;
import com.example.module_control_volume.VolumeControlFloatwindow;
import com.gcml.call.CallAuthHelper;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.PinYinUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.module_health_record.HealthRecordActivity;
import com.gcml.old.auth.personal.PersonDetailActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.alarm.AlarmHelper;
import com.medlink.danbogh.alarm.AlarmList2Activity;
import com.ml.edu.OldRouter;
import com.ml.edu.old.TheOldHomeActivity;
import com.ml.edu.old.music.TheOldMusicActivity;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
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

import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.example.lenovo.rto.Constans.ACCESSTOKEN_KEY;
import static com.example.lenovo.rto.Constans.SCENE_Id;
@Route(path = "/app/speech/synthesis/activity")
public class SpeechSynthesisActivity extends BaseActivity implements View.OnClickListener {

    private static String TAG = SpeechSynthesisActivity.class.getSimpleName();
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
//    private RecognizerDialog mIatDialog;
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


    @Override
    protected int provideWaveViewWidth() {
        return 1200;
    }

    @Override
    protected int provideWaveViewHeight() {
        return 360;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowVoiceView(true);
        setContentView(R.layout.activity_speech_synthesis);
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
//        mIatDialog = new MlRecognizerDialog(this, mInitListener);

        mIatPreferences = getSharedPreferences(IatSettings.PREFER_NAME, Activity.MODE_PRIVATE);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mEngineType = SpeechConstant.TYPE_CLOUD;


//        speak("来和我聊天吧", isDefaultParam);
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
        setDisableGlobalListen(true);
        super.onResume();
        speak("来和我聊天吧", isDefaultParam);
        setEnableListeningLoop(false);
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


                new PlaySearchedMusic(SpeechSynthesisActivity.this, mSearchMusicList.get(0)) {
                    @Override
                    public void onPrepare() {

                    }

                    @Override
                    public void onExecuteSuccess(Music music) {
                        if (isStoped) {
                            return;
                        }
                        //跳转到音乐播放界面去
                        startActivityForResult(new Intent(SpeechSynthesisActivity.this, MusicPlayActivity.class)
                                .putExtra("music", music), TO_MUSICPLAY);
                    }

                    @Override
                    public void onExecuteFail(Exception e) {
                        ToastUtils.showShort(R.string.unable_to_play);
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

        switch (view.getId()) {
            // 开始听写
            // 如何判断一次听写结束：OnResult isLast=true 或者 onError
            case R.id.iat_recognizes:

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
                break;
            case R.id.tv_normal:
                isDefaultParam = true;
                break;
            case R.id.tv_whine:
                MLVoiceSynthetize.setRandomParam();
                isDefaultParam = false;
                break;
            case R.id.iv_yuyin:
                onEndOfSpeech();
                notice.setVisibility(View.GONE);
                mImageView.performClick();
                break;
            default:
                break;
        }

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


    @Override
    protected void onActivitySpeakFinish() {
        super.onActivitySpeakFinish();
        if (!TextUtils.isEmpty(mAudioPath)) {

            int tag = TO_STORY;
            String service = results.get("service");
            if ("storyTelling".equals(service)) {
                tag = TO_PING_SHU;
            }
            onPlayAudio(mAudioPath, tag);
            mAudioPath = null;
            return;
        }
//        if (faceAnim != null && faceAnim.isRunning()) {
//            faceAnim.stop();
//        }
        if (yuyinFlag) {
            findViewById(R.id.iat_recognizes).performClick();
        }
    }

    private void onPlayAudio(String audioPath, int tag) {
        Music music = new Music(audioPath);
        startActivityForResult(new Intent(SpeechSynthesisActivity.this, MusicPlayActivity.class)
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
                speak("我没听清您能再说一遍吗", isDefaultParam);
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            //  showTip("结束说话");
            if (yuyinFlag) {
                showWaveView(false);
            } else {
                SpeechSynthesisActivity.this.onEndOfSpeech();
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
                updateVolume(voiceLineView);
            } else {
                lineWave.waveH = volume / 6 + 2;
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {

        }
    };

    private void dealData(RecognizerResult results, boolean isLast) {
        printResult(results);
        if (isLast) {
            String result = resultBuffer.toString();
            ToastUtils.showShort(result);
            String inSpell = PinYinUtils.converterToSpell(result);

            Pattern patternWhenAlarm = Pattern.compile(REGEX_SET_ALARM_WHEN);
            Matcher matcherWhenAlarm = patternWhenAlarm.matcher(inSpell);
            /*if (inSpell.matches(".*((xin|xing)dian).*")) {
                startActivityForResult(new Intent(SpeechSynthesisActivity.this, DetectActivity.class).putExtra("type", "xindian"));
                return;
            }
            if (inSpell.matches(".*(sanheyi|(xie|xue)(niao|liao)(suan|shuan)|dangu(chun|cun)).*")) {
                startActivityForResult(new Intent(SpeechSynthesisActivity.this, DetectActivity.class).putExtra("type", "sanheyi"));
                return;
            }*/
            if (matcherWhenAlarm.find()) {
                String am = matcherWhenAlarm.group(1);
                String hourOfDay = matcherWhenAlarm.group(2);
                String minute = matcherWhenAlarm.group(3);
                AlarmHelper.setupAlarm(SpeechSynthesisActivity.this.getApplicationContext(),
                        am.equals("shangwu") ? Integer.valueOf(hourOfDay) : Integer.valueOf(hourOfDay) + 12,
                        Integer.valueOf(minute));
                String tip = String.format(Locale.CHINA,
                        "小易将在%s:%s提醒您吃药", hourOfDay, minute);
                speak(tip, isDefaultParam);
                return;
            }

            if (inSpell.matches(".*woyaogengxin|genxinxitong|xitonggengxin.*") || inSpell.matches(".*gengxin.*")) {
                showLoadingDialog("检查更新中");
                NetworkApi.getVersionInfo(new NetworkManager.SuccessCallback<VersionInfoBean>() {
                    @Override
                    public void onSuccess(VersionInfoBean response) {
                        hideLoadingDialog();
                        try {
                            if (response != null && response.vid > getPackageManager().getPackageInfo(SpeechSynthesisActivity.this.getPackageName(), 0).versionCode) {
                                new UpdateAppManager(SpeechSynthesisActivity.this).showNoticeDialog(response.url);
                            } else {
                                speak("当前已经是最新版本了");
                                Toast.makeText(mContext, "当前已经是最新版本了", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        hideLoadingDialog();
                        speak("当前已经是最新版本了");
                        Toast.makeText(mContext, "当前已经是最新版本了", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }

            if (inSpell.matches(".*((jiankang|meiri|zuo|zhuo|chakan|cakan|jintiande)renwu).*") || inSpell.matches(".*(jintianzhuoshenme|jintianzuoshenme).*")) {
                CC.obtainBuilder("app.component.task").addParam("startType", "MLSpeech").build().callAsync();
                return;
            }

            if (inSpell.matches(".*(jiankangjiance|zuogejiancha|jianchashenti|zuotijian).*")) {
                jiance();
                return;
            }


            if (inSpell.matches(".*(cexueya|liangxueya|xueyajiance).*")) {
                jiance();
                return;
            }


            if (inSpell.matches(".*(cexueyang|liangxueyang).*")) {
                jiance();
                return;
            }


            if (inSpell.matches(".*(cetiwen|liangtiwen|cewendu|liangwendu).*")) {
                jiance();
                return;
            }

            if (inSpell.matches(".*(cexuetang|liangxuetang|xuetangyi).*")) {
                jiance();
                return;
            }

            if (inSpell.matches(".*(ceizhong|liangtizhong).*")) {
                jiance();
                return;
            }

            if (inSpell.matches(".*(cexindian|liangxindian|xiandianceliang|xindianceshi|xindianjiance|xiandiantu).*")) {
                jiance();
                return;
            }

          /*  if (inSpell.matches(".*(hujiaojiaren|jiaren.*dianhua*)")) {
                NimCallActivity.launchNoCheck(this, UserSpHelper.getEqId());
//                NetworkApi.PersonInfo(MyApplication.getInstance().eqid, new NetworkManager.SuccessCallback<UserInfo>() {
//                    @Override
//                    public void onSuccess(UserInfo response) {
//                        if (isFinishing() || isDestroyed()) {
//                            return;
//                        }
//                        NetworkApi.postTelMessage(response.tel, MyApplication.getInstance().userName, new NetworkManager.SuccessCallback<Object>() {
//                            @Override
//                            public void onSuccess(Object response) {
//
//                            }
//                        }, new NetworkManager.FailedCallback() {
//                            @Override
//                            public void onFailed(String message) {
//
//                            }
//                        });
//                    }
//                }, new NetworkManager.FailedCallback() {
//                    @Override
//                    public void onFailed(String message) {
//
//                    }
//                });
                return;
            }*/

            if (inSpell.matches(".*(yulezhongxin).*")) {
//                CC.obtainBuilder("app.component.recreation").build().callAsync();
                Routerfit.register(AppRouter.class).skipRecreationEntranceActivity();
                return;
            }

            if (inSpell.matches(".*(laorenyule).*")) {
                startActivity(TheOldHomeActivity.class);
                return;
            }

            if (inSpell.matches(".*(youjiao|youjiaowenyu|ertongyoujiao|jiaoxiaohai|ertongyule).*")) {
                startActivity(ChildEduHomeActivity.class);
                return;
            }

            if (inSpell.matches(".*(gushi|tangshisongci|songci|tangshi).*") || result.matches(".*古诗.*")) {
                startActivity(ChildEduPoemListActivity.class);
                return;
            }

            if (inSpell.matches(".*(jianggexiaohua|xiaohua|youqudehua).*")) {
                startActivity(ChildEduJokesActivity.class);
                return;
            }


            if (result.matches(".*听故事|故事.*")) {
                startActivity(ChildEduPoemListActivity.class);
                return;
            }


            if (inSpell.matches(".*(xiaogongju).*")) {
//                CC.obtainBuilder("app.component.recreation.tools").build().call();
                Routerfit.register(AppRouter.class).skipToolsActivity();
                return;
            }

            if (inSpell.matches(".*(zhougongjiemeng|jiemeng|jiegemeng|zuolemeng).*")) {
//                CC.obtainBuilder("app.component.recreation.tool").setActionName("oneiromancy").build().call();
                Routerfit.register(AppRouter.class).skipJieMengActivity();
                return;
            }

            if (inSpell.matches(".*(lishijintian|lishishangdejintian|lishishangjintiandeshijian).*")) {
//                CC.obtainBuilder("app.component.recreation.tool").setActionName("historyToday").build().call();
                Routerfit.register(AppRouter.class).skipHistoryTodayActivity();
                return;
            }

            if (inSpell.matches(".*(riqichaxun|jidianle|chaxunriqi|jintianxingqiji|jidianle|jintianshenmerizi).*")) {
//                CC.obtainBuilder("app.component.recreation.tool").setActionName("dateInquiry").build().call();
                Routerfit.register(AppRouter.class).skipDateInquireActivity();
                return;
            }
            if (inSpell.matches(".*(caipu|shaocai|zuocai|chishenme|chishengme|tuijiancai).*")) {
//                CC.obtainBuilder("app.component.recreation.tool").setActionName("cookBook").build().call();
                Routerfit.register(AppRouter.class).skipCookBookActivity();
                return;
            }

            if (inSpell.matches(".*(baike).*")) {
//                CC.obtainBuilder("app.component.recreation.tool").setActionName("baike").build().call();
                Routerfit.register(AppRouter.class).skipBaikeActivity();
                return;
            }


            if (inSpell.matches(".*(jisuanqi|zuosuanshu).*")) {
//                CC.obtainBuilder("app.component.recreation.tool").setActionName("calculate").build().call();
                Routerfit.register(AppRouter.class).skipCalculationActivity();
                return;
            }
            if (inSpell.matches(".*(zhengzhuangzicha).*")) {
                Routerfit.register(AppRouter.class).skipSymptomCheckActivity();
                return;
            }

            if (inSpell.matches(".*(zhongyitizhi).*")) {
                startActivity(OlderHealthManagementSerciveActivity.class);
                return;
            }


            if (inSpell.matches(".*(yinyue|yinle).*")) {
                OldRouter.routeToOldMusicActivity(this);
                return;
            }
            if (inSpell.matches(".*(jiankangguanli|gaoxueyaguanli|gaoxueyafangan|" +
                    "gaoxueyazhiliao|gaoxueyacaipu|jiankangfangan|jiankangbaogao).*")) {
                startActivity(SlowDiseaseManagementActivity.class);
                return;
            }
            if (inSpell.matches(".*(fengxian|fengxianpinggu|fengxianpanduan" +
                    "|huanbingfenxiang|debingfengxian|jiankangyuce|jiankangyuche|pinggu).*")) {
                Routerfit.register(AppRouter.class).skipHealthInquiryActivity();
                return;
            }

            if (inSpell.matches(".*(qiehuan|qiehuanzhanghao|chongxindenglu|zhongxindenglu|tianjiazhanghao).*")) {
                startActivity(PersonDetailActivity.class);
                return;
            }

            if (inSpell.matches(".*(shezhi|jiqirenshezhi|wifilianjie|tiaojieyinliang|diaojieyinliang|yiliangdaxiao).*")) {
                startActivity(SettingActivity.class);
                return;
            }


            if (inSpell.matches(".*(danganxiazai|lishishuju|lishijilu|jiancejieguo|celiangshuju|jiankangshuju|jiankangdangan|jianchajieguo).*")) {
//                startActivity(new Intent(SpeechSynthesisActivity.this, HealthRecordActivity.class));
                vertifyFaceThenHealthRecordActivity();
                return;
            }

//            if (inSpell.matches(".*jian(ce|che|ca|cha).*")
//                    ||inSpell.matches(".*(ce|che)(shi|si).*")) {
//                Intent intent = new Intent(SpeechSynthesisActivity.this, FaceRecognitionActivity.class);
//                intent.putExtra("from", "Test");
//                startActivityForResult(intent);
//                return;
//            }

            if (inSpell.matches(".*xiaoxi.*")) {
                Routerfit.register(AppRouter.class).skipMessageActivity();
                return;
            }

            if (inSpell.matches(".*(guangbo|diantai|shouyinji|zhisheng|diantai).*")) {
                Intent intent = new Intent(SpeechSynthesisActivity.this, RadioActivity.class);
                startActivity(intent);
                return;
            }


            if (inSpell.matches(".*(erge|ertonggequ).*")) {
                startActivity(ChildEduSheetDetailsActivity.class, "sheetCategory", "儿童歌曲");
                return;
            }


            if (inSpell.matches(".*(yaolanqu).*")) {
                startActivity(ChildEduSheetDetailsActivity.class, "sheetCategory", "摇篮曲");
                return;
            }

            if (inSpell.matches(".*(taijiaoyinyue|taijiao|taijiaoyinle).*")) {
                startActivity(ChildEduSheetDetailsActivity.class, "sheetCategory", "胎教音乐");
                return;
            }


            if (inSpell.matches(".*(tingyinyue|tingge|fangge|yinyueguan|yinleguan|tingyinle).*")) {
                startActivity(TheOldMusicActivity.class);
                return;
            }

            if (inSpell.matches(".*gerenzhongxin.*")
                    || inSpell.matches(".*guorenzhongxin.*")) {
                gotoPersonCenter();
                return;
            }

            if (inSpell.matches(".*(geren|xiugai)xinxi.*")
                    || inSpell.matches(".*huantouxiang.*")) {
                CC.obtainBuilder("com.gcml.auth.profileInfo").build().callAsync();
                return;
            }

            if (inSpell.matches(".*zhujiemian|zujiemian|jujiemian|zhuye.*")
                    || inSpell.matches(".*zhujiemian|shuijiao|xiuxi|guanbi.*")) {
                gotoHomePage();
                return;
            }
            if (inSpell.matches("yishengjianyi|chakanxiaoxi")) {
                Routerfit.register(AppRouter.class).skipMessageActivity();
                return;
            }

           /* if (inSpell.matches(REGEX_SET_ALARM)) {
                Intent intent = AlarmList2Activity.newLaunchIntent(SpeechSynthesisActivity.this);
                startActivity(intent);
                return;
            }*/
            if (inSpell.matches(REGEX_SEE_DOCTOR)) {
                DiseaseUser diseaseUser = new DiseaseUser(
                        LocalShared.getInstance(this).getUserName(),
                        LocalShared.getInstance(this).getSex().equals("男") ? 1 : 2,
                        Integer.parseInt(LocalShared.getInstance(this).getUserAge()) * 12,
                        LocalShared.getInstance(this).getUserPhoto()
                );
                String currentUser = new Gson().toJson(diseaseUser);
                Intent intent = new Intent(this, com.witspring.unitbody.ChooseMemberActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
                return;
            }

            if (inSpell.matches(".*(jiankangzhishi|jiankangketang|jiangkangxuanchuan" +
                    "|tingke|xuexi|yiqishiyong|shebeishiyong|shiyongjiaocheng|shiyongfangfa" +
                    "|shebeijianjie|yiqijieshao|jiankangjiaoyu|jiankangxuanjiao).*")) {
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
           /* if (inSpell.matches(".*(donghuapian|dongman).*")) {
                VideoListActivity.launch(SpeechSynthesisActivity.this, 3);
                return;
            }*/
            if (inSpell.matches(".*(qianyueguwen|zhuanshuguwen|sirenguwen).*")) {
                gotoQianyueYiSheng();
                return;
            }
            if (inSpell.matches(".*(zaixiangu(wen|weng)).*")) {
                startActivity(OnlineDoctorListActivity.class);
                return;
            }
            if (inSpell.matches(".*(zi|zhi)xun.*")
                    || inSpell.matches(".*(gu(wen|weng)|shipin)((zi|zhi)xun).*")) {
                startActivity(DoctorAskGuideActivity.class);
                return;
            }
            /*if (inSpell.matches(".*(gaoxueya).*")) {
                startActivityForResult(new Intent(SpeechSynthesisActivity.this, DiseaseDetailsActivity.class)
                        .putExtra("type", "高血压"));
            }*/
            if (inSpell.matches(".*(bian(sheng|shen|seng)).*")) {
                voiceWhine.performClick();
            }
            if (inSpell.matches(".*(suijibiansheng|suijibianshen|shuijibiansheng|shuijibianseng).*")) {
                voiceWhine.performClick();
            }

            if (inSpell.matches(".*(huifuzhengchang|heifuzhengchang|huifuzengcang|huifuzhengcang|huifu|zengchang).*")) {
                voiceNormal.performClick();
            }

            if (inSpell.matches(".*(xiaoyiyuanshen|xiaoyiyuanshen|xiaoyi|yuansen|xiaoeyuansheng|xiaoeyuanshen).*")) {
                voiceNormal.performClick();
            }

            if (inSpell.matches(".*(guanxin(bin|bing)).*")) {
                startActivity(new Intent(SpeechSynthesisActivity.this, DiseaseDetailsActivity.class)
                        .putExtra("type", "冠心病"));
                return;
            }
            if (inSpell.matches(".*(zhiqiguanxiaochuan).*")) {
                startActivity(new Intent(SpeechSynthesisActivity.this, DiseaseDetailsActivity.class)
                        .putExtra("type", "支气管哮喘"));
                return;
            }
            if (inSpell.matches(".*(gan(yin|ying)hua).*")) {
                startActivity(new Intent(SpeechSynthesisActivity.this, DiseaseDetailsActivity.class)
                        .putExtra("type", "肝硬化"));
                return;
            }
//            if (inSpell.matches(".*(tang(niao|liao)(bin|bing)).*")) {
//                startActivity(new Intent(SpeechSynthesisActivity.this, DiseaseDetailsActivity.class)
//                        .putExtra("type", "糖尿病"));
//                return;
//            }
            if (inSpell.matches(".*(tongfeng).*")) {
                startActivity(new Intent(SpeechSynthesisActivity.this, DiseaseDetailsActivity.class)
                        .putExtra("type", "痛风"));
                return;
            }
            if (inSpell.matches(".*(changweiyan).*")) {
                startActivity(new Intent(SpeechSynthesisActivity.this, DiseaseDetailsActivity.class)
                        .putExtra("type", "肠胃炎"));
                return;
            }
            if (inSpell.matches(".*(ji(xin|xing)(sang|shang)huxidaoganran).*")) {
                startActivity(new Intent(SpeechSynthesisActivity.this, DiseaseDetailsActivity.class)
                        .putExtra("type", "急性上呼吸道感染"));
                return;
            }
            if (inSpell.matches(".*(xinbaoyan).*")) {
                startActivity(new Intent(SpeechSynthesisActivity.this, DiseaseDetailsActivity.class)
                        .putExtra("type", "心包炎"));
                return;
            }
            if (inSpell.matches(".*((pin|ping)(xie|xue)).*")) {
                startActivity(new Intent(SpeechSynthesisActivity.this, DiseaseDetailsActivity.class)
                        .putExtra("type", "贫血"));
                return;
            }
            if (inSpell.matches(".*(feiyan).*")) {
                startActivity(new Intent(SpeechSynthesisActivity.this, DiseaseDetailsActivity.class)
                        .putExtra("type", "肺炎"));
                return;
            }
            if (inSpell.matches(".*(di(xie|xue)tang).*")) {
                startActivity(new Intent(SpeechSynthesisActivity.this, DiseaseDetailsActivity.class)
                        .putExtra("type", "低血糖"));
                return;
            }
            if (inSpell.matches(".*((nao|lao)chu(xie|xue)).*")) {
                startActivity(new Intent(SpeechSynthesisActivity.this, DiseaseDetailsActivity.class)
                        .putExtra("type", "脑出血"));
                return;
            }
            if (inSpell.matches(".*(fei(suan|shuan)sai).*")) {
                startActivity(new Intent(SpeechSynthesisActivity.this, DiseaseDetailsActivity.class)
                        .putExtra("type", "肺栓塞"));
                return;
            }
            if (inSpell.matches(".*(dianxian).*")) {
                startActivity(new Intent(SpeechSynthesisActivity.this, DiseaseDetailsActivity.class)
                        .putExtra("type", "癫痫"));
                return;
            }

            if (inSpell.matches(".*(tuichu|tuicu|baibai|zaijian|zhaijian)")) {
                exit();
                return;
            }
            boolean dealKeyWord = keyWordDeal(inSpell);
            if (dealKeyWord) {
                return;
            }
//            KeyWordBean keyword = (KeyWordBean) SharedPreferencesUtils.getParam(this, "keyword", new KeyWordBean());
//            if (keyword.yueya.equals(resultBuffer.toString())) {
//                mIatDialog.dismiss();
//                Intent intent = new Intent(getApplicationContext(), DetectActivity.class);
//                intent.putExtra("type", "xueya");
//                startActivityForResult(intent);
//                return;
//            }
            if (inSpell.matches(".*(liangxueya|cexueya|xueyajiance).*")) {
//                Bundle bundle = new Bundle();
//                bundle.putString("from", "Test");
//                bundle.putString("fromType", "xueya");
//                CCFaceRecognitionActions.jump2FaceRecognitionActivity(this, bundle);
                jiance();

            } else if (inSpell.matches(".*ce.*xueyang.*")
                    || inSpell.matches(".*xueyang.*")
                    || inSpell.matches(".*liang.*xueyang.*")
                    || inSpell.matches(".*ce.*baohedu.*")) {
                jiance();
            } else if (result.matches(".*测.*血糖.*")
                    || inSpell.matches(".*liang.*xuetang.*")
                    || inSpell.matches(".*xuetangyi.*")
            ) {
                jiance();
            } else if (result.matches(".*测.*体温.*") || result.matches(".*测.*温度.*") || inSpell.matches(".*liang.*tiwen.*") || inSpell.matches(".*liang.*wendu.*")) {
                jiance();
            } else if (inSpell.matches(".*ce.*xindian.*")
                    || inSpell.matches(".*xindian(celiang|ceshi|jiance).*")) {
                jiance();
            } else if (inSpell.matches(".*(ce|liang).*(niaosuan|xuezhi|danguchun).*")) {
                jiance();
            } else if (inSpell.matches(".*ce.*tizhong.*")) {

                jiance();
            } else if (result.matches(".*视频.*") || inSpell.matches(".*jiankang.*jiangtan.*")) {

                Intent intent = new Intent(getApplicationContext(), VideoListActivity.class);
                startActivity(intent);


            } else if (inSpell.matches(".*yisheng.*zixun.*") || inSpell.matches("wenyisheng|yishengzixun|jiatingyisheng|yuyue")) {

                if ("".equals(sharedPreferences.getString("name", ""))) {
                    ToastUtils.showShort("请先查看是否与绑定健康顾问绑定成功");
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), DoctorappoActivity2.class);
                    startActivity(intent);
                }


            } else if (inSpell.matches(".*dashengyin.*")
                    || inSpell.matches(".*dayinliang.*")
                    || inSpell.matches(".*dashengdian.*")
                    || inSpell.matches(".*dadiansheng.*")
                    || inSpell.matches(".*yinliang.*da.*")
                    || inSpell.matches(".*shengyin.*da.*")
                    || inSpell.matches(".*tigao.*shengyin.*")
                    || inSpell.matches(".*shengyin.*tigao.*")
                    || inSpell.matches(".*yinliang.*shenggao.*")
                    || inSpell.matches(".*shenggao.*yinliang.*")
                    || inSpell.matches(".*shengyin.*xiangyidian.*")
                    || inSpell.matches(".*shengyin.*zhongyidian.*")

            ) {
                addVoice();
            } else if (inSpell.matches(".*xiaoshengyin.*")
                    || inSpell.matches(".*xiaoyinliang.*")
                    || inSpell.matches(".*xiaoshengdian.*")
                    || inSpell.matches(".*xiaodiansheng.*")
                    || inSpell.matches(".*shengyin.*xiao.*")
                    || inSpell.matches(".*yinliang.*xiao.*")
                    || inSpell.matches(".*yinliang.*jiangdi.*")
                    || inSpell.matches(".*jiangdi.*yinliang.*")
                    || inSpell.matches(".*jiangdi.*shengyin.*")
                    || inSpell.matches(".*shengyin.*jiangdi.*")
                    || inSpell.matches(".*shengyin.*qingyidian.*")

            ) {

                deleteVoice();


            } else if (inSpell.matches(".*bu.*liao.*") || result.contains("退出")
                    || result.contains("返回") || result.contains("再见")
                    || result.contains("闭嘴") || inSpell.matches(".*baibai.*")) {

                finish();
            } else if (inSpell.matches(".*((bin|bing)(zheng|zhen|zen|zeng)|(zi|zhi)(ca|cha)).*")) {
                DiseaseUser diseaseUser = new DiseaseUser(
                        LocalShared.getInstance(this).getUserName(),
                        LocalShared.getInstance(this).getSex().equals("男") ? 1 : 2,
                        Integer.parseInt(LocalShared.getInstance(this).getUserAge()) * 12,
                        LocalShared.getInstance(this).getUserPhoto()
                );
                String currentUser = new Gson().toJson(diseaseUser);
                Intent intent = new Intent(this, com.witspring.unitbody.ChooseMemberActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            } else if (inSpell.matches(".*chongqian|qianbugou|meiqian.*") || inSpell.matches(".*chongzhi.*") || result.contains("钱不够") || result.contains("没钱")) {
                Intent intent = new Intent(getApplicationContext(), PayActivity.class);
                startActivity(intent);
            } else if (inSpell.matches(".*maidongxi")
                    || inSpell.matches(".*mai.*shizhi")
                    || inSpell.matches(".*mai.*xueyaji")
                    || inSpell.matches(".*mai.*xuetangyi")
                    || inSpell.matches(".*mai.*erwenqiang")
                    || inSpell.matches(".*mai.*xueyangyi")
                    || inSpell.matches(".*mai.*xindianyi")
                    || inSpell.matches(".*shizhiyongwan.*")
                    || inSpell.matches(".*shizhi.*meiyou")
                    || inSpell.matches(".*shangcheng")

                    || inSpell.matches(".*maibaojianpin")
                    || inSpell.matches(".*xiaoyituijian")
                    || inSpell.matches(".*tuijian(shangpin|shanpin)")
            ) {
                Intent intent = new Intent(getApplicationContext(), MarketActivity.class);
                startActivity(intent);


            } else if (inSpell.matches(".*dingdan|wodedingdan|chakandingdan|dingdanxiangqing|gouwuqingdan")) {

                Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                startActivity(intent);
            } else if (inSpell.matches(".*((bin|bing)(zheng|zhen|zen|zeng)|(zi|zhi)(ca|cha)|(lan|nan)(shou|sou)).*")) {//症状自查
                DiseaseUser diseaseUser = new DiseaseUser(LocalShared.getInstance(this).getUserName(), LocalShared.getInstance(this).getSex().equals("男") ? 1 : 2, Integer.parseInt(LocalShared.getInstance(this).getUserAge()) * 12, LocalShared.getInstance(this).getUserPhoto()
                );
                String currentUser = new Gson().toJson(diseaseUser);
                Intent intent = new Intent(this, com.witspring.unitbody.ChooseMemberActivity.class);
                intent.putExtra("currentUser", currentUser);
                startActivity(intent);
            } else if (inSpell.matches(".*(dangan).*")) {
                CC.obtainBuilder("com.gcml.auth.profileInfo").build().callAsync();
            } else {
                new SpeechTask().execute();
            }
        }
    }

    private void exit() {
        MobclickAgent.onProfileSignOff();
        CallAuthHelper.getInstance().logout();
        UserSpHelper.setToken("");
        UserSpHelper.setEqId("");
        UserSpHelper.setUserId("");
        CC.obtainBuilder("com.gcml.auth").build().callAsync();
        finish();
    }

    private void jiance() {
        startActivity(ServicePackageActivity.class);

       /* CC.obtainBuilder("com.gcml.auth.face2.signin")
                .addParam("skip", true)
                .addParam("currentUser", false)
                .build()
                .callAsyncCallbackOnMainThread(new IComponentCallback() {
                    @Override
                    public void onResult(CC cc, CCResult result) {
                        boolean skip = "skip".equals(result.getErrorMessage());
                        if (result.isSuccess() || skip) {
                            if (skip) {
                                CCHealthMeasureActions.jump2MeasureChooseDeviceActivity(true);
                                return;
                            }
                            CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                        } else {
                            ToastUtils.showShort(result.getErrorMessage());
                        }
                    }
                });*/
    }

    private void gotoHomePage() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void gotoQianyueYiSheng() {
        NetworkApi.PersonInfo(UserSpHelper.getUserId(), new NetworkManager.SuccessCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                if ("1".equals(response.getState())) {
                    //已绑定
                    startActivity(new Intent(SpeechSynthesisActivity.this,
                            DoctorappoActivity2.class));
                } else if ("0".equals(response.getState())
                        && (TextUtils.isEmpty(response.getDoctername()))) {
                    //未绑定
                    Intent intent = new Intent(SpeechSynthesisActivity.this,
                            OnlineDoctorListActivity.class);
                    intent.putExtra("flag", "contract");
                    startActivity(intent);
                } else {
                    // 待审核
                    Intent intent = new Intent(SpeechSynthesisActivity.this,
                            CheckContractActivity.class);
                    startActivity(intent);
                }
            }

        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.showShort(message);
            }
        });
    }

    private void gotoPersonCenter() {
        Intent intent = new Intent(SpeechSynthesisActivity.this, PersonDetailActivity.class);
        startActivity(intent);
    }

    private void deleteVoice() {
        volume -= 3;
        if (volume > 3) {
            speak(getString(R.string.reduce_volume), isDefaultParam);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
            mHandler.sendEmptyMessageDelayed(1, 2000);
            VolumeControlFloatwindow.setVolumSB(volume);

        } else {
            speak(getString(R.string.min_volume), isDefaultParam);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 3, AudioManager.FLAG_PLAY_SOUND);
            mHandler.sendEmptyMessageDelayed(1, 3000);

        }
    }

    private void addVoice() {
        volume += 3;
        if (volume < maxVolume) {
            speak(getString(R.string.add_volume), isDefaultParam);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
            mHandler.sendEmptyMessageDelayed(1, 2000);
            VolumeControlFloatwindow.setVolumSB(volume);
        } else {
            speak(getString(R.string.max_volume), isDefaultParam);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_PLAY_SOUND);
            mHandler.sendEmptyMessageDelayed(1, 3000);
        }
    }

    private boolean keyWordDeal(String yuyin) {
        if (TextUtils.isEmpty(yuyin)) {
            return false;
        }
        //血压
//        jiance.addAll(getDefineData("xueyang"));
//        jiance.addAll(getDefineData("tiwen"));
//        jiance.addAll(getDefineData("xuetang"));
//        jiance.addAll(getDefineData("xindian"));
//        jiance.addAll(getDefineData("tizhong"));
//        jiance.addAll(getDefineData("sanheyi"));
        List<KeyWordDefinevBean> jiance = getDefineData("xueya");
        String pinyin;
        for (int i = 0; i < jiance.size(); i++) {
            pinyin = jiance.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                jiance();
                return true;
            }
        }

        //血氧
        List<KeyWordDefinevBean> xueyang = getDefineData("xueyang");
        for (int i = 0; i < xueyang.size(); i++) {
            pinyin = xueyang.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                jiance();
                return true;
            }
        }
        //体温

        List<KeyWordDefinevBean> tiwen = getDefineData("tiwen");
        for (int i = 0; i < tiwen.size(); i++) {
            pinyin = tiwen.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                jiance();
                return true;
            }
        }

        //血糖

        List<KeyWordDefinevBean> xuetang = getDefineData("xuetang");
        for (int i = 0; i < xuetang.size(); i++) {
            pinyin = xuetang.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                jiance();
                return true;
            }
        }

        //心电
        List<KeyWordDefinevBean> xindian = getDefineData("xindian");
        for (int i = 0; i < xindian.size(); i++) {
            pinyin = xindian.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                jiance();
                return true;
            }
        }

        //体重
        List<KeyWordDefinevBean> tizhong = getDefineData("tizhong");
        for (int i = 0; i < tizhong.size(); i++) {
            pinyin = tizhong.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                jiance();
                return true;
            }
        }


        //三合一
        List<KeyWordDefinevBean> sanheyi = getDefineData("sanheyi");
        for (int i = 0; i < sanheyi.size(); i++) {
            pinyin = sanheyi.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                jiance();
                return true;
            }
        }


        //调大声音
        List<KeyWordDefinevBean> addVoice = getDefineData("tiaodashengyin");
        for (int i = 0; i < addVoice.size(); i++) {
            pinyin = addVoice.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(addVoice.get(i).pinyin)) {
                addVoice();
                return true;
            }
        }

        //调小声音
        List<KeyWordDefinevBean> deleteVoice = getDefineData("tiaoxiaoshengyin");
        for (int i = 0; i < deleteVoice.size(); i++) {
            pinyin = deleteVoice.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                addVoice();
                return true;
            }
        }

        //回到主界面
        List<KeyWordDefinevBean> home = getDefineData("huidaozhujiemian");
        for (int i = 0; i < home.size(); i++) {
            pinyin = home.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                finish();
                return true;
            }
        }

        //个人中心
        List<KeyWordDefinevBean> personCenter = getDefineData("gerenzhongxin");
        for (int i = 0; i < personCenter.size(); i++) {
            pinyin = personCenter.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                gotoPersonCenter();
                return true;
            }
        }

        //症状自查
        List<KeyWordDefinevBean> check = getDefineData("zhengzhuangzicha");
        for (int i = 0; i < check.size(); i++) {
            pinyin = check.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                gotoZhengzhuangCheck();
                return true;
            }
        }

        //测量历史
        List<KeyWordDefinevBean> celianglishi = getDefineData("celianglishi");
        for (int i = 0; i < celianglishi.size(); i++) {
            pinyin = celianglishi.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                startActivity(new Intent(SpeechSynthesisActivity.this, HealthRecordActivity.class));
                return true;
            }
        }

        //健康顾问建议
        List<KeyWordDefinevBean> doctorJianyi = getDefineData("yishengjianyi");
        for (int i = 0; i < doctorJianyi.size(); i++) {
            pinyin = doctorJianyi.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(pinyin)) {
                Routerfit.register(AppRouter.class).skipMessageActivity();
                return true;
            }
        }

        //吃药提醒
        List<KeyWordDefinevBean> chiyaoTixing = getDefineData("chiyaotixing");
        for (int i = 0; i < chiyaoTixing.size(); i++) {
            pinyin = chiyaoTixing.get(i).pinyin;
            if (TextUtils.isEmpty(pinyin)) {
                continue;
            }
            if (yuyin.contains(chiyaoTixing.get(i).pinyin)) {
                Intent intent = AlarmList2Activity.newLaunchIntent(this);
                startActivity(intent);
                return true;
            }
        }

        //账户充值
        List<KeyWordDefinevBean> zhanghuchongzhi = getDefineData("zhanghuchongzhi");
        for (int i = 0; i < zhanghuchongzhi.size(); i++) {
            if (yuyin.contains(zhanghuchongzhi.get(i).pinyin)) {
                Intent intent = new Intent(getApplicationContext(), PayActivity.class);
                startActivity(intent);
                return true;
            }
        }

        //我的订单
        List<KeyWordDefinevBean> dingdan = getDefineData("wodedingdan");
        for (int i = 0; i < dingdan.size(); i++) {
            if (yuyin.contains(dingdan.get(i).pinyin)) {
                Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                startActivity(intent);
                return true;
            }
        }

        //健康课堂
        List<KeyWordDefinevBean> jiankangketang = getDefineData("jiankangketang");
        for (int i = 0; i < jiankangketang.size(); i++) {
            if (yuyin.contains(jiankangketang.get(i).pinyin)) {
                startActivity(new Intent(this, VideoListActivity.class));
                return true;
            }
        }


        //娱乐
        List<KeyWordDefinevBean> yule = getDefineData("yule");
        for (int i = 0; i < yule.size(); i++) {
            if (yuyin.contains(yule.get(i).pinyin)) {
                //老人娱乐
//                OldRouter.routeToOldHomeActivity(this);
                startActivity(TheOldHomeActivity.class);
                return true;
            }
        }


        //收音机
        List<KeyWordDefinevBean> shouyinji = getDefineData("shouyinji");
        for (int i = 0; i < shouyinji.size(); i++) {
            if (yuyin.contains(shouyinji.get(i).pinyin)) {
                startActivity(new Intent(this, RadioActivity.class));
                return true;
            }
        }

        //音乐
        List<KeyWordDefinevBean> yinyue = getDefineData("yinyue");
        for (int i = 0; i < yinyue.size(); i++) {
            if (yuyin.contains(yinyue.get(i).pinyin)) {
                startActivity(new Intent(getApplicationContext(), TheOldMusicActivity.class));
                return true;
            }
        }


        //健康顾问咨询
        List<KeyWordDefinevBean> zixunyisheng = getDefineData("yishengzixun");
        for (int i = 0; i < zixunyisheng.size(); i++) {
            if (yuyin.contains(zixunyisheng.get(i).pinyin)) {
                startActivity(new Intent(getApplicationContext(), DoctorAskGuideActivity.class));
                return true;
            }
        }

        //在线健康顾问
        List<KeyWordDefinevBean> zaixianyisheng = getDefineData("zaixianyisheng");
        for (int i = 0; i < zaixianyisheng.size(); i++) {
            if (yuyin.contains(zaixianyisheng.get(i).pinyin)) {
                startActivity(new Intent(this, OnlineDoctorListActivity.class));
                return true;
            }
        }

        //绑定健康顾问
        List<KeyWordDefinevBean> qianyueyisheng = getDefineData("qianyueyisheng");
        for (int i = 0; i < qianyueyisheng.size(); i++) {
            if (yuyin.contains(qianyueyisheng.get(i).pinyin)) {
                gotoQianyueYiSheng();
                return true;
            }
        }


        //健康商城
        List<KeyWordDefinevBean> jiankang = getDefineData("jiankangshangcheng");
        for (int i = 0; i < jiankang.size(); i++) {
            if (yuyin.contains(jiankang.get(i).pinyin)) {
                startActivity(new Intent(this, MarketActivity.class
                ));
                return true;
            }
        }

        return false;

    }

    private void gotoZhengzhuangCheck() {
        DiseaseUser diseaseUser = new DiseaseUser(
                LocalShared.getInstance(this).getUserName(),
                LocalShared.getInstance(this).getSex().equals("男") ? 1 : 2,
                Integer.parseInt(LocalShared.getInstance(this).getUserAge()) * 12,
                LocalShared.getInstance(this).getUserPhoto()
        );
        String currentUser = new Gson().toJson(diseaseUser);
        Intent intent = new Intent(this, com.witspring.unitbody.ChooseMemberActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }


    private List<KeyWordDefinevBean> getDefineData(String keyWord) {
        String xueya = (String) SharedPreferencesUtils.getParam(this, keyWord, "");
        if (gson == null) {
            gson = new Gson();
        }
        List<KeyWordDefinevBean> list = gson.fromJson(xueya, new TypeToken<List<KeyWordDefinevBean>>() {
        }.getType());
        if (list != null) {
            return list;
        }

        return new ArrayList<KeyWordDefinevBean>();
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
        Timber.i("QaApi %s", results);
        String audiopath = results.get("audiopath");
        String text = results.get("text");
        boolean empty = TextUtils.isEmpty(text);
        if (!TextUtils.isEmpty(audiopath)) {
            mAudioPath = audiopath;
            if (!empty) {
                speak(text, isDefaultParam);
            } else {
                onActivitySpeakFinish();
            }
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

        if (!empty) {
            speak(text);
            return;
        }
        str1 = empty ? "我真的不知道了" : text;

//        try {
//            dealToke(str);
//        } catch (Exception e) {
//
//        }

        try {
            str1 = sendMessage(str);
        } catch (Exception e) {
            defaultToke();
        }

//

    }

    private void dealToke(String str) throws IOException {
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

            while ((lineContent = br.readLine()) != null) {
                content = lineContent;
            }


            Receive1 string = gson.fromJson(content, Receive1.class);


            str1 = string.getReceive().getOutput();

        }
    }

    private void defaultToke() {
        if (str1 != null) {

            if (getString(R.string.speak_null).equals(str1)) {
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
                if (data != null) {
                    sessionId = data.getSession_id();
                }
                List<Unit.ActionListBean> list = data.getAction_list();
                if (list != null && list.size() != 0) {
                    if (list.size() >= 10) {
                        str1 = list.get(new Random().nextInt(10)).getSay().replace("<USER-NAME>", "");
                    } else {
                        str1 = list.get(0).getSay().replace("<USER-NAME>", "");
                    }
                }
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

   /* private static String parseXffunQAResponse(String text) {
    private void searchAndPlayMusic(String music) {

    }

    private static String parseXffunQAResponse(String text) {
        try {
            Logg.i("mylog", text);
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
            Logg.i("mylog", e.getMessage());
            e.printStackTrace();
            return "我真的不知道了";
        }
    }*/


   /* public void startSynthesis(String str) {

        //  mTts = SpeechSynthesizer.createSynthesizer(IatDemo.this, mTtsInitListener);

        // 设置参数
        setParams();
        mTts.startSpeaking(str, mTtsListener);


    }*/


    /**
     * 初始化监听。
     */
   /* private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Logg.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };*/


    /**
     * 合成回调监听。
     */
   /* private SynthesizerListener mTtsListener = new SynthesizerListener() {

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
    };*/


    /*private void setParams() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, mIatPreferences.getString("speed_preference", "50"));
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, mIatPreferences.getString("pitch_preference", "50"));
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, mIatPreferences.getString("volume_preference", "50"));
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            */
    /**
     * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
     * 开发者如需自定义参数，请参考在线合成参数设置
     *//*
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, mIatPreferences.getString("stream_preference", "3"));
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }*/


    //private MediaPlayer mediaPlayer;//MediaPlayer对象
    private File file;//要播放的文件


    public static final String REGEX_SET_ALARM = ".*((ding|she|shezhi|)naozhong|tixing|chiyao|fuyao|chiyaotixing|dingshi).*";
    public static final String REGEX_SET_ALARM_WHEN = ".*tixing.*(shangwu|xiawu).*(\\d{1,2}):(\\d{1,2}).*yao.*";
    public static final String REGEX_SEE_DOCTOR = ".*(bushufu|touteng|fa(sao|shao)|duziteng|nanshou).*";


    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            dealData(results, isLast);
        }

        /**
         * 识别回调错误.
         */
        @Override
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));

            findViewById(R.id.iat_recognizes).performClick();

        }

    };

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TO_MUSICPLAY:
                speak("想听更多歌曲，请告诉我！", isDefaultParam);
                break;
            case TO_STORY:
                speak("我讲的故事好听吗？", isDefaultParam);
                break;
            case TO_PING_SHU:
                speak("想听更多评书，请告诉我！", isDefaultParam);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startActivity(Class<?> cls) {
        startActivity(cls, null, null);
    }

    private void startActivity(Class<?> cls, String key, String value) {
        Intent intent = new Intent(this, cls);
        if (!(this instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(key, value);
        startActivity(intent);

//        if (listener != null) {
//            listener.onEnd();
//        }

    }

    private void vertifyFaceThenHealthRecordActivity() {
        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .getUserEntity()
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity userEntity) {
                        if (TextUtils.isEmpty(userEntity.sex) || TextUtils.isEmpty(userEntity.birthday)) {
                            ToastUtils.showShort("请先去个人中心完善性别和年龄信息");
                            MLVoiceSynthetize.startSynthesize(UM.getApp(),
                                    "请先去个人中心完善性别和年龄信息");
                        } else {
                            CC.obtainBuilder("com.gcml.auth.face2.signin")
                                    .addParam("skip", true)
                                    .addParam("verify", true)
                                    .addParam("currentUser", false)
                                    .addParam("hidden", true)
                                    .build()
                                    .callAsyncCallbackOnMainThread(new IComponentCallback() {
                                        @Override
                                        public void onResult(CC cc, CCResult result) {
                                            boolean skip = "skip".equals(result.getErrorMessage());
                                            if (result.isSuccess() || skip) {
//                                                startActivity(new Intent(getActivity(), HealthRecordActivity.class));
                                                startActivity(HealthRecordActivity.class);
                                            } else {
                                                ToastUtils.showShort(result.getErrorMessage());
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
