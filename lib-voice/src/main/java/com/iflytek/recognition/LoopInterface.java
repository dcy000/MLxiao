package com.iflytek.recognition;

import com.iflytek.cloud.SpeechError;

/**
 * Created by lenovo on 2018/8/17.
 */

public interface LoopInterface {

        void onVolumeChanged(int i, byte[] bytes);

        void onBeginOfSpeech();

        void onEndOfSpeech();

        void onResult(String result);

        void onError(SpeechError error);
}
