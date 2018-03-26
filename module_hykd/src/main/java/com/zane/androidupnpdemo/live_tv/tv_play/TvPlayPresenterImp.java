package com.zane.androidupnpdemo.live_tv.tv_play;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.player.KSYTextureView;
import com.zane.androidupnpdemo.live_tv.FloatingPlayer;

import java.io.IOException;

import static com.zane.androidupnpdemo.live_tv.MediaHelper.getCurrentSpan;
import static com.zane.androidupnpdemo.live_tv.MediaHelper.getFocusX;
import static com.zane.androidupnpdemo.live_tv.MediaHelper.getFocusY;

/**
 * Created by gzq on 2018/3/26.
 */

public class TvPlayPresenterImp implements ITvPlayPresenter {
    private KSYTextureView ksyTextureView;
    private ITvPlayView tvPlayActivity;

    /**
     * 视频设置
     */
    private Boolean mTouching;
    private float centerPointX;
    private float centerPointY;
    private float lastMoveX = -1;
    private float lastMoveY = -1;
    private float movedDeltaX;
    private float movedDeltaY;
    private float totalRatio;
    private float deltaRatio;
    private double lastSpan;
    private TimeCount timeCount;

    public TvPlayPresenterImp(ITvPlayView tvPlayActivity) {
        this.tvPlayActivity = tvPlayActivity;
        FloatingPlayer.getInstance().init((Context) tvPlayActivity);
        ksyTextureView = FloatingPlayer.getInstance().getKSYTextureView();
        timeCount = new TimeCount(5000, 1000);
    }

    @Override
    public void startPlay(String url) {
        if (tvPlayActivity != null) {
            timeCount.start();
            tvPlayActivity.showLoadingDialog();
            tvPlayActivity.hideStatusBar();
            tvPlayActivity.addVideoView(ksyTextureView);


            ksyTextureView.setOnTouchListener(mTouchListener);
            ksyTextureView.setOnPreparedListener(mOnPreparedListener);
            ksyTextureView.setOnErrorListener(mOnErrorListener);
            ksyTextureView.setOnInfoListener(mOnInfoListener);
            ksyTextureView.setOnCompletionListener(mOnCompletionListener);
            ksyTextureView.setVolume(1.0f, 1.0f);
            ksyTextureView.setBufferTimeMax(3);
            ksyTextureView.setBufferSize(15);
            ksyTextureView.setTimeout(5, 30);

            try {
                ksyTextureView.setDataSource(url);
                ksyTextureView.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        if (ksyTextureView != null) {
            ksyTextureView.runInForeground();
            if (!ksyTextureView.isPlaying())
                ksyTextureView.start();
        }
    }

    @Override
    public void onPause() {
        if (ksyTextureView != null)
            ksyTextureView.runInBackground(false);
    }

    //事件监听
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
//            KSYTextureView mVideoView = FloatingPlayer.getInstance().getKSYTextureView();

            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    mTouching = false;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    mTouching = true;
                    if (event.getPointerCount() == 2) {
                        lastSpan = getCurrentSpan(event);
                        centerPointX = getFocusX(event);
                        centerPointY = getFocusY(event);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (event.getPointerCount() == 1) {
                        float posX = event.getX();
                        float posY = event.getY();
                        if (lastMoveX == -1 && lastMoveX == -1) {
                            lastMoveX = posX;
                            lastMoveY = posY;
                        }
                        movedDeltaX = posX - lastMoveX;
                        movedDeltaY = posY - lastMoveY;

                        if (Math.abs(movedDeltaX) > 5 || Math.abs(movedDeltaY) > 5) {
                            //判断调节音量和亮度 还是缩放画面
                            if (ksyTextureView != null) {
                                ksyTextureView.moveVideo(movedDeltaX, movedDeltaY);
                            }
                            mTouching = true;
                        }
                        lastMoveX = posX;
                        lastMoveY = posY;

                    } else if (event.getPointerCount() == 2) {
                        double spans = getCurrentSpan(event);
                        if (spans > 5) {
                            deltaRatio = (float) (spans / lastSpan);
                            totalRatio = ksyTextureView.getVideoScaleRatio() * deltaRatio;
                            if (ksyTextureView != null) {
                                ksyTextureView.setVideoScaleRatio(totalRatio, centerPointX, centerPointY);
                            }
                            lastSpan = spans;
                        }
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    if (event.getPointerCount() == 2) {
                        lastMoveX = -1;
                        lastMoveY = -1;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    lastMoveX = -1;
                    lastMoveY = -1;
                    if (!mTouching) {
                        if (tvPlayActivity.getControlBarVisibility() == View.VISIBLE) {
                            tvPlayActivity.hideControlBar();
                            timeCount.cancel();
                        } else {
                            tvPlayActivity.showControlBar();
                            timeCount.start();
                        }
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    private IMediaPlayer.OnErrorListener mOnErrorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
            Log.d("播放异常", "onError: " + i);
            Toast.makeText((Context) tvPlayActivity, "网络连接不稳定", Toast.LENGTH_SHORT).show();
            videoPlayEnd();
            tvPlayActivity.closeTv();
            return false;
        }
    };
    private IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            tvPlayActivity.hideLoadingDialog();
            ksyTextureView.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            ksyTextureView.start();
            timeCount.start();
        }
    };

    private IMediaPlayer.OnInfoListener mOnInfoListener = new IMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
            switch (i) {
                case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    break;
                case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    break;
            }
            return false;
        }
    };

    private IMediaPlayer.OnCompletionListener mOnCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {
            tvPlayActivity.closeTv();
            Toast.makeText((Context) tvPlayActivity, "播放结束", Toast.LENGTH_SHORT).show();
        }
    };

    /* 定义一个倒计时的内部类 */
    private class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            if (tvPlayActivity.getControlBarVisibility() == View.VISIBLE) {
                tvPlayActivity.hideControlBar();
                timeCount.cancel();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示

        }
    }

    private void videoPlayEnd() {
        if (ksyTextureView != null) {
            FloatingPlayer.getInstance().destroy();
        }
    }

    @Override
    public void onDestroy() {
        tvPlayActivity = null;
    }
}
