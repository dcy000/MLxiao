package com.iflytek.recognition;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.constant.TtsSettings;

/**
 * Created by lenovo on 2018/3/29.
 */

public class MLVoiceRecognize {
    // 引擎类型
    private static final String engineType = SpeechConstant.TYPE_CLOUD;

    private MLVoiceRecognize() {

    }

    /**
     * 初始化语音识别对象
     */
    public static SpeechRecognizer initSpeechRecognizer(Context context) {
        return initSpeechRecognizer(context.getApplicationContext(), null);
    }

    /**
     * 初始化语音识别对象
     */
    public static SpeechRecognizer initSpeechRecognizer(Context context, InitListener initListener) {
        return initSpeechRecognizer(context.getApplicationContext(), initListener, false);
    }


    /**
     * 初始化语音识别对象
     */
    public static SpeechRecognizer initSpeechRecognizer(Context context, InitListener initListener, boolean defaultParam) {
        SpeechRecognizer speechRecognizer = SpeechRecognizer.getRecognizer();
        if (speechRecognizer == null) {
            if (initListener == null) {
                initListener = new InitListener() {
                    @Override
                    public void onInit(int i) {

                    }
                };
            }
            speechRecognizer = SpeechRecognizer.createRecognizer(context.getApplicationContext(), initListener);
        }
        if (!defaultParam) {
            setParam(context.getApplicationContext(), speechRecognizer);
        }
        return speechRecognizer;
    }


    /**
     * 设置参数
     */
    public static void setParam(Context context) {
        SpeechRecognizer speechRecognizer = SpeechRecognizer.getRecognizer();
        setParam(context.getApplicationContext(), speechRecognizer);
    }


    /**
     * 设置参数
     */
    public static void setParam(Context context, SpeechRecognizer speechRecognizer) {
        SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(TtsSettings.PREFER_NAME, Context.MODE_PRIVATE);
        initDefaulParam(speechRecognizer, sharedPreferences);
    }

    private static void initDefaulParam(SpeechRecognizer speechRecognizer, SharedPreferences mTtsSharedPreferences) {
        if (speechRecognizer == null) {
            return;
        }
        // 清空参数
        speechRecognizer.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        speechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, engineType);
        // 设置返回结果格式
        speechRecognizer.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        speechRecognizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        speechRecognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
//        speechRecognizer.setParameter(SpeechConstant.VAD_BOS, mTtsSharedPreferences.getString("iat_vadbos_preference", "5000"));
        speechRecognizer.setParameter(SpeechConstant.VAD_BOS, "5000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
//        speechRecognizer.setParameter(SpeechConstant.VAD_EOS, mTtsSharedPreferences.getString("iat_vadeos_preference", "1500"));
        speechRecognizer.setParameter(SpeechConstant.VAD_EOS, "1500");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
//        speechRecognizer.setParameter(SpeechConstant.ASR_PTT, mTtsSharedPreferences.getString("iat_punc_preference", "0"));
        speechRecognizer.setParameter(SpeechConstant.ASR_PTT, "0");

        String language = mTtsSharedPreferences.getString("iat_language_preference", "mandarin");

        if (language.equals("en_us")) {
            // 设置语言
            speechRecognizer.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            speechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            speechRecognizer.setParameter(SpeechConstant.ACCENT, language);
        }

    }

    /**
     * 开始识别
     */
    public void startRecognize(Context context) {
        startRecognize(context, (InitListener) null);
    }

    /**
     * 开始识别
     */
    public void startRecognize(Context context, InitListener initListener) {
        startRecognize(context, initListener, false);
    }


    /**
     * 开始识别
     */
    public void startRecognize(Context context, InitListener initListener, boolean defaultParam) {
        startRecognize(context, initListener, defaultParam, null);
    }

    /**
     * 开始识别
     */
    public static void startRecognize(Context context, RecognizerListener recognizerListener) {
        startRecognize(context, null, recognizerListener);
    }

    /**
     * 开始识别
     */
    public static void startRecognize(Context context, InitListener initListener, RecognizerListener recognizerListener) {
        startRecognize(context, initListener, false, recognizerListener);
    }


    /**
     * 开始识别
     */
    public static void startRecognize(Context context, InitListener initListener, boolean defaultParam, RecognizerListener recognizerListener) {

        SpeechRecognizer speechRecognizer = initSpeechRecognizer(context, initListener, defaultParam);

        if (speechRecognizer != null && !speechRecognizer.isListening() && speechRecognizer != null) {
            if (recognizerListener == null) {
                recognizerListener = new MLRecognizerListener() {
                    @Override
                    public void onMLVolumeChanged(int i, byte[] bytes) {

                    }

                    @Override
                    public void onMLBeginOfSpeech() {

                    }

                    @Override
                    public void onMLEndOfSpeech() {

                    }

                    @Override
                    public void onMLResult(String result) {

                    }

                    @Override
                    public void onMLError(SpeechError error) {

                    }
                };
            }
            speechRecognizer.startListening(recognizerListener);
        }
    }


    /**
     * 停止识别
     */

    public static void stopListening() {
        SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
        if (recognizer != null && recognizer.isListening()) {
            recognizer.stopListening();
            recognizer.cancel();
        }
    }

    public static void stop() {
        SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
        if (recognizer != null) {
            recognizer.stopListening();
            recognizer.cancel();
        }
    }


    public static void destroy() {
        SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
        if (recognizer != null) {
            // 退出时释放连接
            recognizer.stopListening();
            recognizer.cancel();
            recognizer.destroy();
        }

    }
//
//    public static void startLoopRecognize(final Context context, @NonNull LoopInterface loopInterface) {
//        Observable.interval(500, 300, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .filter(aLong -> {
//                    SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
//                    return !recognizer.isListening();
//                })
//                .subscribe(aLong -> {
//                    MLVoiceRecognize.startRecognize(context.getApplicationContext(), new MLRecognizerListener() {
//                        @Override
//                        public void onMLVolumeChanged(int i, byte[] bytes) {
//                            loopInterface.onVolumeChanged(i, bytes);
//                        }
//
//                        @Override
//                        public void onMLBeginOfSpeech() {
//                            loopInterface.onBeginOfSpeech();
//
//                        }
//
//                        @Override
//                        public void onMLEndOfSpeech() {
//                            loopInterface.onEndOfSpeech();
//
//                        }
//
//                        @Override
//                        public void onMLResult(String result) {
//                            loopInterface.onResult(result);
//
//
//                        }
//
//                        @Override
//                        public void onMLError(SpeechError error) {
//
//                            loopInterface.onError(error);
//                        }
//
//                    });
//
//
//                });
//    }
}
