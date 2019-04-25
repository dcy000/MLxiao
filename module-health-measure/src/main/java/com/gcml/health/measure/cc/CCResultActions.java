package com.gcml.health.measure.cc;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;

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
     * 回传参数的key
     */
    interface ResultKeys{
        /**
         * CC结果返回key
         */
        public static final String KEY_EXTRA_CC_CALLBACK = "key_cc_callback";
    }

    /**
     * 异步操作的Actions
     */
    interface ResultActions{
        /**
         * 点击历史记录
         */
        String PRESS_HEALTH_RECORD="press_health_record";
    }
    /**
     * CC框架结果反馈
     *
     * @param action
     */
    public static void onCCResultAction(String action) {
        if (!TextUtils.isEmpty(ccId)){
            CC.sendCCResult(ccId, CCResult.success(ResultKeys.KEY_EXTRA_CC_CALLBACK, action));
        }
    }

    /**
     * 返回一个Fragment对象
     * @param fragment
     */
    public static void onCCResultActionWithFragmentBean(Fragment fragment){
        CC.sendCCResult(ccId,CCResult.success(ResultKeys.KEY_EXTRA_CC_CALLBACK,fragment));
    }
}
