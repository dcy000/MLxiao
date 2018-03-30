package com.iflytek.recognition;

import android.os.Bundle;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;

/**
 * Created by lenovo on 2018/2/1.
 */

public abstract class MLRecognizerListener implements RecognizerListener {

    @Override
    public void onVolumeChanged(int i, byte[] bytes) {
        onMLVolumeChanged(i, bytes);
    }

    @Override
    public void onBeginOfSpeech() {
        onMLBeginOfSpeech();
    }

    @Override
    public void onEndOfSpeech() {
        onMLEndOfSpeech();
    }

    @Override
    public void onResult(RecognizerResult recognizerResult, boolean last) {
        dealData(recognizerResult, last);
    }


    @Override
    public void onError(SpeechError speechError) {
        onMLError(speechError);
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {

    }


    private void dealData(RecognizerResult recognizerResult, boolean last) {
        StringBuffer result = DataHandler.printResult(recognizerResult);
        if (last) {
            onMLResult(result.toString());
        }
    }

    public abstract void onMLVolumeChanged(int i, byte[] bytes);

    public abstract void onMLBeginOfSpeech();

    public abstract void onMLEndOfSpeech();

    public abstract void onMLResult(String result);

    public abstract void onMLError(SpeechError error);


}
