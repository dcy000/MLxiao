package com.iflytek.synthetize;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.gzq.lib_core.base.App;
import com.gzq.lib_core.base.Box;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.constant.TtsSettings;
import com.iflytek.recognition.MLVoiceRecognize;

import java.util.Random;

/**
 * Created by lenovo on 2018/3/29.
 */

public final class MLVoiceSynthetize {

    private MLVoiceSynthetize() {
    }

    /**
     * 发音人
     */
    public static final String VOICE_NAME = "nannan";
    /**
     * 引擎类型
     */
    private static final String ENGIEN_TYPE = SpeechConstant.TYPE_CLOUD;

    /**
     * 发音人
     */
    public static final String[] VOICER = {"xiaoyan", "xiaoqi", "xiaoli", "xiaoyu", "xiaofeng", "xiaoxin", "laosun"};

    /**
     * 初始化语音合成对象
     */
    private static SpeechSynthesizer initSpeechSynthesizer() {
        return initSpeechSynthesizer(null);
    }

    /**
     * 初始化语音合成对象  默认设置一次参数
     */
    private static SpeechSynthesizer initSpeechSynthesizer(InitListener initListener) {
        return initSpeechSynthesizer(initListener, false);
    }

    /**
     * 初始化语音合成对象 是否采用默认参数
     */
    private static SpeechSynthesizer initSpeechSynthesizer(InitListener initListener, boolean whine) {

        SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
        if (synthesizer == null) {
            if (initListener == null) {
                initListener = new InitListener() {
                    @Override
                    public void onInit(int i) {

                    }
                };
            }
            synthesizer = SpeechSynthesizer.createSynthesizer(App.getApp(), initListener);
        }
        if (!whine) {
            setParam(synthesizer);
        } else {
            setRandomParam();
        }
        return synthesizer;
    }


    /**
     * 设置参数
     */
    private static void setParam() {
        SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
        setParam(synthesizer);

    }

    /**
     * 设置参数
     */
    private static void setParam(SpeechSynthesizer synthesizer) {
        //语音选项设置
        SharedPreferences sharedPreferences = App.getApp().getSharedPreferences(TtsSettings.PREFER_NAME, App.getApp().MODE_PRIVATE);
        initDefaultParam(synthesizer, sharedPreferences);
    }

    /**
     * 默认不变声
     *
     * @param text
     */
    public static void startSynthesize(String text) {
        startSynthesize(text, false);
    }
    public static void startSynthesize(@StringRes int id) {
        startSynthesize(Box.getString(id), false);
    }
    /**
     * 开始合成
     */
    public static void startSynthesize(String text, boolean whine) {
        startSynthesize(text,whine,null);
    }

    /**
     * 开始合成
     */
    public static void startSynthesize(String text,boolean whine, SynthesizerListener synthesizerListener) {
        startSynthesize(text,whine,null, synthesizerListener);
    }


    /**
     * 开始合成
     */
    public static void startSynthesize( String text, boolean isDefaultParam,InitListener initListener, SynthesizerListener synthesizerListener) {

        if (TextUtils.isEmpty(text)) {
            return;
        }
        MLVoiceRecognize.stopListening();
        SpeechSynthesizer synthesizer = initSpeechSynthesizer(initListener, isDefaultParam);

        if (synthesizerListener == null) {
            synthesizerListener = new MLSynthesizerListener();
        }
        synthesizer.startSpeaking(text, synthesizerListener);
    }


    /**
     * 清空参数
     */
    private static SpeechSynthesizer setNoParam(SpeechSynthesizer synthesizer) {
        synthesizer.setParameter(SpeechConstant.SPEED, null);
        return synthesizer;
    }

    /**
     * 设置引擎类型
     */

    private static SpeechSynthesizer setEngineType(SpeechSynthesizer synthesizer, String engineType) {
        synthesizer.setParameter(SpeechConstant.ENGINE_TYPE, engineType);
        return synthesizer;
    }

    private static SpeechSynthesizer setFormat(SpeechSynthesizer synthesizer, String format) {
        synthesizer.setParameter(SpeechConstant.AUDIO_FORMAT, format);
        return synthesizer;
    }


