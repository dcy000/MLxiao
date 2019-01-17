package com.gcml.old.setting;

import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.example.han.referralproject.WelcomeActivity;
import com.example.han.referralproject.settting.activity.VoiceSettingActivity;

/**
 * Created by lenovo on 2019/1/15.
 */

public class WelcomComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.old.welcome";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent();
        intent.setClass(context, WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
