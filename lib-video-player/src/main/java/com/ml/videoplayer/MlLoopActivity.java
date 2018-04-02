package com.ml.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class MlLoopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ml_activity_loop);
        JZVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        JZVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        if (mPaths == null || mPaths.size() == 0) {
            return;
        }
        JZVideoPlayerStandard.startFullscreen(
                this,
                MyPlayerView.class,
                mDirPath + "/" + mPaths.get(0));
    }

    private static List<String> mPaths;
    private static String mDirPath;

    static {
        mDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/lude";
        File file = new File(mDirPath);
        mPaths = Arrays.asList(file.list());
    }

    private static int position; //4,6,7

    public static class MyPlayerView extends JZVideoPlayerStandard {
        public MyPlayerView(Context context) {
            super(context);
        }

        public MyPlayerView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public int getLayoutId() {
            return R.layout.ml_player;
        }

        @Override
        public void init(Context context) {
            super.init(context);
            findViewById(R.id.common_tv_action).setOnClickListener(this);
            fullscreenButton.setVisibility(GONE);
        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            int i = v.getId();
            backButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = (Activity) getContext();
                    activity.finish();
                    MyPlayerView.this.onClick(v);
                }
            });
            if (i == R.id.common_tv_action) {
                backButton.performClick();
            }
        }

        @Override
        public void onAutoCompletion() {
            super.onAutoCompletion();
            if (mPaths == null || mPaths.size() == 0) {
                return;
            }
            position++;
            position %= mPaths.size();
            Log.i(TAG, "onAutoCompletion: " + position);
            JZVideoPlayerStandard.startFullscreen(
                    getContext(),
                    MyPlayerView.class,
                    mDirPath + "/" + mPaths.get(position));
        }
    }
}
