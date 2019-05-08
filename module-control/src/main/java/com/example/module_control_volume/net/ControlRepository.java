package com.example.module_control_volume.net;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.gcml.common.RetrofitHelper;
import com.gcml.common.data.UserEntity;
import com.gcml.common.recommend.bean.get.VersionInfoBean;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.UM;

import io.reactivex.Observable;

public class ControlRepository {
    private static ControlService controlService = RetrofitHelper.service(ControlService.class);

    public Observable<VersionInfoBean> getVersionInfo() {
        ApplicationInfo appInfo;
        String msg = "";
        try {
            appInfo = UM.getApp().getPackageManager().getApplicationInfo(UM.getApp().getPackageName(), PackageManager.GET_META_DATA);
            msg = appInfo.metaData.getString("com.gcml.version");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return controlService.getVersionInfo(msg).compose(RxUtils.apiResultTransformer());
    }

    public Observable<UserEntity> PersonInfo(String userId) {
        return controlService.PersonInfo(userId).compose(RxUtils.apiResultTransformer());
    }
}
