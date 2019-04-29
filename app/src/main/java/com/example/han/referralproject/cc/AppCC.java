package com.example.han.referralproject.cc;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.IComponent;
import com.example.han.referralproject.health_manager_program.TreatmentPlanActivity;
import com.example.han.referralproject.homepage.MainActivity;
import com.example.han.referralproject.hypertensionmanagement.activity.BasicInformationActivity;
import com.example.han.referralproject.hypertensionmanagement.activity.HypertensionTipActivity;
import com.example.han.referralproject.hypertensionmanagement.activity.IsEmptyStomachOrNotActivity;
import com.example.han.referralproject.hypertensionmanagement.activity.NormalHightActivity;
import com.example.han.referralproject.hypertensionmanagement.activity.SlowDiseaseManagementTipActivity;

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
         * 跳转到TreatmentPlanActivity页面
         */
        String TO_TREATMENTPLANACTIVITY = "ToTreatmentPlanActivity";
        /**
         * 跳转到MainActivity
         */
        String TO_MAINACTIVITY = "ToMainActivity";
        String TO_SLOWDISEASEMANAGEMENTTIPACTIVITY="To_SlowDiseaseManagementTipActivity";
        String TO_BASICINFORMATIONACTIVITY="To_BasicInformationActivity";
        String TO_HypertensionTipActivity="To_HypertensionTipActivity";
        String TO_ISEMPTYSTOMACHORNOTACTIVITY="To_IsEmptyStomachOrNotActivity";
    }

    @Override
    public boolean onCall(CC cc) {
        CCResultActions.setCcId(cc.getCallId());
        Context context = cc.getContext();
        String actionName = cc.getActionName();
        switch (actionName) {
            case ReceiveActionNames.TO_TREATMENTPLANACTIVITY:
                Intent intent1 = new Intent(context, TreatmentPlanActivity.class);
                if (context instanceof Application) {
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent1);
                break;
            case ReceiveActionNames.TO_MAINACTIVITY:
                Intent intent2 = new Intent(context, MainActivity.class);
                if (context instanceof Application) {
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent2);
                break;
            case ReceiveActionNames.TO_SLOWDISEASEMANAGEMENTTIPACTIVITY:
                Intent intent3 = new Intent(context, SlowDiseaseManagementTipActivity.class);
                if (context instanceof Application) {
                    intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent3);
                break;
            case ReceiveActionNames.TO_BASICINFORMATIONACTIVITY:
                Intent intent4 = new Intent(context, BasicInformationActivity.class);
                intent4.putExtra("fromWhere",(String)cc.getParamItem("fromWhere"));
                if (context instanceof Application) {
                    intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent4);
                break;
            case ReceiveActionNames.TO_HypertensionTipActivity:
                Intent intent5 = new Intent(context, HypertensionTipActivity.class);
                if (context instanceof Application) {
                    intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent5);
                break;
            case ReceiveActionNames.TO_ISEMPTYSTOMACHORNOTACTIVITY:
                Intent intent6 = new Intent(context, IsEmptyStomachOrNotActivity.class);
                if (context instanceof Application) {
                    intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                context.startActivity(intent6);
                break;

            default:
                break;
        }
        return false;
    }
}
