package com.gcml.module_health_profile.cc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.gcml.module_health_profile.HealthProfileActivity;
import com.gcml.module_health_profile.bracelet.activity.BraceletActivtity;

public class BraceletComponent implements IComponent{
    @Override
    public String getName() {
        return "com.gcml.bracelet";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent=new Intent(context, BraceletActivtity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
        return false;
    }
}
