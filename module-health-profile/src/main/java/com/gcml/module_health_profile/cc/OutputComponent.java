package com.gcml.module_health_profile.cc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.gcml.module_health_profile.OutputResultActivity;

public class OutputComponent implements IComponent {
    @Override
    public String getName() {
        return "health.profile.outputresult";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        String rdRecordId = cc.getParamItem("rdRecordId");
        String userRecordId = cc.getParamItem("userRecordId");
        String typeString =cc.getParamItem("typeString");
        Intent intent = new Intent(context, OutputResultActivity.class);
        intent.putExtra("rdRecordId", rdRecordId);
        intent.putExtra("userRecordId", userRecordId);
        intent.putExtra("typeString",typeString);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        return false;
    }
}
