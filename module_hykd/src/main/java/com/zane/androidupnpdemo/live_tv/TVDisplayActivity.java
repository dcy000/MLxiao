package com.zane.androidupnpdemo.live_tv;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.player.KSYTextureView;
import com.zane.androidupnpdemo.R;
import com.zane.androidupnpdemo.connect_tv.ui.TVConnectMainActivity;

import java.io.IOException;

import static com.zane.androidupnpdemo.live_tv.MediaHelper.getCurrentSpan;
import static com.zane.androidupnpdemo.live_tv.MediaHelper.getFocusX;
import static com.zane.androidupnpdemo.live_tv.MediaHelper.getFocusY;

public class TVDisplayActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mVideoPlay;
    private ImageView mLiveBack;
    private RelativeLayout liveControl;
    private String url;
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
    //视频旋转
    private int degree = 0;

    private TextView mLiveTitle;
    private KSYTextureView ksyTextureView;
    private TimeCount timeCount;
    private LinearLayout loaddingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_display);
        hideStatusBar();
        initView();
        FloatingPlayer.getInstance().init(this);
        ksyTextureView=FloatingPlayer.getInstance().getKSYTextureView();

        startToPlay();
    }


    private void startToPlay() {

        mVideoPlay.addView(ksyTextureView);

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

    @Override
    protected void onPause() {
        super.onPause();
        if (ksyTextureView != null)
            ksyTextureView.runInBackground(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ksyTextureView != null) {
            ksyTextureView.runInForeground();
            if (!ksyTextureView.isPlaying())
                ksyTextureView.start();
        }
    }

    private IMediaPlayer.OnCompletionListener mOnCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer iMediaPlayer) {
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

    private void videoPlayEnd() {
        if (ksyTextureView != null) {
            FloatingPlayer.getInstance().destroy();
        }
    }

    private IMediaPlayer.OnErrorListener mOnErrorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
            Log.d("播放异常", "onError: "+i);
            Toast.makeText(TVDisplayActivity.this, "网络连接不稳定", Toast.LENGTH_SHORT).show();
            videoPlayEnd();
            finish();
            return false;
        }
    };
    private IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            loaddingView.setVisibility(View.GONE);
            ksyTextureView.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            ksyTextureView.start();
            timeCount.start();
        }
    };
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
                        if (liveControl.getVisibility()==View.VISIBLE){
                            liveControl.setVisibility(View.GONE);
                            timeCount.cancel();
                        }else{
                            liveControl.setVisibility(View.VISIBLE);
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

    private void initView() {
        url=getIntent().getStringExtra("url");
        mVideoPlay = (LinearLayout) findViewById(R.id.video_play);
        mLiveBack = (ImageView) findViewById(R.id.livemedia_back);
        mLiveBack.setOnClickListener(this);
        mLiveTitle = (TextView) findViewById(R.id.live_title);
        liveControl=findViewById(R.id.live_control);
        loaddingView=findViewById(R.id.loadding_view);
        findViewById(R.id.connectTv).setOnClickListener(this);
        timeCount=new TimeCount(5000,1000);
    }

    private void showStatusBar() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(params);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void hideStatusBar() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.livemedia_back){
            finish();
        }else if(v.getId()==R.id.connectTv){
            startActivity(new Intent(this, TVConnectMainActivity.class).putExtra("url",url));
        }
    }

    /* 定义一个倒计时的内部类 */
    private class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            if (liveControl.getVisibility()==View.VISIBLE){
                liveControl.setVisibility(View.GONE);
                timeCount.cancel();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示

        }
    }
}
