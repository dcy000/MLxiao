package com.gcml.auth.face.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.auth.face.model.FaceRepository;
import com.gcml.auth.face.ui.FaceSignInActivity;
import com.gcml.common.data.UserEntity;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class FaceSignInComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.auth.face.signin";
    }

    @Override
    public boolean onCall(CC cc) {
        FaceRepository faceRepository = new FaceRepository();
        List<UserEntity> users = faceRepository
                .getLocalUsers()
                .subscribeOn(Schedulers.io())
                .blockingFirst();
        if (users.isEmpty()) {
            CC.sendCCResult(cc.getCallId(), CCResult.error("当前设备未注册过人脸, 请登录后注册人脸"));
            return false;
        }

        Context context = cc.getContext();
        Intent intent = new Intent();
        intent.setClass(context, FaceSignInActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        String componentName = cc.getParamItem("componentName");
        if (!TextUtils.isEmpty(componentName)) {
            intent.putExtra("callId", cc.getCallId());
            intent.putExtra("componentName", componentName);
            context.startActivity(intent);
            return true;
        }

        String actionName = cc.getActionName();
        if ("forResult".equals(actionName)) {
            intent.putExtra("callId", cc.getCallId());
            context.startActivity(intent);
            return true;
        }

        context.startActivity(intent);
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
