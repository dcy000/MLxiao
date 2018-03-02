package com.example.han.referralproject.measure;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 在该页面需要做两件事：1.播放指导动画；2尝试蓝牙的连接
 */
public class InstructionsActivity extends AppCompatActivity {
    @BindView(R.id.video)
    VideoView video;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.icon_home)
    ImageView iconHome;
    @BindView(R.id.jump)
    TextView jump;
    private String type;
    private int resourseId;
    private int MY_PERMISSIONS_REQUEST_LOCATION = 0;
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏应用程序的标题栏，即当前activity的label
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏android系统的状态栏
        setContentView(R.layout.activity_instructions);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra("type");
        playVideo();
        setClick();
    }

    private void setClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video.isPlaying()) {
                    video.stopPlayback();
                }
                finish();
            }
        });
        iconHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video.isPlaying()) {
                    video.stopPlayback();
                }
                startActivity(new Intent(InstructionsActivity.this, MainActivity.class));
                finish();
            }
        });

        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video.isPlaying()) {
                    video.stopPlayback();
                }
                startActivity(new Intent(InstructionsActivity.this, OnMeasureActivity.class).putExtra("type", type));
                finish();
            }
        });
    }

    private void playVideo() {
        switch (type) {
            case "wendu":
                resourseId = R.raw.tips_wendu;
                break;
            case "xueya":
                resourseId = R.raw.tips_xueya;
                break;
            case "xuetang":
                resourseId = R.raw.tips_xuetang;
                break;
            case "xueyang":
                resourseId = R.raw.tips_xueyang;
                break;
            case "xindian":
                resourseId = R.raw.tips_xindian;
                break;
            case "tizhong":
                resourseId = 0;
                break;
            case "sanheyi":
                resourseId = R.raw.tips_sanheyi;
                break;
        }
        if (resourseId != 0) {
            String uri = "android.resource://" + getPackageName() + "/" + resourseId;
            video.setVideoURI(Uri.parse(uri));
            video.start();
            video.setOnCompletionListener(videoComplete);
        }
    }

    /**
     * 视频播放结束
     */
    private MediaPlayer.OnCompletionListener videoComplete = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            startActivity(new Intent(InstructionsActivity.this, OnMeasureActivity.class).putExtra("type", type));
            finish();
        }
    };
}
