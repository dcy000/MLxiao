package com.example.module_control_volume.update;

import android.content.Context;
import android.os.SystemClock;
import android.widget.Chronometer;

import com.example.module_control_volume.net.ControlRepository;
import com.gcml.common.recommend.bean.get.VersionInfoBean;
import com.gcml.common.router.AppRouter;
import com.gcml.common.service.IAppUpdateProvider;
import com.gcml.common.utils.AppUtils;
import com.gcml.common.utils.UM;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@Route(path = "/module/control/app/update/provider")
public class AppUpdateProviderImp implements IAppUpdateProvider {
    @Override
    public void checkAppVersion(Context context) {
        new ControlRepository()
                .getVersionInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<VersionInfoBean>() {
                    @Override
                    public void onNext(VersionInfoBean versionInfoBean) {
                        if (versionInfoBean != null && versionInfoBean.vid > AppUtils.getAppInfo().getVersionCode()) {
                            showDialog(context, versionInfoBean.url);
                        } else {
                            Timber.i("版本检查更新-->已经是最新版本");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i("版本检查更新error-->已经是最新版本");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void showDialog(Context context, String appUrl) {
        new UpdateAppManager(context).showNoticeDialog(appUrl);
    }
}
