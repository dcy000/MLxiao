package com.gcml.module_health_record.cc;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;

import java.sql.Time;

import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/21 16:16
 * created by:gzq
 * description:进入测量界面的操作
 */
public class CCHealthMeasureActions {
    public static final String MODULE_NAME = "health_measure";

    /**
     * 发送的操作
     */
    interface SendActionNames {
        /**
         * 跳转到AllMeasureActivity
         */
        String TO_ALL_MEASURE_ACTIVITY = "ToAllMeasureActivity";
    }

    interface SendKeys {
        String KEY_EXTRA_MEASURE_TYPE = "measure_type";
    }

    /**
     * 跳转到测量界面
     *
     * @param measureType
     */
    public static void jump2AllMeasureActivity(int measureType) {

        CCResult call = CC.obtainBuilder(MODULE_NAME)
                .setActionName(SendActionNames.TO_ALL_MEASURE_ACTIVITY)
                .addParam(SendKeys.KEY_EXTRA_MEASURE_TYPE, measureType)
                .build()
                .call();
        Timber.d("》》》CCHealthMeasureActions" + call.toString());
    }
}
