package com.gcml.auth.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

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
        intent.setClass(context, SimpleProfileActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        intent.putExtra("callId", cc.getCallId());
        context.startActivity(intent);
        return true;
    }
}
