package com.gcml.common.service;

import android.app.Application;

public interface IVolumeControlProvider {
    void init(Application app);
    void setVolume(int volume);
}
