package com.medlink.danbogh.wakeup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.WindowManager;

import com.example.han.referralproject.R;
import com.example.han.referralproject.speechsynthesis.SpeechSynthesisActivity;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;
import com.medlink.danbogh.wakeup.dialog.VoiceDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lenovo on 2017/11/15.
 */

public class WakeupHelper {
    private static final String TAG = "WakeupHelper";

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
    private VoiceDialog voiceDialog;

    public static void init(Context context) {
        sContext = context.getApplicationContext();
    }

    public static WakeupHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        @SuppressLint("StaticFieldLeak")
        private static WakeupHelper INSTANCE = new WakeupHelper();
    }

    private WakeupHelper() {
        VoiceWakeuper.createWakeuper(sContext, new InitListener() {
            @Override
            public void onInit(int code) {
                Log.d(TAG, "onInit: " + code);
            }
        });
    }

    private volatile boolean inited;

    public synchronized void startWakeuprListening(final WakeuperListener listener) {
        if (VoiceWakeuper.getWakeuper() == null) {
            if (sContext == null) {
                Log.e(TAG, "WakeupHelper: must init sContext");
            }
            VoiceWakeuper.createWakeuper(sContext, new InitListener() {
                @Override
                public void onInit(int code) {
                    Log.d(TAG, "onInit: " + code);
                }
            });
        }
        VoiceWakeuper wakeuper = VoiceWakeuper.getWakeuper();
        if (wakeuper == null) {
            Log.d(TAG, "startWakeuprListening: wakeuper == null");
            return;
        }
        inited = true;
        setParameter(wakeuper);
        if (!wakeuper.isListening()) {
            wakeuper.startListening(listener);
        }
    }

    private void setParameter(VoiceWakeuper wakeuper) {
        wakeuper.setParameter(SpeechConstant.PARAMS, null);
        wakeuper.setParameter(SpeechConstant.IVW_THRESHOLD, "0:10");
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

    public synchronized void stopWakeuprListening() {
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

    private volatile boolean enableCache;

    public synchronized void enableWakeuperListening(boolean enable) {
        if (inited && enableCache == enable) {
            return;
        }
        enableCache = enable;
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
                public void onResult(WakeuperResult result) {
                    String json = result.getResultString();
                    try {
                        JSONObject jsonObj = new JSONObject(json);
                        int score = jsonObj.optInt("score");
                        if (score >= 20) {
//                            Intent intent = new Intent(sContext, SpeechSynthesisActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            sContext.startActivity(intent);
                            String userId = UserSpHelper.getUserId();
                            if (TextUtils.isEmpty(userId)) {
                                MLVoiceSynthetize.startSynthesize(sContext, "如需使用唤醒功能,请先登录");
                                return;
                            }
                            if (voiceDialog == null) {
                                voiceDialog = new VoiceDialog(sContext, R.style.XDialog);
                                voiceDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                            }
                            if (voiceDialog != null && !voiceDialog.isShowing()) {
                                voiceDialog.show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        return listener;
    }
}