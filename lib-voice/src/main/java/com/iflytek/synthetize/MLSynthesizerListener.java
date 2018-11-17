package com.iflytek.synthetize;

import android.os.Bundle;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;

import timber.log.Timber;

/**
 * Created by lenovo on 2018/2/1.
 */

public class MLSynthesizerListener implements SynthesizerListener {
    @Override
    public void onSpeakBegin() {
        Timber.i("MLSynthesizerListener>>>>>>>>========>>>>>>onSpeakBegin()");
    }

    @Override
    public void onBufferProgress(int i, int i1, int i2, String s) {
        Timber.i("MLSynthesizerListener>>>>>>>>========>>>>>>onBufferProgress()" + i + ">>>" + i1 + ">>>>" + i2 + ">>>>" + s);
    }

    @Override
    public void onSpeakPaused() {
        Timber.i("MLSynthesizerListener>>>>>>>>========>>>>>>onSpeakPaused()");
    }

    @Override
    public void onSpeakResumed() {
        Timber.i("MLSynthesizerListener>>>>>>>>========>>>>>>onSpeakResumed()");
    }

    @Override
    public void onSpeakProgress(int i, int i1, int i2) {
        Timber.i("MLSynthesizerListener>>>>>>>>========>>>>>>onSpeakProgress()" + i + ">>>" + i1 + ">>>>" + i2);
    }

    @Override
    public void onCompleted(SpeechError speechError) {
        Timber.i("MLSynthesizerListener>>>>>>>>========>>>>>>onCompleted()" + (speechError == null ? "" : speechError.getErrorDescription()));
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {
        Timber.i("MLSynthesizerListener>>>>>>>>========>>>>>>onEvent()" + i + ">>>" + i1 + ">>>>" + i2 + ">>>" + bundle);
    }
}
