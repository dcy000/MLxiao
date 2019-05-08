package com.example.module_control_volume.update;

import android.app.Application;
import android.content.Context;
import android.os.SystemClock;
import android.widget.Chronometer;

import com.example.module_control_volume.net.ControlRepository;
import com.gcml.common.recommend.bean.get.VersionInfoBean;
import com.gcml.common.router.AppRouter;
import com.gcml.common.service.IAppUpdateProvider;
import com.gcml.common.utils.AppUtils;
import com.gcml.common.utils.UM;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@Route(path = "/module/control/app/update/provider")
public class AppUpdateProviderImp implements IAppUpdateProvider {
    @Override
    public void checkAppVersion(Context context, boolean isNeed) {
        new ControlRepository()
                .getVersionInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (isNeed) {
                            showLoading(context, "检查更新中");
                        }
                    }
                })
                .subscribe(new DefaultObserver<VersionInfoBean>() {
                    @Override
                    public void onNext(VersionInfoBean versionInfoBean) {
                        if (versionInfoBean != null && versionInfoBean.vid > AppUtils.getAppInfo().getVersionCode()) {
                            showDialog(context, versionInfoBean.url);
                        } else {
                            Timber.i("版本检查更新-->已经是最新版本");
                            if (isNeed) {
                                MLVoiceSynthetize.startSynthesize(UM.getApp(), "已经是最新版本了");
                                ToastUtils.showShort("已经是最新版本了");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.i("版本检查更新error-->已经是最新版本");
                        if (isNeed) {
                            dismissLoading();
                            MLVoiceSynthetize.startSynthesize(UM.getApp(), "已经是最新版本了");
                            ToastUtils.showShort("已经是最新版本了");
                        }
                    }

                    @Override
                    public void onComplete() {
                        dismissLoading();
                    }
                });
    }

    @Override
    public void showDialog(Context context, String appUrl) {
        new UpdateAppManager(context).showNoticeDialog(appUrl);
    }

    private LoadingDialog mLoadingDialog;

    protected void showLoading(Context context, String tips) {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
        mLoadingDialog = new LoadingDialog.Builder(context)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(tips)
                .create();
        mLoadingDialog.show();
    }

    protected void dismissLoading() {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
    }
}
