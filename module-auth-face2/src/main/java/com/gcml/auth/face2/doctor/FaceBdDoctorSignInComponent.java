package com.gcml.auth.face2.doctor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.gcml.auth.face2.model.FaceBdRepository;
import com.gcml.auth.face2.ui.FaceBdSignInActivity;
import com.gcml.common.data.UserSpHelper;

import io.reactivex.schedulers.Schedulers;

/**
 * 1. 人脸识别登录 （verify = false）
 * 2. 人脸认证登录 （verify = true）
 */
public class FaceBdDoctorSignInComponent implements IComponent {
    @Override
    public String getName() {
        return "com.gcml.auth.face2.doctor.signin";
    }

    @Override
    public boolean onCall(CC cc) {

        // 人脸认证登录
        Boolean verify = cc.getParamItem("verify");
        Boolean skip = cc.getParamItem("skip");

        String faceId = "";
        if (verify != null && verify) {
            String userId = UserSpHelper.getUserId();
            if (TextUtils.isEmpty(userId)) {
                CC.sendCCResult(cc.getCallId(), CCResult.error("请先登录!"));
                return false;
            }
            FaceBdRepository repository = new FaceBdRepository();
            faceId = repository.getFaceId(userId)
                    .subscribeOn(Schedulers.io())
                    .blockingFirst();

            if (TextUtils.isEmpty(faceId)) {
                CC.sendCCResult(cc.getCallId(), CCResult.error("请先注册人脸!"));
                return false;
            }
        }

        Context context = cc.getContext();
        Intent intent = new Intent();
        intent.setClass(context, FaceBdSignInActivity.class);

        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        intent.putExtra("callId", cc.getCallId());
        intent.putExtra("verify", verify);
        intent.putExtra("faceId", faceId);
        intent.putExtra("skip", skip);
        context.startActivity(intent);
        return true;
    }
}
