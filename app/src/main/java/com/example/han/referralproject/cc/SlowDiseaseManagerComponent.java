package com.example.han.referralproject.cc;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.example.han.referralproject.hypertensionmanagement.activity.SlowDiseaseManagementActivity;

public class SlowDiseaseManagerComponent implements IComponent {
    @Override
    public String getName() {
        return "app.hypertension.manager.slow.disease";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent(context, SlowDiseaseManagementActivity.class);
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
