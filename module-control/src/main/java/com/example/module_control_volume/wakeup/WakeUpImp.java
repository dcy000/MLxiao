package com.example.module_control_volume.wakeup;

import android.content.Context;

import com.gcml.common.service.IWakeUpControlProvider;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/module/control/wake/control/provider")
public class WakeUpImp implements IWakeUpControlProvider {

    @Override
    public void init(Context context) {
        WakeupHelper.init(context);
    }

    @Override
    public void enableWakeuperListening(boolean wake) {
        WakeupHelper.getInstance().enableWakeuperListening(wake);
    }
}
