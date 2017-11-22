package com.medlink.danbogh.wakeup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;

/**
 * Created by lenovo on 2017/11/15.
 */

public class WakeupHelper {
    private static final String TAG = "WakeupHelper";

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    public static WakeupHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        @SuppressLint("StaticFieldLeak")
        private static WakeupHelper INSTANCE = new WakeupHelper();
    }

    private WakeupHelper() {
        VoiceWakeuper wakeuper = VoiceWakeuper.getWakeuper();
        if (wakeuper == null) {
            if (sContext == null) {
                Log.d(TAG, "WakeupHelper: must init sContext");
            }
            wakeuper = VoiceWakeuper.createWakeuper(sContext, null);
            if (wakeuper == null) {
                Log.d(TAG, "WakeupHelper: wakeup create failed");
            }
        }
    }

    public void startWakeuprListening(WakeuperListener listener) {
        VoiceWakeuper wakeuper = VoiceWakeuper.getWakeuper();
        if (wakeuper == null) {
            Log.d(TAG, "startWakeuprListening: wakeuper == null");
            return;
        }
        if (!wakeuper.isListening()) {
            setParameter(wakeuper);
            wakeuper.startListening(listener);
        }
    }

    private void setParameter(VoiceWakeuper wakeuper) {
        wakeuper.setParameter(SpeechConstant.PARAMS, null);
        wakeuper.setParameter(SpeechConstant.IVW_THRESHOLD, "0:40");
        wakeuper.setParameter(SpeechConstant.IVW_SST, "wakeup");
        wakeuper.setParameter(SpeechConstant.KEEP_ALIVE, "1");
        wakeuper.setParameter(SpeechConstant.IVW_NET_MODE, "1");
        wakeuper.setParameter(SpeechConstant.IVW_RES_PATH, getResource());
        wakeuper.setParameter(SpeechConstant.IVW_AUDIO_PATH,
                Environment.getExternalStorageDirectory().getPath() + "/msc/ivw.wav");
        wakeuper.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
    }

    private String getResource() {
        final String resPath = ResourceUtil.generateResourcePath(sContext,
                ResourceUtil.RESOURCE_TYPE.assets, "ivw/59196d96.jet");
        Log.d(TAG, "resPath: " + resPath);
        return resPath;
    }

    public void stopWakeuprListening() {
        VoiceWakeuper wakeuper = VoiceWakeuper.getWakeuper();
        if (wakeuper != null && wakeuper.isListening()) {
            wakeuper.stopListening();
        }
    }

    public void destroy() {
        VoiceWakeuper wakeuper = VoiceWakeuper.getWakeuper();
        if (wakeuper != null) {
            wakeuper.destroy();
        }
    }

    public void enableWakeuperListening(boolean enable) {
        if (enable) {
            startWakeuprListening(wakeuperlistener());
        } else {
            stopWakeuprListening();
        }
    }

    private WakeuperListener listener;

    private WakeuperListener wakeuperlistener() {
        if (listener == null) {
            listener = new AbsWakeuperListener() {
                @Override
                public void onResult(WakeuperResult wakeuperResult) {
                    Intent intent = new Intent(sContext, SpeechSynthesisActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sContext.startActivity(intent);
                }
            };
        }
        return listener;
    }
}