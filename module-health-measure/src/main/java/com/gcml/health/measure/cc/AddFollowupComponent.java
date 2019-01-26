package com.gcml.health.measure.cc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.gcml.health.measure.health_profile.AddFollowupActivity;

public class AddFollowupComponent implements IComponent {
    @Override
    public String getName() {
        return "health.profile.add.followup";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        String healthRecordId = cc.getParamItem("healthRecordId");
        String rdRecordId = cc.getParamItem("rdRecordId");
        Intent intent = new Intent(context, AddFollowupActivity.class);
        intent.putExtra("healthRecordId", healthRecordId);
        intent.putExtra("rdRecordId", rdRecordId);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        return false;
    }
}
