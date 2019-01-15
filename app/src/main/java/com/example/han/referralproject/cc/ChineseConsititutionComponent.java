package com.example.han.referralproject.cc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.example.han.referralproject.tcm.activity.OlderHealthManagementSerciveActivity;

public class ChineseConsititutionComponent implements IComponent {
    @Override
    public String getName() {
        return "app.chinese.consititution";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent(context, OlderHealthManagementSerciveActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        return false;
    }
}
