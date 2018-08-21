package com.gcml.module_face_recognition.cc;

import android.content.Context;
import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.gcml.lib_utils.display.ToastUtils;
import com.gcml.module_face_recognition.FaceRecognitionActivity;
import com.gcml.module_face_recognition.RegisterHead2XunfeiActivity;
import com.gcml.module_face_recognition.manifests.FaceRecognitionSPManifest;

import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/13 15:51
 * created by:gzq
 * description:人脸识别模块路由
 * TODO:额为补充：Action的命名规则：如果自己是源，那么就CC开头，如CCxxxxx;如果自己是目标，是接收方，则xxxxCC
 */
public class FaceRecognitionCC implements IComponent {
    @Override
    public String getName() {
        return "face_recognition";
    }

    /**
     * 接收参数key，和SendKeys成对
     */
    interface ReceiveKeys {
        /**
         * 讯飞id
         */
        String KEY_EXTRA_XFID = "key_xfid";
        /**
         * 传递bundle对象的key
         */
        String KEY_EXTRA_BUNDLE = "key_bundle";
    }

    /**
     * 接收的操作Actions,和SendActionNames成对
     */
    interface ReceiceActionNames {
        /**
         * 跳转RegisterHead2XunfeiActivity
         */
        String TO_REGISTERHEAD2XUNFEIACTIVITY = "To_RegisterHead2XunfeiActivity";
        /**
         * 跳转到FaceRecognitionActivity
         */
        String TO_FACERECOGNITIONACTIVITY = "To_FaceRecognitionActivity";
    }

    @Override
    public boolean onCall(CC cc) {
        CCResultActions.setCcId(cc.getCallId());
        Context context = cc.getContext();
        Timber.i(cc.getActionName());
        String actionName = cc.getActionName();
        switch (actionName) {
            case ReceiceActionNames.TO_REGISTERHEAD2XUNFEIACTIVITY:
                //如果从前一个页面传递有讯飞id则直接使用，如果没有则尝试去SP中获取。
                String keyExtraXfid = cc.getParamItem(ReceiveKeys.KEY_EXTRA_XFID);
                if (!TextUtils.isEmpty(keyExtraXfid)) {
                    RegisterHead2XunfeiActivity.startActivity(context,
                            keyExtraXfid);
                } else {
                    RegisterHead2XunfeiActivity.startActivity(context,
                            FaceRecognitionSPManifest.getXunfeiId());
                }
                //返回true表示异步的，如果没有CC.sendResult()则该异步一直等待
                return true;
            case ReceiceActionNames.TO_FACERECOGNITIONACTIVITY:
                FaceRecognitionActivity.startActivity(context, cc.getParamItem(ReceiveKeys.KEY_EXTRA_BUNDLE));
                return true;
            default:
                ToastUtils.showShort("没有匹配到CC_Action");
                break;
        }
        //返回false表示同步操作
        return false;
    }

}
