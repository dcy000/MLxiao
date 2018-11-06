package com.gcml.family.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.family.ui.FamilyNewsActivity;

/**
 * desc: FamilyPrivacyComponent .
 * author: wecent .
 * date: 2018/8/21 .
 */

public class FamilyNewsComponent implements IComponent {

    @Override
    public String getName() {
        return "app.component.family.news";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent(context, FamilyNewsActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        //发送组件调用的结果（返回信息）
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
