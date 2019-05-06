package com.example.module_control_volume.update;

import android.content.Context;

import com.gcml.common.service.IAppUpdateProvider;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/module/control/app/update/provider")
public class AppUpdateProviderImp implements IAppUpdateProvider {
    @Override
    public void showDialog(Context context, String appUrl) {
        new UpdateAppManager(context).showNoticeDialog(appUrl);
    }
}
