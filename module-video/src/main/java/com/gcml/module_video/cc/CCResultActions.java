package com.gcml.module_video.cc;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/25 15:22
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
     * 回传参数的key
     */
    interface ResultKeys{
        /**
         * CC结果返回key
         */
        String KEY_EXTRA_CC_CALLBACK = "key_cc_callback";
    }
    /**
     * CC框架结果反馈
     *
     * @param action
     */
    public static void onCCResultAction(String action) {
        CC.sendCCResult(ccId, CCResult.success(ResultKeys.KEY_EXTRA_CC_CALLBACK, action));
    }
}
