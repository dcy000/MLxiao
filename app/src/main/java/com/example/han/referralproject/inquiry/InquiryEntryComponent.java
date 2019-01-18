package com.example.han.referralproject.inquiry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.example.han.referralproject.inquiry.activity.InquiryEntranceActivity;

import static com.gcml.common.IConstant.KEY_INUIRY_ENTRY;

/**
 * Created by lenovo on 2019/1/18.
 */

public class InquiryEntryComponent implements IComponent {
    @Override
    public String getName() {
        return KEY_INUIRY_ENTRY;
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        Intent intent = new Intent();
        intent.setClass(context, InquiryEntranceActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        context.startActivity(intent);
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
