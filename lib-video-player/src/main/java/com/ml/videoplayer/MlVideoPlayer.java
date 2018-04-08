package com.ml.videoplayer;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by lenovo on 2018/3/23.
 */

public class MlVideoPlayer {
    public static void play(Context context, String url, String title) {
        try {
            Class<?> aClass = Class.forName("com.example.han.referralproject.video.PlayVideoActivity");
            Intent intent = new Intent(context, aClass);
            intent.putExtra("url", url);
            context.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
//        JZVideoPlayerStandard.startFullscreen(context, Player.class, url, title);
//        JZVideoPlayer.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//        JZVideoPlayer.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

    public static boolean onBackPressed() {
        return JZVideoPlayerStandard.backPress();
    }

    public static void release() {
        JZVideoPlayer.releaseAllVideos();
    }

    public static class Player extends JZVideoPlayerStandard {

        public Player(Context context) {
            super(context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.ml_player;
        }

        @Override
        public void init(Context context) {
            super.init(context);
            View tvAction = findViewById(R.id.common_tv_action);
            tvAction.setOnClickListener(this);
            tvAction.setVisibility(GONE);
        }

        @Override
        public void onStateAutoComplete() {
            super.onStateAutoComplete();
            backPress();
        }
    }
}
