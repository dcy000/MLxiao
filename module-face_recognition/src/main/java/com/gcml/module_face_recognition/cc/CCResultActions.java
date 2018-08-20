package com.gcml.module_face_recognition.cc;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.gcml.module_face_recognition.bean.UserInfoBean;

import java.util.HashMap;
import java.util.Map;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/15 14:46
 * created by:gzq
 * description:在目标模块中维护CCResultActions表，在源模块中维护CCActions
 */
public class CCResultActions {
    private static String ccId;
    public static String getCcId() {
        return ccId;
    }

    /**
     * 需要在FaceRecognitionCC中进行初始化（必须）
     * @param ccId
     */
    public static void setCcId(String ccId) {
        CCResultActions.ccId = ccId;
    }

    /**
     * CC结果返回key
     */
    public static final String KEY_EXTRA_CC_CALLBACK = "key_cc_callback";
    /**
     * CC结果返回UserInfoBean
     */
    public static final String KEY_EXTRA_CC_USERINFOBEAN="key_cc_userinfobean";
    /**
     * CC框架结果反馈
     *
     * @param action
     */
    public static void onCCResultAction(String action) {
        CC.sendCCResult(ccId, CCResult.success(KEY_EXTRA_CC_CALLBACK, action));
    }

    /**
     * 返回userinforbean
     * @param action
     * @param userInfoBean
     */
    public static void onCCResultAction(String action,UserInfoBean userInfoBean){
        Map<String,Object> map=new HashMap<>();
        map.put(KEY_EXTRA_CC_CALLBACK,action);
        map.put(KEY_EXTRA_CC_USERINFOBEAN,userInfoBean);
        CC.sendCCResult(ccId,CCResult.success());
    }
}
