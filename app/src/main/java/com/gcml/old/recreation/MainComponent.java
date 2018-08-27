package com.gcml.old.recreation;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.example.han.referralproject.homepage.MainActivity;

/**
 * Created by lenovo on 2018/8/27.
 */

public class MainComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.old.main";
    }

    @Override
    public boolean onCall(CC cc) {

        Context context = cc.getContext();

        Intent intent = new Intent(context, MainActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        context.startActivity(intent);
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
