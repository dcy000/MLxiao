package com.gcml.old.recreation;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.hypertensionmanagement.util.AppManager;

/**
 * Created by lenovo on 2018/8/27.
 */

public class FinishAllActivityComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.old.finishAll";
    }

    @Override
    public boolean onCall(CC cc) {
        AppManager.getAppManager().finishAllActivity();
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
