package com.example.module_register.ui.base;

import android.arch.lifecycle.Lifecycle;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gcml.lib_widget.dialog.LoadingDialog;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.Handlers;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ScreenUtils;
import com.iflytek.cloud.SpeechError;
import com.iflytek.recognition.MLRecognizerListener;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.wake.MLVoiceWake;
import com.iflytek.wake.MLWakeuperListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import timber.log.Timber;

public abstract class VoiceToolBarActivity extends ToolbarBaseActivity {
    private VoiceLineView voiceLineView;
    private LoadingDialog mLoadingDialog;
    private MediaRecorder mMediaRecorder;

    @Override
    public void setContentView(int layoutResID) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        LinearLayout parent = new LinearLayout(this);
        parent.setOrientation(LinearLayout.VERTICAL);
        viewGroup.addView(parent);
        if (isShowToolbar()) {
            mToolbar = LayoutInflater.from(this).inflate(com.gcml.lib_widget.R.layout.toolbar_layout, parent, true);
            initToolbar();
        }
        LayoutInflater.from(this).inflate(layoutResID, parent, true);

        if (isShowVoiceView()) {
            voiceLineView = new VoiceLineView(this);
            voiceLineView.setBackgroundColor(Color.parseColor("#00000000"));
            voiceLineView.setAnimation(AnimationUtils.loadAnimation(this, com.gcml.lib_widget.R.anim.popup_health_fade_in));
            int width = provideWaveViewWidth();
            int height = provideWaveViewHeight();
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
            params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            viewGroup.addView(voiceLineView, params);
            viewGroup.bringToFront();
            voiceLineView.setVisibility(View.GONE);
        }
    }

    protected int provideWaveViewWidth() {
        return ScreenUtils.dp2px(450);
    }

    protected int provideWaveViewHeight() {
        return ScreenUtils.dp2px(120);
    }

    public boolean isShowVoiceView() {
        return false;
    }

    protected void robotStartListening() {
        Observable.interval(0, 200, TimeUnit.MILLISECONDS)
                .as(RxUtils.<Long>autoDisposeConverter(this, Lifecycle.Event.ON_PAUSE))
                .subscribe(new CommonObserver<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        MLVoiceRecognize.startRecognize(recognizerListener);
                    }
                });
    }

    private MLRecognizerListener recognizerListener = new MLRecognizerListener() {
        @Override
        public void onMLVolumeChanged(int i, byte[] bytes) {
            updateVolume();
        }

        @Override
        public void onMLBeginOfSpeech() {
            showWaveView(true);
        }

        @Override
        public void onMLEndOfSpeech() {
            showWaveView(false);
        }

        @Override
        public void onMLResult(String result) {
            if (!TextUtils.isEmpty(result)) {
                onSpeakListenerResult(result);
            }
        }

        @Override
        public void onMLError(SpeechError error) {

        }
    };

    protected void robotStopListening() {
        MLVoiceRecognize.stop();
    }

    protected void onSpeakListenerResult(String result) {
    }


    public void setDisableWakeup(boolean disableListener) {
        if (!disableListener) {
            MLVoiceWake.startWakeUp(new MLWakeuperListener() {
                @Override
                public void onMLError(int errorCode) {
                    Timber.e("小E唤醒失败：" + errorCode);
                }

                @Override
                public void onMLResult() {
                    Timber.i("小E唤醒成功");
                }
            });
        } else {
            MLVoiceWake.stopWakeUp();
        }
    }


    private Runnable updateVolumeAction = new Runnable() {
        @Override
        public void run() {
            if (mMediaRecorder == null) return;
            double ratio = (double) mMediaRecorder.getMaxAmplitude() / 100;
            if (ratio > 1) {
                volume = (int) (20 * Math.log10(ratio));
            }
            voiceLineView.setVolume(volume);

        }
    };

    private volatile int volume;

    protected void showWaveView(boolean visible) {
        if (voiceLineView != null) {
            voiceLineView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    protected void updateVolume() {
        if (isShowVoiceView()) {
            Handlers.ui().postDelayed(updateVolumeAction, 100);
        }
    }

    @Override
    protected void onPause() {
        robotStopListening();
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        //释放通知消息的资源
        Handlers.ui().removeCallbacks(updateVolumeAction);
        super.onPause();
    }
    public void showLoadingDialog(String tips) {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
        mLoadingDialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(tips)
                .create();
        mLoadingDialog.show();
    }

    public void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
    }
}
