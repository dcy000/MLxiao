package com.example.han.referralproject.measure;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.DataInfoBean;
import com.example.han.referralproject.bean.MeasureResult;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.WeakHandler;
import com.medlink.danbogh.healthdetection.HealthRecordActivity;

public class BloodsugarMeasureActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mVideoXuetang1;
    private ImageView mVideoXuetang2;
    /**
     * 历史记录
     */
    private Button mHistory2;
    /**
     * 使用演示
     */
    private Button mXuetangVideo;
    private LinearLayout mDetectLlNav3;
    /**
     * 血糖
     */
    private TextView mTestXuetang;
    /**
     * 0.0
     */
    private TextView mTvXuetang;
    /**
     * 空腹
     */
    private TextView mTvEmpty;
    /**
     * 饭后\n一小时
     */
    private TextView mTvAnHour;
    /**
     * 饭后\n两小时
     */
    private TextView mTvTwoHour;
    /**
     * 饭后\n三小时
     */
    private TextView mTvThreeHour;
    //    private LinearLayout mLlSelectTime;
    private RelativeLayout mRlXuetang;
    private VideoView mVvTips;
    private FrameLayout mViewOver;
    private RelativeLayout mDeviceRlTips;
    private ImageView mIvBack;
    private ImageView mIconHome;
    private BloodsugarPresenter bloodsugarPresenter;
    private int timeFlag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloodsugar_measure);
        initView();
        playVideo();

        timeFlag = getIntent().getIntExtra("time", 0);

        bloodsugarPresenter = new BloodsugarPresenter(this);

    }

    private void playVideo() {
        mVvTips.setVisibility(View.VISIBLE);
        mViewOver.setVisibility(View.VISIBLE);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xuetang);
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

    private void initView() {
        mVideoXuetang1 = (ImageView) findViewById(R.id.video_xuetang1);
        mVideoXuetang2 = (ImageView) findViewById(R.id.video_xuetang2);
        mHistory2 = (Button) findViewById(R.id.history2);
        mHistory2.setOnClickListener(this);
        mXuetangVideo = (Button) findViewById(R.id.xuetang_video);
        mXuetangVideo.setOnClickListener(this);
        mDetectLlNav3 = (LinearLayout) findViewById(R.id.detect_ll_nav3);
        mTestXuetang = (TextView) findViewById(R.id.test_xuetang);
        mTvXuetang = (TextView) findViewById(R.id.tv_xuetang);
        mTvEmpty = (TextView) findViewById(R.id.tv_empty);
        mTvEmpty.setOnClickListener(this);
        mTvAnHour = (TextView) findViewById(R.id.tv_an_hour);
        mTvAnHour.setOnClickListener(this);
        mTvTwoHour = (TextView) findViewById(R.id.tv_two_hour);
        mTvTwoHour.setOnClickListener(this);
        mTvThreeHour = (TextView) findViewById(R.id.tv_three_hour);
        mTvThreeHour.setOnClickListener(this);
        mRlXuetang = (RelativeLayout) findViewById(R.id.rl_xuetang);
        mVvTips = (VideoView) findViewById(R.id.vv_tips);
        mViewOver = (FrameLayout) findViewById(R.id.view_over);
        mViewOver.setOnClickListener(this);
        mDeviceRlTips = (RelativeLayout) findViewById(R.id.device_rl_tips);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        mIconHome = (ImageView) findViewById(R.id.icon_home);
        mIconHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.history2:
                Intent intent = new Intent(this, HealthRecordActivity.class);
                intent.putExtra("position", 2);
                startActivity(intent);
                break;
            case R.id.xuetang_video:
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
                                LocalShared.getInstance(BloodsugarMeasureActivity.this).setXuetangMac("");
                                if (bloodsugarPresenter != null) {
                                    bloodsugarPresenter.onDestroy();
                                    new WeakHandler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            bloodsugarPresenter=new BloodsugarPresenter(BloodsugarMeasureActivity.this);
                                        }
                                    }, 1000);
                                }
                            }
                        }).show();
                break;
        }
    }

    public void updateData(String... datas) {
        if (datas.length == 2) {
            mTvXuetang.setText("0.00");
        } else if (datas.length == 1) {
            mTvXuetang.setText(datas[0]);
        }

        final DataInfoBean info = new DataInfoBean();
        info.blood_sugar = datas[0];
        info.sugar_time = timeFlag + "";
        info.upload_state = true;

        NetworkApi.postData(info, new NetworkManager.SuccessCallback<MeasureResult>() {
            @Override
            public void onSuccess(MeasureResult response) {
                startActivity(new Intent(BloodsugarMeasureActivity.this, MeasureXuetangResultActivity.class)
                        .putExtra("measure_piangao_num", response.high)
                        .putExtra("measure_zhengchang_num", response.regular)
                        .putExtra("measure_piandi_num", response.low)
                        .putExtra("measure_sum", response.zonggong)
                        .putExtra("result", info.blood_sugar + "")
                        .putExtra("suggest", response.message)
                        .putExtra("week_avg_one", response.oneHour_stomach)
                        .putExtra("week_avg_two", response.twoHour_stomach)
                        .putExtra("week_avg_empty", response.empty_stomach)
                        .putExtra("fenshu", response.exponent));
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bloodsugarPresenter = null;
    }
}
