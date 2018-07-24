package com.gzq.test_all_devices;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gcml.lib_video_ksyplayer.DataInter;
import com.gcml.lib_video_ksyplayer.default_cover.ControllerCover;
import com.gcml.lib_video_ksyplayer.util.PUtil;
import com.kk.taurus.playerbase.assist.OnVideoViewEventHandler;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.receiver.IReceiver;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;
import com.kk.taurus.playerbase.render.AspectRatio;
import com.kk.taurus.playerbase.render.IRender;
import com.kk.taurus.playerbase.widget.BaseVideoView;

public class TestVideoActivity extends AppCompatActivity implements OnPlayerEventListener {
    private BaseVideoView mVideoView;
    private ReceiverGroup mReceiverGroup;
    private boolean isLandscape;
    private long mDataSourceId;
    private boolean userPause;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mVideoView = findViewById(R.id.videoView);
        initPlay();


    }

    private void initPlay() {
        updateVideo(true);
        mVideoView.setOnPlayerEventListener(this);
        mVideoView.setEventHandler(mOnEventAssistHandler);
        mReceiverGroup = ReceiverGroupManager.get().getReceiverGroup(this, null);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_NETWORK_RESOURCE, true);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, true);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_IS_HAS_NEXT, false);
        mVideoView.setReceiverGroup(mReceiverGroup);
        DataSource dataSource = new DataSource();
