package com.gcml.auth.face.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.auth.face.ui.FaceSignUpActivity;

public class FaceSignUpComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.auth.face.signup";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent();
        intent.setClass(context, FaceSignUpActivity.class);

        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        String componentName = cc.getParamItem("componentName");
        if (!TextUtils.isEmpty(componentName)) {
            intent.putExtra("componentName", componentName);
            context.startActivity(intent);
            CC.sendCCResult(cc.getCallId(), CCResult.success());
            return false;
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
