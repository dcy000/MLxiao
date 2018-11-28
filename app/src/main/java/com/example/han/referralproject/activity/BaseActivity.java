package com.example.han.referralproject.activity;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.carlos.voiceline.mylibrary.VoiceLineView;
import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.gcml.lib_widget.dialog.LoadingDialog;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ScreenUtils;
import com.iflytek.cloud.SpeechError;
import com.iflytek.recognition.MLRecognizerListener;
import com.iflytek.recognition.MLVoiceRecognize;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.iflytek.wake.MLVoiceWake;
import com.iflytek.wake.MLWakeuperListener;
import com.medlink.danbogh.utils.Handlers;
import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import timber.log.Timber;

public class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected Resources mResources;
    protected LayoutInflater mInflater;
    private LinearLayout rootView;
    private View mTitleView;
    protected TextView mTitleText;
    protected TextView mRightText;
    protected ImageView mLeftView;
    protected ImageView mRightView;
    protected TextView mLeftText;
    protected RelativeLayout mToolbar;
    protected LinearLayout mllBack;
    protected boolean isShowVoiceView = false;//是否显示声音录入图像
    private MediaRecorder mMediaRecorder;
    private LoadingDialog mLoadingDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mResources = getResources();
        mInflater = LayoutInflater.from(this);
        rootView = new LinearLayout(this);
        rootView.setOrientation(LinearLayout.VERTICAL);
        mTitleView = mInflater.inflate(R.layout.custom_title_layout, null);

        rootView.addView(mTitleView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (70 * mResources.getDisplayMetrics().density)));
        initToolbar();
    }


    private long lastTimeMillis = -1;
    private static final long DURATION = 500L;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            long currentTimeMillis = System.currentTimeMillis();
            if (lastTimeMillis != -1) {
                long elapsedTime = currentTimeMillis - lastTimeMillis;
                if (elapsedTime < DURATION) {
                    lastTimeMillis = currentTimeMillis;
                    return true;
                }
            }
            lastTimeMillis = currentTimeMillis;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void initToolbar() {
        mllBack = (LinearLayout) mTitleView.findViewById(R.id.ll_back);
        mToolbar = (RelativeLayout) mTitleView.findViewById(R.id.toolbar);
        mTitleText = (TextView) mTitleView.findViewById(R.id.tv_top_title);
        mLeftText = (TextView) mTitleView.findViewById(R.id.tv_top_left);
        mRightText = (TextView) mTitleView.findViewById(R.id.tv_top_right);
        mLeftView = (ImageView) mTitleView.findViewById(R.id.iv_top_left);
        mRightView = (ImageView) mTitleView.findViewById(R.id.iv_top_right);
        mllBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backLastActivity();
            }
        });
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backMainActivity();
            }
        });
    }

    /**
     * 返回上一页
     */
    protected void backLastActivity() {
        finish();
    }

    /**
     * 返回到主页面
     */
    protected void backMainActivity() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    protected VoiceLineView voiceLineView;

    protected FrameLayout mContentParent;

    protected int provideWaveViewWidth() {
        return ScreenUtils.dp2px(450);
    }

    protected int provideWaveViewHeight() {
        return ScreenUtils.dp2px(120);
    }

    @Override
    public void setContentView(int layoutResID) {
        mInflater.inflate(layoutResID, rootView);
        super.setContentView(rootView);
        if (isShowVoiceView) {
            mContentParent = (FrameLayout) findViewById(android.R.id.content);
            voiceLineView = new VoiceLineView(this);
            voiceLineView.setBackgroundColor(Color.parseColor("#00000000"));
            voiceLineView.setAnimation(AnimationUtils.loadAnimation(BaseActivity.this, R.anim.popshow_anim));
            int width = provideWaveViewWidth();
            int height = provideWaveViewHeight();
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
            params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            mContentParent.addView(voiceLineView, params);
            mContentParent.bringToFront();
            voiceLineView.setVisibility(View.GONE);
        }

    }

    @Override
    public void setContentView(View view) {
        rootView.addView(view);
        super.setContentView(rootView);
        if (isShowVoiceView) {
            mContentParent = (FrameLayout) findViewById(android.R.id.content);
            voiceLineView = new VoiceLineView(this);
            voiceLineView.setBackgroundColor(Color.parseColor("#00000000"));
            int width = ScreenUtils.dp2px(450);
            int height = ScreenUtils.dp2px(120);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, height);
            params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            params.bottomMargin = 20;
            mContentParent.addView(voiceLineView, params);
            mContentParent.bringToFront();
            voiceLineView.setVisibility(View.GONE);
        }
    }


    protected void robotStartListening() {
        Observable.interval(0, 200, TimeUnit.MILLISECONDS)
                .as(RxUtils.autoDisposeConverter(this, Lifecycle.Event.ON_PAUSE))
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
        if (isShowVoiceView) {
            Handlers.ui().postDelayed(updateVolumeAction, 100);
        }
    }

    protected void setShowVoiceView(boolean showVoiceView) {
        isShowVoiceView = showVoiceView;
    }


    protected void onActivitySpeakFinish() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
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
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoadingDialog();
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