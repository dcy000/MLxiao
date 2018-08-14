package com.gcml.module_face_recognition.cc;

import android.app.Activity;
import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_face_recognition.RegisterHead2XunfeiActivity;
import com.gcml.module_face_recognition.manifests.SPManifest;

import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/13 15:51
 * created by:gzq
 * description:人脸识别模块路由
 */
public class FaceRecognitionCC implements IComponent {
    private static final String KEY_EXTRA_XFID = "key_xfid";

    @Override
    public String getName() {
        return "face_recognition";
    }

    @Override
    public boolean onCall(CC cc) {
        Timber.e(cc.getActionName());
        String actionName = cc.getActionName();
        switch (actionName) {
            case "To_RegisterHead2XunfeiActivity":
                //如果从前一个页面传递有讯飞id则直接使用，如果没有则尝试去SP中获取。
                String keyExtraXfid = cc.getParamItem(KEY_EXTRA_XFID);
                if (!TextUtils.isEmpty(keyExtraXfid)) {
                    RegisterHead2XunfeiActivity.startActivity(cc.getContext(),
                            keyExtraXfid, cc.getCallId());
                } else {
                    RegisterHead2XunfeiActivity.startActivity(cc.getContext(),
                            SPManifest.getXunfeiId(), cc.getCallId());
                }
                //返回true表示异步的，如果没有CC.sendResult()则该异步一直等待
                return true;
            default:
                ToastUtils.showShort("没有匹配到CC_Action");
                break;
        }
        //返回false表示同步操作
        return false;
    }
}
