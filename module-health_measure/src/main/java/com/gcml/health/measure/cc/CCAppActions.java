package com.gcml.health.measure.cc;

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
         * 跳转到NormalHightActivity页面
         */
        String TO_NORMALHIGHTACTIVITY = "ToNormalHightActivity";
        /**
         * 跳转到TreatmentPlanActivity页面
         */
        String TO_TREATMENTPLANACTIVITY = "ToTreatmentPlanActivity";
        /**
         * 跳转到MainActivity
         */
        String TO_MAINACTIVITY = "ToMainActivity";
    }

    /**
     * 发送数据的Key
     */
    interface SendKeys {
        /**
         * 表示从哪里来
         */
        String KEY_EXTRA_FROM_WHERE = "fromWhere";
    }

    public static void jump2NormalHightActivity(String fromWhere) {
        CCResult call = CC.obtainBuilder(MODULE_NAME)
                .setActionName(SendActionNames.TO_NORMALHIGHTACTIVITY)
                .addParam(SendKeys.KEY_EXTRA_FROM_WHERE, fromWhere)
                .build()
                .call();
        Timber.i("》》》CCAppActions：" + call.toString());
    }

    public static void jump2TreatmentPlanActivity() {
        CCResult call = CC.obtainBuilder(MODULE_NAME)
                .setActionName(SendActionNames.TO_TREATMENTPLANACTIVITY)
                .build()
                .call();
        Timber.i("》》》CCAppActions：" + call.toString());
    }

    public static void jump2MainActivity() {
        CCResult call = CC.obtainBuilder(MODULE_NAME)
                .setActionName(SendActionNames.TO_MAINACTIVITY)
                .build()
                .call();
        Timber.i("》》》CCAppActions：" + call.toString());
    }
}