    private static SpeechSynthesizer setPath(SpeechSynthesizer synthesizer, String path) {
        synthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, path);
        return synthesizer;
    }


    //设置合成语速
    private static SpeechSynthesizer setSpeed(SpeechSynthesizer synthesizer, String speed) {
        synthesizer.setParameter(SpeechConstant.SPEED, speed);
        return synthesizer;
    }

    //设置合成语速
    private static SpeechSynthesizer setPitch(SpeechSynthesizer synthesizer, String pitch) {
        synthesizer.setParameter(SpeechConstant.PITCH, pitch);
        return synthesizer;
    }

    //设置合成音量
    private static SpeechSynthesizer setVolume(SpeechSynthesizer synthesizer, String volume) {
        synthesizer.setParameter(SpeechConstant.VOLUME, volume);
        return synthesizer;
    }

    //设置播放器音频流类型
    private static SpeechSynthesizer setStreamType(SpeechSynthesizer synthesizer, String streamType) {
        synthesizer.setParameter(SpeechConstant.STREAM_TYPE, streamType);
        return synthesizer;
    }


    //设置采样率
    private static SpeechSynthesizer setRate(SpeechSynthesizer synthesizer, String rate) {
        synthesizer.setParameter(SpeechConstant.SAMPLE_RATE, rate);
        return synthesizer;
    }

    //设置在线合成发音人
    private static SpeechSynthesizer setVoiceName(SpeechSynthesizer synthesizer, String voiceName) {
        synthesizer.setParameter(SpeechConstant.VOICE_NAME, voiceName);
        return synthesizer;
    }


    private static void initDefaultParam(SpeechSynthesizer synthesizer, SharedPreferences sharedPreferences) {

        if (synthesizer != null) {
            // 清空参数
            synthesizer.setParameter(SpeechConstant.ENGINE_TYPE, ENGIEN_TYPE);
            synthesizer.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
            // 设置在线合成发音人
//            synthesizer.setParameter(SpeechConstant.VOICE_NAME, VOICE_NAME);
            synthesizer.setParameter(SpeechConstant.VOICE_NAME, sharedPreferences.getString("iat_language_preference", "nannan"));
//            设置合成语速
            synthesizer.setParameter(SpeechConstant.SPEED, sharedPreferences.getString("speed_preference", "50"));
//            设置合成音调
            synthesizer.setParameter(SpeechConstant.PITCH, sharedPreferences.getString("pitch_preference", "50"));
//            设置合成音量
            synthesizer.setParameter(SpeechConstant.VOLUME, sharedPreferences.getString("volume_preference", "50"));
//采样率
            synthesizer.setParameter(SpeechConstant.SAMPLE_RATE, sharedPreferences.getString("rate_preference", "16000"));
            //设置播放器音频流类型
            synthesizer.setParameter(SpeechConstant.STREAM_TYPE, sharedPreferences.getString("stream_preference", "3"));
            // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
            // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
            synthesizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
            synthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
        }
    }

    /**
     * 停止说话
     */
    public static void stop() {
        SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
        if (synthesizer != null) {
            synthesizer.stopSpeaking();
        }
    }

    /**
     * 销毁回话
     */
    public static void destory() {
        SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
        if (synthesizer != null) {
            synthesizer.stopSpeaking();
            synthesizer.destroy();
        }
    }


    /**
     * 设置随机参数 变声
     */
    public static void setRandomParam() {
        //设置发音人 采样率 语速 语速 语调
        SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
        Random random = new Random();
        if (synthesizer != null) {
            setVoiceName(synthesizer, VOICER[random.nextInt(7)]);
            setPitch(synthesizer, (random.nextInt(5) + 2) * 10 + "");
            setSpeed(synthesizer, random.nextInt(30) + 40 + "");
            setRate(synthesizer, (random.nextInt(80) + 80) * 100 + "");
        }
    }

    /**
     * 设置默认参数
     */
    private static void setDefaultParam() {
        //设置发音人 采样率 语速 语速 语调
        SpeechSynthesizer synthesizer = SpeechSynthesizer.getSynthesizer();
        if (synthesizer != null) {
            setVoiceName(synthesizer, "nannan");
            setPitch(synthesizer, "50");
            setSpeed(synthesizer, "50");
            setRate(synthesizer, "16000");
        }
    }
}
