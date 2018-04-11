package com.gzq.administrator.module_live;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ksyun.media.streamer.capture.CameraCapture;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.ksyun.media.streamer.util.device.DeviceInfo;
import com.ksyun.media.streamer.util.device.DeviceInfoTools;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mTvUrl;
    private Button mBtnLive;
    private Button mBtnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mTvUrl = (EditText) findViewById(R.id.tv_url);
        mBtnLive = (Button) findViewById(R.id.btn_live);
        mBtnLive.setOnClickListener(this);
        mBtnPlay = (Button) findViewById(R.id.btn_play);
        mBtnPlay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_live:
                String url=mTvUrl.getText().toString().trim();
                if (TextUtils.isEmpty(url)){
                    Toast.makeText(this, "直播推流地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                LiveBaseConfig config=setLiveBaseConfig(url);
                LiveShowActivity.startActivity(this,config,LiveShowActivity.class);
                break;
            case R.id.btn_play:
                String playUrl=mTvUrl.getText().toString().trim();
                if (TextUtils.isEmpty(playUrl)){
                    Toast.makeText(this, "播放推流地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                LivePlayActivity.startActivity(this,playUrl,LivePlayActivity.class);
                break;
        }
    }

    private LiveBaseConfig setLiveBaseConfig(String url) {
        LiveBaseConfig baseConfig=new LiveBaseConfig();
        baseConfig.mUrl=url;//直播的推流地址
        baseConfig.mFrameRate = 15.0f;//默认视屏帧率
        baseConfig.mVideoKBitrate = 1000;//默认视频最大码率
        baseConfig.mAudioKBitrate = 48;//默认音频码率
        baseConfig.mCameraFacing = CameraCapture.FACING_FRONT;//设置前置或者后置摄像头的方向
        baseConfig.mTargetResolution = StreamerConstants.VIDEO_RESOLUTION_540P;//推流分辨率，有360,480,540,720四个档
        baseConfig.mOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;//横屏或者竖屏
        if (isHw264EncoderSupported()) {//设置编码方式
            baseConfig.mEncodeMethod = StreamerConstants.ENCODE_METHOD_HARDWARE;
        } else {
            baseConfig.mEncodeMethod = StreamerConstants.ENCODE_METHOD_SOFTWARE;
        }
        baseConfig.mAutoStart=true;//是否在参数初始化完成之后马上开始直播
        baseConfig.mShowDebugInfo=true;//是否显示调试信息
        return baseConfig;
    }
    protected boolean isHw264EncoderSupported() {
        DeviceInfo deviceInfo = DeviceInfoTools.getInstance().getDeviceInfo();
        if (deviceInfo != null) {
            return deviceInfo.encode_h264 == DeviceInfo.ENCODE_HW_SUPPORT;
        }
        return false;
    }
}
