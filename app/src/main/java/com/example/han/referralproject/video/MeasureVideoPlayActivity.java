package com.example.han.referralproject.video;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.han.referralproject.R;
import com.gcml.lib_video_ksyplayer.DataInter;
import com.gcml.lib_video_ksyplayer.default_cover.ControllerCover;
import com.gcml.lib_video_ksyplayer.default_cover.ErrorCover;
import com.gcml.lib_video_ksyplayer.default_cover.IJump2NextListener;
import com.gcml.lib_video_ksyplayer.util.PUtil;
import com.kk.taurus.playerbase.assist.OnVideoViewEventHandler;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.event.OnPlayerEventListener;
import com.kk.taurus.playerbase.receiver.IReceiver;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;
import com.kk.taurus.playerbase.widget.BaseVideoView;

import java.io.Serializable;

import static com.gcml.lib_video_ksyplayer.DataInter.ReceiverKey.KEY_CONTROLLER_COVER;
import static com.gcml.lib_video_ksyplayer.DataInter.ReceiverKey.KEY_ERROR_COVER;

public class MeasureVideoPlayActivity extends AppCompatActivity implements OnPlayerEventListener, IJump2NextListener {
    public static final int REQUEST_PALY_VIDEO = 1001;
    private BaseVideoView mVideoView;
    private ReceiverGroup mReceiverGroup;
    private boolean isLandscape;
    private long mDataSourceId;
    private boolean userPause;

    //播放本地资源的时候传resId,url传null;比方网络资源的时候resId传null
    public static void startActivity(Activity context, Class clazz, Uri uri, String url, String title) {
        context.startActivityForResult(new Intent(context, clazz)
                .putExtra("uri", uri)
                .putExtra("url", url)
                .putExtra("title", title), REQUEST_PALY_VIDEO);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_general_video_view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mVideoView = findViewById(R.id.videoView);
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra("uri");
        String url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");
        DataSource dataSource=new DataSource();
        dataSource.setUri(uri);
        dataSource.setData(url);
        dataSource.setTitle(title);
        initPlay(dataSource);


    }

    private void initPlay(DataSource dataSource) {
        updateVideo(true);
        mVideoView.setOnPlayerEventListener(this);
        mVideoView.setEventHandler(mOnEventAssistHandler);
        mReceiverGroup = ReceiverGroupManager.get().getMeasureVideoReceiverGroup(this,null);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_NETWORK_RESOURCE, false);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, true);
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_IS_HAS_NEXT, false);
        mVideoView.setReceiverGroup(mReceiverGroup);
        mVideoView.setDataSource(dataSource);
        mVideoView.start();

        ControllerCover controllerCover = mReceiverGroup.getReceiver(KEY_CONTROLLER_COVER);
        ErrorCover errorCover = mReceiverGroup.getReceiver(KEY_ERROR_COVER);
        controllerCover.setOnJump2NextListener(this);
        errorCover.setOnJump2NextListener(this);

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
                case DataInter.Event.EVENT_CODE_REQUEST_CLOSE:
                    setResult(RESULT_OK);
                    break;
            }
        }
    };

    @Override
    public void onPlayerEvent(int eventCode, Bundle bundle) {
        switch (eventCode) {
            case OnPlayerEventListener.PLAYER_EVENT_ON_VIDEO_RENDER_START:
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_PLAY_COMPLETE:
                setResult(RESULT_OK);
                break;
            case OnPlayerEventListener.PLAYER_EVENT_ON_RESUME:
                userPause = false;
                break;
        }
    }

    @Override
    public void clickJump2Next(View view) {
        finish();
    }
}
