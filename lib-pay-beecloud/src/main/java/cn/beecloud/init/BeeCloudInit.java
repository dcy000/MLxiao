package cn.beecloud.init;

import android.app.Application;
import android.content.Context;

import com.gcml.common.api.AppLifecycleCallbacks;
import com.google.auto.service.AutoService;

import cn.beecloud.BeeCloud;

@AutoService(AppLifecycleCallbacks.class)
public class BeeCloudInit implements AppLifecycleCallbacks {
    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
        BeeCloud.setAppIdAndSecret("51bc86ef-06da-4bc0-b34c-e221938b10c9", "4410cd33-2dc5-48ca-ab60-fb7dd5015f8d");//自己
//        BeeCloud.setAppIdAndSecret("91ee2a0a-661d-4d81-8979-547124be340d", "b8b53d06-5571-404a-bda2-a1d0b8bca0e8");
    }

    @Override
    public void onTerminate(Application app) {

    }
}
