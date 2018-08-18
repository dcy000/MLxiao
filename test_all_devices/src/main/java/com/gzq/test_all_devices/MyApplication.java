package com.gzq.test_all_devices;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;

import com.gcml.lib_utils.UtilsManager;
import com.gcml.lib_utils.ui.UiUtils;
import com.gcml.lib_video_ksyplayer.KSYPlayer;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.config.PlayerLibrary;
import com.kk.taurus.playerbase.entity.DecoderPlan;

public class MyApplication extends Application {
    public static final int PLAN_ID_KSY = 1;
    private static MyApplication instance;
    public String userId = "100067";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化px转pt工具
        UiUtils.init(this, 1920, 1200);
        BluetoothClientManager.init(this);
        UtilsManager.init(this);

        PlayerConfig.addDecoderPlan(new DecoderPlan(PLAN_ID_KSY, KSYPlayer.class.getName(), "Ksyplayer"));
        PlayerConfig.setDefaultPlanId(PLAN_ID_KSY);
        PlayerConfig.setUseDefaultNetworkEventProducer(true);
        PlayerLibrary.init(this);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        UiUtils.compatWithOrientation(newConfig);
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
