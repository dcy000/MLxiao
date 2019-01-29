package com.gcml.module_health_profile.cc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.gcml.module_health_profile.OutputResultActivity;
import com.gcml.module_health_profile.webview.AddHealthProfileActivity;

public class FileComponent implements IComponent {
    @Override
    public String getName() {
        return "health.profile.file";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent(context, AddHealthProfileActivity.class);
        intent.putExtra("RdCordId","22d594369d8246ad9542f462d6f0f4ce");
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        return false;
    }
}
