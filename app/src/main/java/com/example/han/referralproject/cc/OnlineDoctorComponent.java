package com.example.han.referralproject.cc;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.example.han.referralproject.recyclerview.OnlineDoctorListActivity;

public class OnlineDoctorComponent implements IComponent {
    @Override
    public String getName() {
        return "com.online.doctor.list";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent(context, OnlineDoctorListActivity.class);
        if (context instanceof Application) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra("flag", "contract");
        context.startActivity(intent);
        return false;
    }
}
