package com.example.module_control_volume.volume;

import android.app.Application;

import com.gcml.common.service.IVolumeControlProvider;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/module/control/volume/control/provider")
public class VolumeControlProviderImp implements IVolumeControlProvider {
    @Override
    public void init(Application app) {
        VolumeControlFloatwindow.init(app);
    }

    @Override
    public void setVolume(int volume) {
        VolumeControlFloatwindow.setVolumSB(volume);
    }
}
