package com.example.han.referralproject.cc;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.example.han.referralproject.tcm.SymptomCheckActivity;

public class SymptomCheckComponent implements IComponent {
    @Override
    public String getName() {
        return "com.app.symptom.check";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent(context, SymptomCheckActivity.class);
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        return false;
    }
}
