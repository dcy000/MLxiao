package com.zhang.hui.lib_recreation.cc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.zhang.hui.lib_recreation.tool.activtiy.BaikeActivity;
import com.zhang.hui.lib_recreation.tool.activtiy.ToolsActivity;

/**
 * Created by lenovo on 2018/8/13.
 */

public class ComponentRecreation implements IComponent {

    @Override
    public String getName() {
        return "app.component.recreation";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = null;
        String actionName = cc.getActionName();

//        switch (actionName) {
//            case "0":
//                intent = new Intent(context, RecreationMainActivity.class);
//                break;
//        }

        intent = new Intent(context, ToolsActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);

        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
