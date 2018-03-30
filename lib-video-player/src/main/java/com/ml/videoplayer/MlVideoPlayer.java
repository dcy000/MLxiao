package com.ml.videoplayer;

import android.content.Context;
import android.view.View;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by lenovo on 2018/3/23.
 */

public class MlVideoPlayer {
    public static void play(Context context, String url, String title) {

        JZVideoPlayerStandard.startFullscreen(context, Player.class, url, title);
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
