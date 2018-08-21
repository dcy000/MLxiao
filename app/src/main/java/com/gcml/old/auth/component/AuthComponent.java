package com.gcml.old.auth.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.old.auth.profile.AgreementActivity;
import com.gcml.old.auth.register.simple.SignUp01NameActivity;
import com.gcml.old.auth.signin.ChooseLoginTypeActivity;
import com.gcml.old.auth.signin.SignInActivity;

public class AuthComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.old.user.auth";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent();
        String actionName = cc.getActionName();
        if ("signin".equals(actionName)) {
            intent.setClass(context, SignInActivity.class);
        } else if ("signup".equals(actionName)) {
            intent.setClass(context, SignUp01NameActivity.class);
        }else if ("protocol".equals(actionName)) {
            intent.setClass(context, AgreementActivity.class);
        } else {
            intent.setClass(context, ChooseLoginTypeActivity.class);
//            CC.obtainBuilder("com.gcml.user.auth").build().callAsync();
//            return false;
        }
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
