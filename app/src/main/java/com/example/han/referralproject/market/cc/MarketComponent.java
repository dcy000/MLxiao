package com.example.han.referralproject.market.cc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.example.han.referralproject.activity.MarketActivity;

/**
 * Created by lenovo on 2018/9/20.
 */

public class MarketComponent implements IComponent {
    @Override
    public String getName() {
        return "com.old.gcml.market";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent(context, MarketActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        cc.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
