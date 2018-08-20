package com.example.han.referralproject.cc;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;

import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/20 16:39
 * created by:gzq
 * description:操作module-health_measure模块的actions
 * 源模块CCHealthMeasureActions,目标模块
 */
public class CCHealthMeasureActions {
    /**
     * 模块名字
     */
    public static final String MODULE_NAME = "health_measure";

    /**
     * 发送的Action
     */
    interface SendActionNames {
        /**
         * 单测
         */
        String SINGLE_MEASURE = "SingleMeasure";
        /**
         * 首诊
         */
        String FIRST_DIAGNOSIS = "FirstDiagnosis";
        /**
         * 心电
         */
        String TO_ECG="ToECG";
    }

    /**
     * 发送的数据的key
     */
    interface SendKeys{
        /**
         * 表示这个跳转是哪一个页面发起的
         */
        String KEY_EXTRA_FROM_WHERE = "key_from_where";
    }
    /**
     * 跳转到选择所有测量设备的界面
     */
    public static void jump2MeasureChooseDeviceActivity() {
        CCResult call = CC.obtainBuilder(MODULE_NAME)
                .setActionName(SendActionNames.SINGLE_MEASURE)
                .build()
                .call();
        Timber.d("CCHealthMeasureActions>>>>>>>>>"+call.toString());
    }

    /**
     * 跳转到首诊界面
     */
    public static void jump2HealthIntelligentDetectionActivity(){
        CCResult call = CC.obtainBuilder(MODULE_NAME)
                .setActionName(SendActionNames.FIRST_DIAGNOSIS)
                .build().call();
        Timber.d("CCHealthMeasureActions>>>>>>>>>"+call.toString());
    }
    public static void jump2XinDianDetectActivity(){
        CCResult call = CC.obtainBuilder(MODULE_NAME)
                .setActionName(SendActionNames.TO_ECG)
                .build().call();
        Timber.d("CCHealthMeasureActions>>>>>>>>>"+call.toString());
    }
}
