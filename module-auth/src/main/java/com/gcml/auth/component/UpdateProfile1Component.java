package com.gcml.auth.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.auth.ui.profile.SimpleProfileActivity;

public class UpdateProfile1Component implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.auth.updateProfile1";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent();
        String signUpType = cc.<String>getParamItem("signUpType");
        if (!TextUtils.isEmpty(signUpType)) {
            intent.putExtra("signUpType", signUpType);
        }

        String signUpIdCard = cc.<String>getParamItem("signUpIdCard");
        if (!TextUtils.isEmpty(signUpIdCard)) {
            intent.putExtra("signUpIdCard", signUpIdCard);
        }

        intent.setClass(context, SimpleProfileActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        intent.putExtra("callId", cc.getCallId());
        context.startActivity(intent);
        return true;
    }
}
