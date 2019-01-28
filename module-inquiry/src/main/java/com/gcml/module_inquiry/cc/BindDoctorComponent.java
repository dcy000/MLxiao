package com.gcml.module_inquiry.cc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.module_inquiry.ui.BindDoctorActivity;

import static com.gcml.common.IConstant.KEY_BIND_DOCTOR;

/**
 * Created by lenovo on 2019/1/21.
 */

public class BindDoctorComponent implements IComponent {
    @Override
    public String getName() {
        return KEY_BIND_DOCTOR;
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent();
        intent.setClass(context, BindDoctorActivity.class);
//        intent.putExtra("callID", cc.getCallId());
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
        //发送结果给调用方
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
