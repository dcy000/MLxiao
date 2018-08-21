package com.gcml.module_health_record.cc;

import android.content.Context;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.gcml.module_health_record.HealthRecordActivity;

import timber.log.Timber;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/21 10:28
 * created by:gzq
 * description:TODO
 */
public class HealthRecordCC implements IComponent {
    /**
     * 和SendActionNames是成对的
     */
    interface ReceiveActionNames {
        /**
         * 跳转到历史记录
         */
        String TO_HEALTH_RECORD_ACTIVITY = "toHealthRecordActivity";
    }

    /**
     * 和SendKeys是成对的
     */
    interface ReceiveKeys {
        /**
         * 测量类型信息 1：体温；2：血压；3：血氧；4：心跳；5：胆固醇;6：血尿酸；7：心电图；8：体重；
         */
        String KEY_EXTRA_POSITION = "position";
    }

    @Override
    public String getName() {
        return "health_record";
    }

    @Override
    public boolean onCall(CC cc) {
        CCResultActions.setCcId(cc.getCallId());
        Context context = cc.getContext();
        int param = cc.getParamItem(ReceiveKeys.KEY_EXTRA_POSITION);
        String actionName = cc.getActionName();
        switch (actionName) {
            case ReceiveActionNames.TO_HEALTH_RECORD_ACTIVITY:
                HealthRecordActivity.startActivity(context, param);
                break;
            default:
                Timber.e("》》》没有匹配到任何操作Actions");
                break;
        }
        return false;
    }
}