//        Uri parse = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.tips_xuetang);
//        dataSource.setUri(parse);
//        dataSource.setUri(parse);
        dataSource.setData("http://oyptcv2pb.bkt.clouddn.com/abc_1521797390144");
        dataSource.setTitle("测试");
        mVideoView.setDataSource(dataSource);
        mVideoView.start();
    }

    private DataSource generatorDataSource(long id) {
        DataSource dataSource = new DataSource();
        dataSource.setId(id);
        return dataSource;
    }

    /**
     * 设置视频容器为SurfaceView
     *
     * @param view
     */
    public void setRenderSurfaceView(View view) {
        mVideoView.setRenderType(IRender.RENDER_TYPE_SURFACE_VIEW);
    }

    /**
     * 设置视频容器为TextureView
     *
     * @param view
     */
    public void setRenderTextureView(View view) {
        mVideoView.setRenderType(IRender.RENDER_TYPE_TEXTURE_VIEW);
    }

    /**
     * 设置视频有圆角
     *
     * @param view
     */
    public void onStyleSetRoundRect(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mVideoView.setRoundRectShape(PUtil.dip2px(this, 25));
        } else {
            Toast.makeText(this, "not support", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置视频为圆
     *
     * @param view
     */
    public void onStyleSetOvalRect(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mVideoView.setOvalRectShape();
        } else {
            Toast.makeText(this, "not support", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 恢复视频播放界面
     *
     * @param view
     */
    public void onShapeStyleReset(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mVideoView.clearShapeStyle();
        } else {
            Toast.makeText(this, "not support", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 视频播放比例16:9
     *
     * @param view
     */
    public void onAspect16_9(View view) {
        mVideoView.setAspectRatio(AspectRatio.AspectRatio_16_9);
    }

    /**
     * 视频播放比例4:3
     *
     * @param view
     */
    public void onAspect4_3(View view) {
        mVideoView.setAspectRatio(AspectRatio.AspectRatio_4_3);
    }

    /**
     * 设置视频填充满
     *
     * @param view
     */
    public void onAspectFill(View view) {
        mVideoView.setAspectRatio(AspectRatio.AspectRatio_FILL_PARENT);
    }

    /**
     * 设置视频填充满父容器
     *
     * @param view
     */
    public void onAspectFit(View view) {
        mVideoView.setAspectRatio(AspectRatio.AspectRatio_FIT_PARENT);
    }

    public void onAspectOrigin(View view) {
        mVideoView.setAspectRatio(AspectRatio.AspectRatio_ORIGIN);
    }

    public void onDecoderChangeMediaPlayer(View view) {
        int curr = mVideoView.getCurrentPosition();
        if (mVideoView.switchDecoder(PlayerConfig.DEFAULT_PLAN_ID)) {
            replay(curr);
        }
    }

    public void onDecoderChangeIjkPlayer(View view) {
        int curr = mVideoView.getCurrentPosition();
//        if (mVideoView.switchDecoder(MyApplication.PLAN_ID_IJK)) {
//            replay(curr);
//        }
    }

    public void onDecoderChangeExoPlayer(View view) {
        int curr = mVideoView.getCurrentPosition();
//        if(mVideoView.switchDecoder(App.PLAN_ID_EXO)){
//            replay(curr);
//        }
    }

    /**
     * 移除控制视图
     *
     * @param view
     */
    public void removeControllerCover(View view) {
        mReceiverGroup.removeReceiver(DataInter.ReceiverKey.KEY_CONTROLLER_COVER);
        Toast.makeText(this, "已移除", Toast.LENGTH_SHORT).show();
    }

    /**
     * 添加控制视图
     *
     * @param view
     */
    public void addControllerCover(View view) {
        IReceiver receiver = mReceiverGroup.getReceiver(DataInter.ReceiverKey.KEY_CONTROLLER_COVER);
        if (receiver == null) {
            mReceiverGroup.addReceiver(DataInter.ReceiverKey.KEY_CONTROLLER_COVER, new ControllerCover(this));
            Toast.makeText(this, "已添加", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 播放倍率
     *
     * @param view
     */
    public void halfSpeedPlay(View view) {
        mVideoView.setSpeed(0.5f);
    }

    /**
     * 播放倍率
     *
     * @param view
     */
    public void doubleSpeedPlay(View view) {
        mVideoView.setSpeed(2f);
    }

    /**
     * 播放倍率
     *
     * @param view
     */
    public void normalSpeedPlay(View view) {
        mVideoView.setSpeed(1f);
    }

    private void updateVideo(boolean landscape) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mVideoView.getLayoutParams();
        if (landscape) {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.setMargins(0, 0, 0, 0);
        } else {
            layoutParams.width = PUtil.getScreenW(this);
            layoutParams.height = layoutParams.width * 9 / 16;
//            layoutParams.setMargins(margin, margin, margin, margin);
        }
        mVideoView.setLayoutParams(layoutParams);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView.isInPlaybackState()) {
            mVideoView.pause();
        } else {
            mVideoView.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView.isInPlaybackState()) {
            if (!userPause)
                mVideoView.resume();
        } else {
            mVideoView.rePlay(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }

    @Override
    public void onBackPressed() {
        if (isLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isLandscape = true;
            updateVideo(true);
        } else {
            isLandscape = false;
            updateVideo(false);
        }
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_IS_LANDSCAPE, isLandscape);
    }

    private OnVideoViewEventHandler mOnEventAssistHandler = new OnVideoViewEventHandler() {
        @Override
        public void onAssistHandle(BaseVideoView assist, int eventCode, Bundle bundle) {
            super.onAssistHandle(assist, eventCode, bundle);
            switch (eventCode) {
                case DataInter.Event.CODE_REQUEST_PAUSE:
                    userPause = true;
                    break;
                case DataInter.Event.EVENT_CODE_REQUEST_BACK:
                    if (isLandscape) {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    } else {
                        finish();
                    }
                    break;
                case DataInter.Event.EVENT_CODE_REQUEST_NEXT:
                    mDataSourceId++;
                    mVideoView.setDataSource(generatorDataSource(mDataSourceId));
                    mVideoView.start();
                    break;
                case DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN:
                    setRequestedOrientation(isLandscape ?
                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT :
                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    break;
                case DataInter.Event.EVENT_CODE_ERROR_SHOW:
                    mVideoView.stop();
                    break;
            }
        }
    };

    private void replay(int msc) {
        mVideoView.setDataSource(generatorDataSource(mDataSourceId));
        mVideoView.start(msc);
    }

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode) {
            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START:
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE:

                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_RESUME:
                userPause = false;
                break;
        }
    }
}
