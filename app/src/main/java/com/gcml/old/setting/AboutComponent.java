package com.gcml.old.setting;

import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.example.han.referralproject.settting.activity.AboutActivity;
import com.example.han.referralproject.settting.activity.VoiceSettingActivity;

/**
 * Created by lenovo on 2019/1/15.
 */

public class AboutComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.old.setting.about";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent();
        intent.setClass(context, AboutActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
