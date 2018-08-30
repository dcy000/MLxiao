package com.gcml.health.measure.health_report_form;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gcml.health.measure.R;
import com.gcml.lib_utils.base.ToolbarBaseActivity;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version:V1.2.5
 * created on 2018/8/29 13:56
 * created by:gzq
 * description:TODO
 */
public class HealthReportFormDetailActivity extends ToolbarBaseActivity{
    public static void startActivity(Context context){
        Intent intent=new Intent(context,HealthReportFormDetailActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_measure_activity_report_form_detail);
    }
}
