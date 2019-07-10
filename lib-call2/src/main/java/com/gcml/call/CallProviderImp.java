package com.gcml.call;

import android.Manifest;
import android.app.Activity;
import android.text.TextUtils;

import com.gcml.common.service.ICallProvider;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.PermissionUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.sjtu.yifei.annotation.Route;
import com.tbruyelle.rxpermissions2.Permission;

@Route(path = "/call/call/provider")
public class CallProviderImp implements ICallProvider {

    @Override
    public void login(String callId, String callPwd) {
        CallAccountHelper.INSTANCE.login(callId, callPwd, null);
    }

    @Override
    public void logout() {
        CallAccountHelper.INSTANCE.logout();
    }

    @Override
    public void call(Activity activity, String callId) {
        if (activity == null) {
            return;
        }
        PermissionUtils.requestEach(activity,
                android.Manifest.permission.READ_PHONE_STATE,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new DefaultObserver<Permission>() {
                    @Override
                    public void onNext(Permission permission) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        String message = e.getMessage();
                        if (!TextUtils.isEmpty(message)) {
                            ToastUtils.showLong(message);
                        }
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        CallHelper.outgoingCall(activity, callId);
                    }
                });
    }
}
