package com.example.han.referralproject.new_music;

import android.app.Application;
import android.content.Context;

import com.gcml.common.api.AppLifecycleCallbacks;
import com.google.auto.service.AutoService;

@AutoService(AppLifecycleCallbacks.class)
public class MusicPlayerInit implements AppLifecycleCallbacks {
    @Override
    public void attachBaseContext(Application app, Context base) {

    }

    @Override
    public void onCreate(Application app) {
        LibMusicPlayer.init(app);
        Preferences.init(app);
    }

    @Override
    public void onTerminate(Application app) {

    }
}
