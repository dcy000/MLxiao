package com.example.han.referralproject.cc;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.example.han.referralproject.health_manager_program.TreatmentPlanActivity;
import com.example.han.referralproject.hypertensionmanagement.activity.NormalHightActivity;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/14 13:47
 * created by:gzq
 * description:其他模块跳转过来的港口
 */
public class AppCC implements IComponent {
    @Override
    public String getName() {
        return "app";
    }

    /**
     * 接收的actions
     */
    interface ReceiveActionNames {
        /**
         * 跳转到NormalHightActivity页面
         */
        String TO_NORMALHIGHTACTIVITY = "ToNormalHightActivity";
        /**
         * 跳转到TreatmentPlanActivity页面
         */
        String TO_TREATMENTPLANACTIVITY = "ToTreatmentPlanActivity";
    }

    /**
     * 接收数据的key
     */
    interface ReceiveKeys {
        /**
         * 表示从哪里来
         */
        String KEY_EXTRA_FROM_WHERE = "fromWhere";
    }

    @Override
    public boolean onCall(CC cc) {
        CCResultActions.setCcId(cc.getCallId());
        Context context = cc.getContext();
        String actionName = cc.getActionName();
        switch (actionName) {
            case ReceiveActionNames.TO_NORMALHIGHTACTIVITY:
                String param = cc.getParamItem(ReceiveKeys.KEY_EXTRA_FROM_WHERE);
                Intent intent = new Intent(context, NormalHightActivity.class);
                if (context instanceof Application) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                intent.putExtra("fromWhere", param);
                context.startActivity(intent);
                break;
            case ReceiveActionNames.TO_TREATMENTPLANACTIVITY:
                Intent intent1 = new Intent(context, TreatmentPlanActivity.class);
                if (context instanceof Application) {
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent1);
                break;
            default:
                break;
        }
        return false;
    }
}
