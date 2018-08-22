package com.gcml.module_health_record.cc;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;

import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/22 15:30
 * created by:gzq
 * description:TODO
 */
public class CCAppActions {
    public static final String MODULE_NAME = "app";

    /**
     * 发送的操作
     */
    interface SendActionNames {
        /**
         * 跳转到MainActivity
         */
        String TO_MAINACTIVITY = "ToMainActivity";
    }

    public static void jump2MainActivity() {
        CCResult call = CC.obtainBuilder(MODULE_NAME)
                .setActionName(SendActionNames.TO_MAINACTIVITY)
                .build()
                .call();
        Timber.i("》》》CCAppActions：" + call.toString());
    }
}
