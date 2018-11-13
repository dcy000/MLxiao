package com.example.han.referralproject.measure;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.bean.MeasureResult;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.healthdetection.HealthRecordActivity;

public class TemperatureMeasureActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mVideoTemperature;
    /**
     * 历史记录
     */
    private Button mHistory;
    /**
     * 使用演示
     */
    private Button mTemperatureVideo;
    private LinearLayout mDetectLlNav1;
    /**
     * 体温
     */
    private TextView mTextTemperature1;
    /**
     * 00.0
     */
    private TextView mTvResult;
    private RelativeLayout mRelativeLayout1;
    private ImageView mTest2;
    private RelativeLayout mRlTemp;
    private VideoView mVvTips;
    private FrameLayout mViewOver;
    private RelativeLayout mDeviceRlTips;
    private TemperaturePresenter temperaturePresenter;
    private ImageView mIvBack;
    private ImageView mIconHome;
    private String[] mWenduResults;
    private static final String TAG = "TemperatureMeasureActiv";
    private boolean isPostData = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_measure);
        initView();
        playVideo();

        temperaturePresenter = new TemperaturePresenter(this);
    }

    private void playVideo() {
        mVvTips.setVisibility(View.VISIBLE);
        mViewOver.setVisibility(View.VISIBLE);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_wendu);
        mVvTips.setZOrderOnTop(true);
        mVvTips.setZOrderMediaOverlay(true);
        mVvTips.setVideoURI(uri);
        mVvTips.setOnCompletionListener(mCompletionListener);
        mVvTips.start();
    }

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mVvTips.setVisibility(View.GONE);
            mViewOver.setVisibility(View.GONE);
        }
    };

    public void updateData(String... datas) {
        if (datas.length == 2) {
            mTvResult.setText("0.00");
        } else if (datas.length == 1) {
            mTvResult.setText(datas[0]);

            float v = Float.parseFloat(datas[0]);
            String wenduResult;
            if (v < 36) {
                wenduResult = mWenduResults[3];
            } else if (v < 38) {
                wenduResult = mWenduResults[0];
            } else if (v < 40) {
                wenduResult = mWenduResults[1];
            } else {
                wenduResult = mWenduResults[2];
            }
            MLVoiceSynthetize.startSynthesize(this, String.format(getString(R.string.tips_result_wendu), datas[0], wenduResult), false);
            if (!isPostData) {
                isPostData = true;
                //上传数据
                DataInfoBean info = new DataInfoBean();
                info.temper_ature = datas[0];
                NetworkApi.postData(info, new NetworkManager.SuccessCallback<MeasureResult>() {
                    @Override
                    public void onSuccess(MeasureResult response) {
                        Log.i(TAG, "onSuccess: ");
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        Log.i(TAG, "onFailed: " + message);
                    }
                });
            }
        }

    }

    private void initView() {
        mVideoTemperature = (ImageView) findViewById(R.id.video_temperature);
        mHistory = (Button) findViewById(R.id.history);
        mHistory.setOnClickListener(this);
        mTemperatureVideo = (Button) findViewById(R.id.temperature_video);
        mTemperatureVideo.setOnClickListener(this);
        mDetectLlNav1 = (LinearLayout) findViewById(R.id.detect_ll_nav1);
        mTextTemperature1 = (TextView) findViewById(R.id.text_temperature1);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mRelativeLayout1 = (RelativeLayout) findViewById(R.id.RelativeLayout1);
        mTest2 = (ImageView) findViewById(R.id.test_2);
        mTest2.setOnClickListener(this);
        mVideoTemperature.setOnClickListener(this);
        mDetectLlNav1.setOnClickListener(this);
        mTextTemperature1.setOnClickListener(this);
        mTvResult.setOnClickListener(this);
        mRelativeLayout1.setOnClickListener(this);
        mRlTemp = (RelativeLayout) findViewById(R.id.rl_temp);
        mVvTips = (VideoView) findViewById(R.id.vv_tips);
        mViewOver = (FrameLayout) findViewById(R.id.view_over);
        mViewOver.setOnClickListener(this);
        mDeviceRlTips = (RelativeLayout) findViewById(R.id.device_rl_tips);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        mIconHome = (ImageView) findViewById(R.id.icon_home);
        mIconHome.setOnClickListener(this);

        mWenduResults = getResources().getStringArray(R.array.result_wendu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.history:
                Intent intent = new Intent(this, HealthRecordActivity.class);
                intent.putExtra("position", 0);
                startActivity(intent);
                break;
            case R.id.temperature_video:
                playVideo();
                break;
            case R.id.view_over:
                mVvTips.setVisibility(View.GONE);
                mViewOver.setVisibility(View.GONE);
                if (mVvTips.isPlaying()) {
                    mVvTips.pause();
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.icon_home:
                new AlertDialog.Builder(this)
                        .setMessage("是否匹配新设备")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                LocalShared.getInstance(TemperatureMeasureActivity.this).setWenduMac("");
                                if (temperaturePresenter != null) {
                                    temperaturePresenter.onDestroy();
                                }
                                temperaturePresenter = new TemperaturePresenter(TemperatureMeasureActivity.this);
                            }
                        }).show();

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        temperaturePresenter = null;
    }
}
