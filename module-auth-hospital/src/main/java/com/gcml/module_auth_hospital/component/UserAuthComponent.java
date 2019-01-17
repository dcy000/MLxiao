package com.gcml.module_auth_hospital.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.common.IConstant;
import com.gcml.module_auth_hospital.doctor.DoctorLoginActivity;
import com.gcml.module_auth_hospital.ui.login.UserLoginsActivity;

/**
 * Created by lenovo on 2019/1/17.
 */

public class UserAuthComponent implements IComponent {
    @Override
    public String getName() {
        return IConstant.KEY_HOSPITAL_USER_SIGN;
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent();
        intent.setClass(context, UserLoginsActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
