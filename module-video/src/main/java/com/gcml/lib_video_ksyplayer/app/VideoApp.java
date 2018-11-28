package com.gcml.lib_video_ksyplayer.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;

import com.gcml.lib_video_ksyplayer.KSYPlayer;
import com.gcml.lib_video_ksyplayer.events.VideoEvents;
import com.gzq.lib_core.base.delegate.AppLifecycle;
import com.gzq.lib_core.base.ui.IEvents;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.config.PlayerLibrary;
import com.kk.taurus.playerbase.entity.DecoderPlan;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/25 15:03
 * created by:gzq
 * description:初始化视频库
 */
public class VideoApp implements AppLifecycle {
    /**
     * 内核金山云
     */
    public static final int PLAN_ID_KSY = 1;

    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(Application app) {
        initVideoPlay(app);
    }

    @Override
    public void onTerminate(Application app) {

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

    }

    @Override
    public IEvents provideEvents() {
        return new VideoEvents();
    }

    private void initVideoPlay(Application app) {
        PlayerConfig.addDecoderPlan(new DecoderPlan(PLAN_ID_KSY, KSYPlayer.class.getName(), "Ksyplayer"));
        PlayerConfig.setDefaultPlanId(PLAN_ID_KSY);
        PlayerConfig.setUseDefaultNetworkEventProducer(true);
        PlayerLibrary.init(app);
    }
}
