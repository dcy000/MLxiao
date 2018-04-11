package com.gzq.administrator.module_live;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ksyun.media.streamer.encoder.VideoEncodeFormat;
import com.ksyun.media.streamer.filter.imgtex.ImgFilterBase;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterBase;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterMgt;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.kit.StreamerConstants;

/**
 * Created by gzq on 2018/4/10.
 */

public class LiveShowActivity extends AppCompatActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {
    private GLSurfaceView mGsvLiveView;
    private TextView mBtnPlay;
    private LiveBaseConfig mConfig;
    private KSYStreamer mStreamer;
    private boolean mIsLandscape = false;
    private boolean mHWEncoderUnsupported;
    private boolean mSWEncoderUnsupported;
    private Handler mMainHandler;
    private boolean mStreaming;
    protected String mBgImagePath = "assets://bg.jpg";
    protected static final int PERMISSION_REQUEST_CAMERA_AUDIOREC = 1;
    private ImageView mIvBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liveshow);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        MediaHelper.hideStatusBar(this);
        initView();
        initLiveStream();
        enableBeautyFilter();
        // 是否自动开始推流
        if (mConfig.mAutoStart) {
            startStream();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handleOnPause();
    }

    protected void handleOnResume() {
        // 调用KSYStreamer的onResume接口
        mStreamer.onResume();
        // 停止背景图采集
        mStreamer.stopImageCapture();
        // 开启摄像头采集
        startCameraPreviewWithPermCheck();
        // 如果onPause中切到了DummyAudio模块，可以在此恢复
        mStreamer.setUseDummyAudioCapture(false);
    }

    protected void handleOnPause() {
        // 调用KSYStreamer的onPause接口
        mStreamer.onPause();
        // 停止摄像头采集，然后开启背景图采集，以实现后台背景图推流功能
        mStreamer.stopCameraPreview();
        mStreamer.startImageCapture(mBgImagePath);
        // 如果希望App切后台后，停止录制主播端的声音，可以在此切换为DummyAudio采集，
        // 该模块会代替mic采集模块产生静音数据，同时释放占用的mic资源
        mStreamer.setUseDummyAudioCapture(true);
    }

    protected void startCameraPreviewWithPermCheck() {
        int cameraPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int audioPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (cameraPerm != PackageManager.PERMISSION_GRANTED ||
                audioPerm != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Log.e(TAG, "No CAMERA or AudioRecord permission, please check");
                Toast.makeText(getApplicationContext(), "No CAMERA or AudioRecord permission, please check",
                        Toast.LENGTH_LONG).show();
            } else {
                String[] permissions = {Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE};
                ActivityCompat.requestPermissions(this, permissions,
                        PERMISSION_REQUEST_CAMERA_AUDIOREC);
            }
        } else {
            mStreamer.startCameraPreview();
        }
    }

    protected void enableBeautyFilter() {
        // 设置美颜滤镜的错误回调，当前机型不支持该滤镜时禁用美颜
        mStreamer.getImgTexFilterMgt().setOnErrorListener(new ImgTexFilterBase.OnErrorListener() {
            @Override
            public void onError(ImgTexFilterBase filter, int errno) {
                Toast.makeText(getApplicationContext(), "当前机型不支持该滤镜",
                        Toast.LENGTH_SHORT).show();
                mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                        ImgTexFilterMgt.KSY_FILTER_BEAUTY_DISABLE);
            }
        });
        // 设置美颜滤镜，关于美颜滤镜的具体说明请参见专题说明以及完整版demo
        mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                ImgTexFilterMgt.KSY_FILTER_BEAUTY_PRO3);
    }

    protected void showWaterMark() {
        if (!mIsLandscape) {
            mStreamer.showWaterMarkLogo("", 0.08f, 0.04f, 0.20f, 0, 0.8f);
            mStreamer.showWaterMarkTime(0.03f, 0.01f, 0.35f, Color.WHITE, 1.0f);
        } else {
            mStreamer.showWaterMarkLogo("", 0.05f, 0.09f, 0, 0.20f, 0.8f);
            mStreamer.showWaterMarkTime(0.01f, 0.03f, 0.22f, Color.WHITE, 1.0f);
        }
    }

    protected void hideWaterMark() {
        mStreamer.hideWaterMarkLogo();
        mStreamer.hideWaterMarkTime();
    }

    private void initLiveStream() {
        if (mConfig == null) {
            Toast.makeText(this, "基本配置有问题", Toast.LENGTH_SHORT).show();
            return;
        }
        mStreamer = new KSYStreamer(this);
        mStreamer.setUrl(mConfig.mUrl);
        // 设置推流分辨率
        mStreamer.setPreviewResolution(mConfig.mTargetResolution);
        mStreamer.setTargetResolution(mConfig.mTargetResolution);
        // 设置编码方式（硬编、软编）
        mStreamer.setEncodeMethod(mConfig.mEncodeMethod);
        // 硬编模式下默认使用高性能模式(high profile)
        if (mConfig.mEncodeMethod == StreamerConstants.ENCODE_METHOD_HARDWARE) {
            mStreamer.setVideoEncodeProfile(VideoEncodeFormat.ENCODE_PROFILE_HIGH_PERFORMANCE);
        }

        // 设置推流帧率
        if (mConfig.mFrameRate > 0) {
            mStreamer.setPreviewFps(mConfig.mFrameRate);
            mStreamer.setTargetFps(mConfig.mFrameRate);
        }

        // 设置推流视频码率，三个参数分别为初始码率、最高码率、最低码率
        int videoBitrate = mConfig.mVideoKBitrate;
        if (videoBitrate > 0) {
            mStreamer.setVideoKBitrate(videoBitrate * 3 / 4, videoBitrate, videoBitrate / 4);
        }

        // 设置音频码率
        if (mConfig.mAudioKBitrate > 0) {
            mStreamer.setAudioKBitrate(mConfig.mAudioKBitrate);
        }

        // 设置视频方向（横屏、竖屏）
        if (mConfig.mOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mIsLandscape = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            mStreamer.setRotateDegrees(90);
        } else if (mConfig.mOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            mIsLandscape = false;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            mStreamer.setRotateDegrees(0);
        }

        // 选择前后摄像头
        mStreamer.setCameraFacing(mConfig.mCameraFacing);

        // 设置预览View
        mStreamer.setDisplayPreview(mGsvLiveView);
        // 设置回调处理函数
        mStreamer.setOnInfoListener(mOnInfoListener);
        mStreamer.setOnErrorListener(mOnErrorListener);
        // 禁用后台推流时重复最后一帧的逻辑（这里我们选择切后台使用背景图推流的方式）
        mStreamer.setEnableRepeatLastFrame(false);
    }

    private KSYStreamer.OnInfoListener mOnInfoListener = new KSYStreamer.OnInfoListener() {
        @Override
        public void onInfo(int what, int msg1, int msg2) {
            onStreamerInfo(what, msg1, msg2);
        }
    };

    private KSYStreamer.OnErrorListener mOnErrorListener = new KSYStreamer.OnErrorListener() {
        @Override
        public void onError(int what, int msg1, int msg2) {
            onStreamerError(what, msg1, msg2);
        }
    };

    public static void startActivity(Context context, LiveBaseConfig params, Class<?> clazz) {
        context.startActivity(new Intent(context, clazz)
                .putExtra("liveConfig", params));
    }

    private static final String TAG = "LiveShowActivity";

    protected void onStreamerInfo(int what, int msg1, int msg2) {
        Log.d(TAG, "OnInfo: " + what + " msg1: " + msg1 + " msg2: " + msg2);
        switch (what) {
            case StreamerConstants.KSY_STREAMER_CAMERA_INIT_DONE:
                Log.d(TAG, "KSY_STREAMER_CAMERA_INIT_DONE");
                break;
            case StreamerConstants.KSY_STREAMER_CAMERA_FACING_CHANGED:
                Log.d(TAG, "KSY_STREAMER_CAMERA_FACING_CHANGED");
                // check is flash torch mode supported
//                mFlashVisetEnabledew.(mStreamer.getCameraCapture().isTorchSupported());
                break;
            case StreamerConstants.KSY_STREAMER_OPEN_STREAM_SUCCESS:
                Log.d(TAG, "KSY_STREAMER_OPEN_STREAM_SUCCESS");
                mBtnPlay.setText("停止直播");
//                startChronometer();
                break;
            case StreamerConstants.KSY_STREAMER_FRAME_SEND_SLOW:
                Log.d(TAG, "KSY_STREAMER_FRAME_SEND_SLOW " + msg1 + "ms");
                Toast.makeText(getApplicationContext(), "Network not good!",
                        Toast.LENGTH_SHORT).show();
                break;
            case StreamerConstants.KSY_STREAMER_EST_BW_RAISE:
                Log.d(TAG, "BW raise to " + msg1 / 1000 + "kbps");
                break;
            case StreamerConstants.KSY_STREAMER_EST_BW_DROP:
                Log.d(TAG, "BW drop to " + msg1 / 1000 + "kpbs");
                break;
            default:
                break;
        }
    }

    protected void onStreamerError(int what, int msg1, int msg2) {
        Log.e(TAG, "streaming error: what=" + what + " msg1=" + msg1 + " msg2=" + msg2);
        switch (what) {
            case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
            case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                break;
            case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
            case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
            case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_EVICTED:
            case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
                mStreamer.stopCameraPreview();
                break;
            case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED:
            case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN:
                handleEncodeError();
            default:
                reStreaming(what);
                break;
        }
    }

    protected void reStreaming(int err) {
        stopStream();
        mMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startStream();
            }
        }, 3000);
    }

    protected void startStream() {
        mStreamer.startStream();
        mBtnPlay.setText("停止直播");
        mStreaming = true;
    }

    // stop streaming
    protected void stopStream() {
        mStreamer.stopStream();
        mBtnPlay.setText("开始直播");
        mStreaming = false;
    }

    protected void handleEncodeError() {
        int encodeMethod = mStreamer.getVideoEncodeMethod();
        if (encodeMethod == StreamerConstants.ENCODE_METHOD_HARDWARE) {
            mHWEncoderUnsupported = true;
            if (mSWEncoderUnsupported) {
                mStreamer.setEncodeMethod(
                        StreamerConstants.ENCODE_METHOD_SOFTWARE_COMPAT);
                Log.e(TAG, "Got HW encoder error, switch to SOFTWARE_COMPAT mode");
            } else {
                mStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
                Log.e(TAG, "Got HW encoder error, switch to SOFTWARE mode");
            }
        } else if (encodeMethod == StreamerConstants.ENCODE_METHOD_SOFTWARE) {
            mSWEncoderUnsupported = true;
            mStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE_COMPAT);
            Log.e(TAG, "Got SW encoder error, switch to SOFTWARE_COMPAT mode");
        }
    }

    private void initView() {
        mGsvLiveView = (GLSurfaceView) findViewById(R.id.gsv_live_view);
        mBtnPlay = (TextView) findViewById(R.id.btn_play);
        mBtnPlay.setOnClickListener(this);
        mConfig = (LiveBaseConfig) getIntent().getSerializableExtra("liveConfig");
        mMainHandler = new Handler();
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_play:
                if (mStreaming) {
                    stopStream();
                } else {
                    startStream();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            MediaHelper.hideStatusBar(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理相关资源
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
        mStreamer.release();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA_AUDIOREC: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mStreamer.startCameraPreview();
                } else {
                    Log.e(TAG, "No CAMERA or AudioRecord permission");
                    Toast.makeText(getApplicationContext(), "No CAMERA or AudioRecord permission",
                            Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }
}
