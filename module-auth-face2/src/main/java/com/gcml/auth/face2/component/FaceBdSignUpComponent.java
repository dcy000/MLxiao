package com.gcml.auth.face2.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.auth.face2.ui.FaceBdSignUpActivity;
import com.gcml.common.data.UserSpHelper;

/**
 * 1. 注册人脸
 * 2. 更新人脸
 */
public class FaceBdSignUpComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.auth.face2.signup";
    }

    @Override
    public boolean onCall(CC cc) {
        String userId = UserSpHelper.getUserId();
        if (TextUtils.isEmpty(userId)) {
            CC.sendCCResult(cc.getCallId(), CCResult.error("请先登录!"));
            return false;
        }

        Context context = cc.getContext();
        Intent intent = new Intent();
        intent.setClass(context, FaceBdSignUpActivity.class);

        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        intent.putExtra("callId", cc.getCallId());
        intent.putExtra("userId", userId);
        context.startActivity(intent);
        return true;
    }
}
