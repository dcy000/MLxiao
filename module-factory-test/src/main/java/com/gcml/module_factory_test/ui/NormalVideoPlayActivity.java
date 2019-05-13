package com.gcml.module_factory_test.ui;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gcml.lib_video_ksyplayer.DataInter;
import com.gcml.lib_video_ksyplayer.default_cover.ControllerCover;
import com.gcml.lib_video_ksyplayer.default_cover.LoadingCover;
import com.gcml.module_factory_test.R;
import com.gcml.module_factory_test.utils.BatteryHelper;
import com.gcml.module_factory_test.utils.PUtil;
import com.kk.taurus.playerbase.assist.OnVideoViewEventHandler;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.receiver.GroupValue;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;
import com.kk.taurus.playerbase.widget.BaseVideoView;

import static com.gcml.lib_video_ksyplayer.DataInter.ReceiverKey.KEY_CONTROLLER_COVER;
import static com.gcml.lib_video_ksyplayer.DataInter.ReceiverKey.KEY_LOADING_COVER;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/25 17:04
 * created by:gzq
 * description:TODO
 */
public class NormalVideoPlayActivity extends AppCompatActivity implements BatteryHelper.OnBatteryChangeListener, BatteryHelper.OnPowerConnectionChangeListener {
    private BaseVideoView mVideoView;
    private ReceiverGroup mReceiverGroup;
    private boolean isLandscape;
    private long mDataSourceId;
    private boolean userPause;
    private PlayerEventListener playerEventListener;
    private TextView battery;
    private Uri uri;
    private String url;
    private String title;
    private DataSource dataSource;
    private TextView time;

    //播放本地资源的时候传resId,url传null;比方网络资源的时候resId传null
    public static void startActivity(Context context, Uri uri, String url, String title) {
        Intent intent = new Intent(context, NormalVideoPlayActivity.class);
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra("uri", uri);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.factory_video_activity_general_video_view);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mVideoView = findViewById(R.id.videoView);
        battery = findViewById(R.id.batteryAAA);
        Intent intent = getIntent();
        uri = intent.getParcelableExtra("uri");
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        time = findViewById(R.id.tv_time);
        initPlay();
    }

    int recordTotalTime = 0;
    private Handler mainHandler = new Handler();

    private void startTimer() {
        mainHandler.removeCallbacksAndMessages(null);
        updateTimerUI(recordTotalTime);
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recordTotalTime += 1;
                updateTimerUI(recordTotalTime);
                mainHandler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void updateTimerUI(int recordTotalTime) {
        String string = String.format("%s", formatTime(recordTotalTime));
        time.setText("累计播放时间" + string);
    }

    private void endTimer() {
        mainHandler.removeCallbacksAndMessages(null);
    }


    public String formatTime(int recTime) {
        int hour = (recTime / 60 / 60) % 60;
        int minute = (recTime / 60) % 60;
        int second = recTime % 60;
        return String.format(" %02d:%02d:%02d ", hour, minute, second);
    }


    private void initPlay() {
        if (dataSource == null) {
            dataSource = new DataSource();
            dataSource.setUri(uri);
            dataSource.setData(url);
            dataSource.setTitle(title);
        }
        updateVideo(true);
        playerEventListener = new PlayerEventListener();
        mVideoView.setOnPlayerEventListener(playerEventListener);
        mVideoView.setEventHandler(mOnEventAssistHandler);
        mReceiverGroup = getNormalVideoReceiverGroup(this.getApplicationContext(), null);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_NETWORK_RESOURCE, true);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, true);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_IS_HAS_NEXT, false);
        mVideoView.setReceiverGroup(mReceiverGroup);
        mVideoView.setDataSource(dataSource);
        mVideoView.start();
    }
    /**
     * 普通视频播放
     *
     * @param context
     * @param groupValue
     * @return
     */
    private ReceiverGroup getNormalVideoReceiverGroup(Context context, GroupValue groupValue) {
        ReceiverGroup receiverGroup = new ReceiverGroup(groupValue);
        receiverGroup.addReceiver(KEY_LOADING_COVER, new LoadingCover(context));
        receiverGroup.addReceiver(KEY_CONTROLLER_COVER, new ControllerCover(context,false));
//        receiverGroup.addReceiver(KEY_GESTURE_COVER, new GestureCover(context));
//        receiverGroup.addReceiver(KEY_COMPLETE_COVER, new CompleteCover(context));
//        receiverGroup.addReceiver(KEY_ERROR_COVER, new ErrorCover(context,false));
        return receiverGroup;
    }
    class PlayerEventListener implements OnPlayerEventListener {

        @Override
        public void onPlayerEvent(int eventCode, Bundle bundle) {
            switch (eventCode) {
                case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START:
                    break;
                case OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE:
                    initPlay();
                    break;
                case OnPlayerEventListener.PLAYER_EVENT_ON_RESUME:
                    startTimer();
                    userPause = false;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startTimer();
        initBattery(this);
    }

    private BatteryHelper mBatteryHelper;

    private void initBattery(Context context) {
        BatteryHelper.init(context);
        if (mBatteryHelper == null) {
            mBatteryHelper = new BatteryHelper();
        }

        if (mBatteryHelper != null) {
            mBatteryHelper.setOnBatteryChangeListener(this);
            mBatteryHelper.setOnPowerConnectionChangeListener(this);
            mBatteryHelper.start();
        }
    }

    private DataSource generatorDataSource(long id) {
        DataSource dataSource = new DataSource();
        dataSource.setId(id);
        return dataSource;
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
            if (!userPause) {
                mVideoView.resume();
            }
        } else {
            mVideoView.rePlay(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
        playerEventListener = null;
        mainHandler.removeCallbacksAndMessages(null);
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
                    endTimer();
                    break;
                case DataInter.Event.EVENT_CODE_REQUEST_BACK:
                    finish();
//                    //如果是横屏就先恢复成竖屏，如果已经是竖屏了，则直接关闭当前页面
//                    if (isLandscape) {
//                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    } else {
//                        finish();
//                    }
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
                case DataInter.Event.EVENT_CODE_REQUEST_CLOSE:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onBatteryChanged(int percent) {
        if (battery != null) {
            battery.setText("电量：" + percent + "%");
        }
    }

    @Override
    public void onBatteryStatusChanged(int status) {

    }

    @Override
    public void onPowerConnectionChanged(boolean connected) {
    }
}
