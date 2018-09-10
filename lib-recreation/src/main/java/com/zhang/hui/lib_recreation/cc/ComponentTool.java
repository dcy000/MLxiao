package com.zhang.hui.lib_recreation.cc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponent;
import com.zhang.hui.lib_recreation.tool.activtiy.BaikeActivity;
import com.zhang.hui.lib_recreation.tool.activtiy.CalculationActivity;
import com.zhang.hui.lib_recreation.tool.activtiy.CookBookActivity;
import com.zhang.hui.lib_recreation.tool.activtiy.DateInquireActivity;
import com.zhang.hui.lib_recreation.tool.activtiy.HistoryTodayActivity;
import com.zhang.hui.lib_recreation.tool.activtiy.JieMengActivity;
import com.zhang.hui.lib_recreation.tool.activtiy.ToolsActivity;

/**
 * 每个小工具具体的页面
 * Created by lenovo on 2018/8/13.
 */

public class ComponentTool implements IComponent {
    public static final String ACTION_JIEMENG = "oneiromancy";
    public static final String ACTION_TODAY = "historyToday";
    public static final String ACTION_DATE = "dateInquiry";
    public static final String ACTION_COOK = "cookBook";
    public static final String ACTION_BAIKE = "Baike";
    public static final String ACTION_CALCULATE = "calculate";

    @Override
    public String getName() {
        return "app.component.recreation.tool";
    }

    @Override
    public boolean onCall(CC cc) {
        Context context = cc.getContext();
        String actionName = cc.getActionName();
        Intent intent = new Intent();
        switch (actionName) {
            case ACTION_JIEMENG:
                intent.setClass(context, JieMengActivity.class);
                break;
            case ACTION_TODAY:
                intent.setClass(context, HistoryTodayActivity.class);
                break;
            case ACTION_DATE:
                intent.setClass(context, DateInquireActivity.class);
                break;
            case ACTION_COOK:
                intent.setClass(context, CookBookActivity.class);
                break;
            case ACTION_BAIKE:
                intent.setClass(context, BaikeActivity.class);
                break;
            case ACTION_CALCULATE:
                intent.setClass(context, CalculationActivity.class);
                break;
        }

        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        CC.sendCCResult(cc.getCallId(), CCResult.success());
        return false;
    }
}
