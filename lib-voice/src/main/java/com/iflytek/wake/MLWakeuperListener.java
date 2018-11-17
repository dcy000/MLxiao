package com.iflytek.wake;

import android.content.Intent;
import android.os.Bundle;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lenovo on 2018/4/10.
 */

public abstract class MLWakeuperListener implements WakeuperListener {
    public static int WAKE_UP_SCORE = 10;


    @Override
    public void onBeginOfSpeech() {

    }

    @Override
    public void onResult(WakeuperResult result) {
        String json = result.getResultString();
        try {
            JSONObject jsonObj = new JSONObject(json);
            int score = jsonObj.optInt("score");
            if (score >= WAKE_UP_SCORE) {
                onMLResult();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onError(SpeechError speechError) {
        int errorCode = speechError.getErrorCode();
        onMLError(errorCode);
    }


    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {

    }

    @Override
    public void onVolumeChanged(int i) {

    }

    public abstract void onMLError(int errorCode);

    public abstract void onMLResult();


}
