package com.gcml.health.measure;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.nfc.tech.NfcA;
import android.support.multidex.MultiDex;

import com.billy.cc.core.component.CC;
import com.gcml.common.AppDelegate;

import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.ui.UiUtils;
import com.gcml.lib_video_ksyplayer.KSYPlayer;
import com.gcml.module_blutooth_devices.base.BluetoothClientManager;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.config.PlayerLibrary;
import com.kk.taurus.playerbase.entity.DecoderPlan;


/**
 * Created by gzq on 2018/8/19.
 */

public class MyApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);

        AppDelegate.INSTANCE.attachBaseContext(this, base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppDelegate.INSTANCE.onCreate(this);
        //初始化工具类
        UtilsManager.init(this);
        //语音模块初始化
        StringBuilder builder = new StringBuilder();
        builder.append("appid=")
                .append("59196d96")
                .append(",")
                .append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);

        SpeechUtility.createUtility(this, builder.toString());
        BluetoothClientManager.init(this);
        initVideoPlay();
        UiUtils.init(this, 1920, 1200);
        //支持多进程
        CC.enableRemoteCC(true);
        //测试使用的ID:15181438908
        UserSpHelper.setUserId("100130");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        UiUtils.compatWithOrientation(newConfig);
    }

    public static final int PLAN_ID_KSY = 1;

    private void initVideoPlay() {
        PlayerConfig.addDecoderPlan(new DecoderPlan(PLAN_ID_KSY, KSYPlayer.class.getName(), "Ksyplayer"));
        PlayerConfig.setDefaultPlanId(PLAN_ID_KSY);
        PlayerConfig.setUseDefaultNetworkEventProducer(true);
        PlayerLibrary.init(this);
    }
}
