package com.gcml.health.measure.cc;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;

import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/21 10:40
 * created by:gzq
 * description:向外的操作
 */
public class CCHealthRecordActions {
    public static final String MODULE_NAME = "health_record";

    /**
     * 发送的操作
     */
    interface SendActionNames{
        /**
         * 跳转到历史记录
         */
        String TO_HEALTH_RECORD_ACTIVITY="toHealthRecordActivity";
    }

    /**
     * 发送参数的key
     */
    interface SendKeys{
        /**
         * 测量类型信息 1：体温；2：血压；3：血氧；4：心跳；5：胆固醇;6：血尿酸；7：心电图；8：体重；
         */
        String KEY_EXTRA_POSITION="position";
    }

    public static void jump2HealthRecordActivity(int position ){
        CCResult call = CC.obtainBuilder(MODULE_NAME)
                .setActionName(SendActionNames.TO_HEALTH_RECORD_ACTIVITY)
                .addParam(SendKeys.KEY_EXTRA_POSITION, position)
                .build()
                .call();
        Timber.i("》》》CCHealthRecordActions"+call.toString());
    }
}
