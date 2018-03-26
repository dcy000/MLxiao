package com.ml.videoplayer;

import android.content.Context;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by lenovo on 2018/3/23.
 */

public class MlVideoPlayer {
    public static void play(Context context, String url, String title) {

        JZVideoPlayerStandard.startFullscreen(context, JZVideoPlayerStandard.class, url, title);
    }

    public static boolean onBackPressed() {
        return JZVideoPlayerStandard.backPress();
    }

    public static void release() {
        JZVideoPlayer.releaseAllVideos();
    }
}
