package com.gcml.auth.face.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.auth.face.ui.FaceSignInActivity;
import com.gcml.common.data.UserEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FaceSignInComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.auth.face.signin";
    }

    @Override
    public boolean onCall(CC cc) {
        Observable<List<UserEntity>> rxUsers = CC.obtainBuilder("com.gcml.auth.getUsers")
                .build()
                .call()
                .getDataItem("data");
        List<UserEntity> users = rxUsers
                .onErrorResumeNext(Observable.empty())
                .subscribeOn(Schedulers.io())
                .blockingFirst();
        boolean hasFace = false;
        for (UserEntity user : users) {
            if (!TextUtils.isEmpty(user.xfid)) {
                hasFace = true;
            }
        }
        if (!hasFace) {
            CC.sendCCResult(cc.getCallId(), CCResult.error("您尚未在当前设备登录，请先注册或用账号密码登录。"));
            return false;
        }

        Context context = cc.getContext();
        Intent intent = new Intent();
        intent.setClass(context, FaceSignInActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        boolean skip = cc.getParamItem("skip", false);
        boolean currentUser = cc.getParamItem("currentUser", false);
        intent.putExtra("skip", skip);
        intent.putExtra("currentUser", currentUser);
        intent.putExtra("callId", cc.getCallId());
        intent.putParcelableArrayListExtra("users", new ArrayList<>(users));
        context.startActivity(intent);
        return true;
    }
}
