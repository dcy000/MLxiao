package com.gcml.auth.ui;

import android.app.Application;
import android.support.annotation.NonNull;

import com.billy.cc.core.component.CC;
import com.gcml.common.mvvm.BaseViewModel;

public class AuthViewModel extends BaseViewModel {
    public AuthViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {

    }

    public void goSignUp(){
        CC.obtainBuilder("com.gcml.old.user.auth").setActionName("signup").build().callAsync();
    }

    public void goSignInByPhone(){
        CC.obtainBuilder("com.gcml.old.user.auth").setActionName("signin").build().callAsync();
    }

    public void goSignInByFace(){
        CC.obtainBuilder("face_recognition").setActionName("To_FaceRecognitionActivity").build().callAsync();
    }

    public void goWifi(){
        CC.obtainBuilder("com.gcml.old.wifi").build().callAsync();
    }

    public void goUserProtocol(){
        CC.obtainBuilder("com.gcml.old.user.auth").setActionName("protocol").build().callAsync();
    }
}
