package com.gcml.old.healthmanage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.example.han.referralproject.healthmanage.HealthManageActivity;
import com.example.han.referralproject.settting.activity.FactoryTestActivity;

/**
 * Created by lenovo on 2019/3/1.
 */

public class HealthManageComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.old.health.manage";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent(context, HealthManageActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
        return false;
    }
}
