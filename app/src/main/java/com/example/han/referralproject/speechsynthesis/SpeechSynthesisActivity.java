package com.example.han.referralproject.speechsynthesis;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.DetectActivity;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.Receive1;
import com.example.han.referralproject.bean.RobotContent;
import com.example.han.referralproject.bean.User;
import com.example.han.referralproject.speech.setting.IatSettings;
import com.example.han.referralproject.speech.util.JsonParser;
import com.example.han.referralproject.temperature.TemperatureActivity;
import com.example.han.referralproject.video.MainVideoActivity;
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
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;
import com.medlink.danbogh.call.EMUIHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SpeechSynthesisActivity extends BaseActivity implements View.OnClickListener {

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


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    startSynthesis(str1);

                    break;

                case 1:
                    findViewById(R.id.iat_recognizes).performClick();
                    break;
            }
            super.handleMessage(msg);
        }


    };


    int maxVolume = 0;
    int volume = 0;
    AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_synthesis);

        initLayout();

        //初始化音频管理器
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.Rela);
        mRelativeLayout.setBackgroundResource(R.drawable.conversation_bg);

        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);

        // 初始化听`写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(this, mInitListener);

        mSharedPreferences = getSharedPreferences(IatSettings.PREFER_NAME, Activity.MODE_PRIVATE);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        //mResultText = ((EditText) findViewById(R.id.iat_text));

        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        mToast1 = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mEngineType = SpeechConstant.TYPE_CLOUD;
        findViewById(R.id.iat_recognizes).performClick();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                findViewById(R.id.iat_recognizes).performClick();
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
                    // 显示听写对话框
                    mIatDialog.setListener(mRecognizerDialogListener);
                    mIatDialog.show();
                    showTip(getString(R.string.text_begin));
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
                        if (resultBuffer.toString().matches(".*测.*血压.*")) {
                            if (sign == true) {
                                sign = false;
                                mIatDialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), XueyaActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        } else if (PinYinUtils.converterToSpell(resultBuffer.toString()).contains("xueyang")) {
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
            Log.e("==============", text);


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

    private void post(String str) throws Exception {
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


        Log.e("返回结果", str1);

        if (str1 != null) {
            mHandler.sendEmptyMessage(0);
        } else {
            Log.e("==========", "已执行");
            // findViewById(R.id.iat_recognizes).performClick();
        }
        pw.close();
        br.close();


    }


    public void startSynthesis(String str) {

        //   mTts = SpeechSynthesizer.createSynthesizer(IatDemo.this, mTtsInitListener);

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


    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
            if (isLast) {
                if (resultBuffer.toString().matches(".*测.*血压.*") || PinYinUtils.converterToSpell(resultBuffer.toString()).matches(".*liang.*xueya.*")) {
                    if (sign == true) {
                        sign = false;
                        mIatDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), DetectActivity.class);
                        intent.putExtra("type", "xueya");
                        startActivity(intent);
                        finish();
                    }

                } else if (PinYinUtils.converterToSpell(resultBuffer.toString()).matches(".*ce.*xueyang.*") || PinYinUtils.converterToSpell(resultBuffer.toString()).matches(".*liang.*xueyang.*")) {
                    if (sign == true) {
                        sign = false;
                        mIatDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), XueyangActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else if (resultBuffer.toString().matches(".*测.*血糖.*") || PinYinUtils.converterToSpell(resultBuffer.toString()).matches(".*liang.*xuetang.*")) {
                    if (sign == true) {
                        sign = false;
                        mIatDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), XuetangActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else if (resultBuffer.toString().matches(".*测.*体温.*") || resultBuffer.toString().matches(".*测.*温度.*") || PinYinUtils.converterToSpell(resultBuffer.toString()).matches(".*liang.*tiwen.*") || PinYinUtils.converterToSpell(resultBuffer.toString()).matches(".*liang.*wendu.*")) {
                    if (sign == true) {
                        sign = false;
                        mIatDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), DetectActivity.class);
                        intent.putExtra("type", "wendu");
                        startActivity(intent);
                        finish();
                    }

                } else if (resultBuffer.toString().matches(".*视频.*") || PinYinUtils.converterToSpell(resultBuffer.toString()).matches(".*jiankang.*jiangtan.*")) {
                    if (sign == true) {
                        sign = false;
                        mIatDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), MainVideoActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else if (resultBuffer.toString().matches(".*歌.*") || resultBuffer.toString().matches(".*音乐.*")) {
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


                } else if (resultBuffer.toString().matches(".*打.*电话.*") || PinYinUtils.converterToSpell(resultBuffer.toString()).matches(".*zixun.*yisheng.*")) {

                    EMUIHelper.callVideo(MyApplication.getInstance(), MyApplication.getInstance().emDoctorId);

                    finish();
                   /* if (sign == true) {
                        sign = false;
                        mIatDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), MainVideoActivity.class);
                        startActivity(intent);
                        finish();
                    }*/

                } else if (PinYinUtils.converterToSpell(resultBuffer.toString()).matches(".*da.*shengyin.*") || PinYinUtils.converterToSpell(resultBuffer.toString()).matches(".*da.*yinliang.*")) {
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


                } else if (PinYinUtils.converterToSpell(resultBuffer.toString()).matches(".*xiao.*shengyin.*") || PinYinUtils.converterToSpell(resultBuffer.toString()).matches(".*xiao.*yinliang.*")) {

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
                    runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    //speak(R.string.speak_no_result);
                                    findViewById(R.id.iat_recognizes).performClick();
                                }
                            }
                    );
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
      /*  if (null != mIat) {
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }

        if (null != mTts) {
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }*/
    }
}
