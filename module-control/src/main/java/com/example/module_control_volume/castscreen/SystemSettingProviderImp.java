package com.example.module_control_volume.castscreen;

import android.app.Activity;
import android.provider.Settings;

import com.gcml.common.service.ISystemSettingProvider;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/module/control/system/setting/provider")
public class SystemSettingProviderImp implements ISystemSettingProvider {

    @Override
    public void skipSettingDisplay(Activity activity) {
        WindowManagerSp windowManagerSp = new WindowManagerSp(activity);
        windowManagerSp.addBackButton();
        IntentSp.startActivity(activity, Settings.ACTION_DISPLAY_SETTINGS, true);
    }
}
