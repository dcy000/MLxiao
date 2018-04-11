package com.gzq.administrator.module_live;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.player.KSYTextureView;

import java.io.IOException;

import static com.gzq.administrator.module_live.MediaHelper.getCurrentSpan;
import static com.gzq.administrator.module_live.MediaHelper.getFocusX;
import static com.gzq.administrator.module_live.MediaHelper.getFocusY;

/**
 * Created by gzq on 2018/4/10.
 */

public class LivePlayActivity extends AppCompatActivity implements View.OnClickListener {
    private KSYTextureView ksyTextureView;
    private String playUrl;
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
    private TextView mTvPausePlay;
    private boolean isOnPlaying;
    private ImageView mIvBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liveplay);
        initView();
        MediaHelper.hideStatusBar(this);
        permissionCheck();
        startPlay(playUrl);
    }

    private void startPlay(String url) {

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

    private IMediaPlayer.OnErrorListener mOnErrorListener = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
            Log.d("播放异常", "onError: " + i);
            Toast.makeText(LivePlayActivity.this, "网络连接不稳定", Toast.LENGTH_SHORT).show();
            videoPlayEnd();
            finish();
            return false;
        }
    };
    private IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer iMediaPlayer) {
            ksyTextureView.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            ksyTextureView.start();
            isOnPlaying = true;
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
            finish();
            Toast.makeText(LivePlayActivity.this, "播放结束", Toast.LENGTH_SHORT).show();
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
//                        if (tvPlayActivity.getControlBarVisibility() == View.VISIBLE) {
//                            tvPlayActivity.hideControlBar();
//                        } else {
//                            tvPlayActivity.showControlBar();
//                        }
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    };


    public static void startActivity(Context context, String url, Class<?> clazz) {
        context.startActivity(new Intent(context, clazz)
                .putExtra("url", url));

    }

    private void permissionCheck() {
        int cameraPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int readPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (cameraPerm != PackageManager.PERMISSION_GRANTED ||
                readPerm != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Log.e("Player", "No CAMERA or AudioRecord permission, please check");
                Toast.makeText(this, "No CAMERA or AudioRecord permission, please check",
                        Toast.LENGTH_LONG).show();
            } else {
                String[] permissions = {Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissions,
                        1);
            }
        }
    }

    private void initView() {
        ksyTextureView = (KSYTextureView) findViewById(R.id.texture_view);
        playUrl = getIntent().getStringExtra("url");
//        FloatingPlayer.getInstance().init(this);
//        ksyTextureView = FloatingPlayer.getInstance().getKSYTextureView();
        mTvPausePlay = (TextView) findViewById(R.id.tv_pause_play);
        mTvPausePlay.setOnClickListener(this);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
    }

    private void videoPlayEnd() {
        if (ksyTextureView != null) {
            FloatingPlayer.getInstance().destroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ksyTextureView != null) {
            ksyTextureView.runInForeground();
            if (!isOnPlaying) {
                ksyTextureView.start();
                isOnPlaying = true;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ksyTextureView != null) {
            ksyTextureView.runInBackground(false);
            isOnPlaying = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pause_play:
                if (isOnPlaying) {
                    ksyTextureView.pause();
                    isOnPlaying = false;
                    mTvPausePlay.setText("开始播放");
                } else {
                    ksyTextureView.start();
                    isOnPlaying = true;
                    mTvPausePlay.setText("暂停播放");
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
