package com.gcml.old.recreation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.example.han.referralproject.children.ChildEduHomeActivity;
import com.ml.edu.old.TheOldHomeActivity;

/**
 * Created by lenovo on 2018/8/22.
 */

public class RecreationComponent implements IComponent {
    public static final String LRYL = "lryl";
    public static final String ETYJ = "etyj";

    @Override
    public String getName() {
        return "com.gcml.old.recreation";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent();

        String actionName = cc.getActionName();
        if (LRYL.equals(actionName)) {
            intent = new Intent(context, TheOldHomeActivity.class);
        } else if (ETYJ.equals(actionName)) {
            intent = new Intent(context, ChildEduHomeActivity.class);
        }
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        context.startActivity(intent);
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
