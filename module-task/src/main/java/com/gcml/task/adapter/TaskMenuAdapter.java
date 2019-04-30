package com.gcml.task.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.TimeHelper;
import com.gcml.task.R;
import com.gcml.task.bean.get.TaskBean;
import com.sjtu.yifei.route.Routerfit;

import java.util.List;

public class TaskMenuAdapter extends BaseQuickAdapter<TaskBean.TaskListBean, BaseViewHolder> {

    public TaskMenuAdapter(int layoutResId, @Nullable List<TaskBean.TaskListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskBean.TaskListBean item) {
        helper.setText(R.id.tv_task_name, item.name);
        helper.setText(R.id.tv_task_name_other, item.name);
        helper.setText(R.id.tv_task_time, "(测试时间：" + TimeHelper.formatDateTimeHour(item.remindStart) + "-" + TimeHelper.formatDateTimeHour(item.remindEnd) + ")");
        if (item.mustStatus.equals("1")) {
            helper.setVisible(R.id.tv_task_tag, true);
            helper.setVisible(R.id.ll_task_name, true);
            helper.setVisible(R.id.tv_task_name_other, false);
            helper.setText(R.id.tv_task_tag, "必做");
        } else {
            helper.setVisible(R.id.tv_task_tag, false);
            helper.setVisible(R.id.ll_task_name, false);
            helper.setVisible(R.id.tv_task_name_other, true);
        }
        if ("0".equals(item.complitionStatus)) {
            if (TimeHelper.getSecondsFromDate(TimeHelper.getCUSeconds()) > item.remindStart && TimeHelper.getSecondsFromDate(TimeHelper.getCUSeconds()) < item.remindEnd) {
                helper.setVisible(R.id.tv_task_action, true);
                helper.setVisible(R.id.iv_task_action, false);
                helper.setText(R.id.tv_task_action, "去完成");
                helper.setTextColor(R.id.tv_task_action, mContext.getResources().getColor(R.color.config_color_white));
                helper.setBackgroundRes(R.id.tv_task_action, R.drawable.btn_task_action1);
                helper.setOnClickListener(R.id.tv_task_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.taskType.equals("32")) {
                            CC.obtainBuilder("app.component.task.diary").addParam("what", 0).build().callAsync();
                        } else if (item.taskType.equals("33")) {
                            CC.obtainBuilder("app.component.task.diary").addParam("what", 1).build().callAsync();
                        } else if (item.taskType.equals("34")) {
                            CC.obtainBuilder("app.component.task.diary").addParam("what", 2).build().callAsync();
                        } else if (item.taskType.equals("31")) {
                            Routerfit.register(AppRouter.class).skipAllMeasureActivity(25, true);
                        } else if (item.taskType.equals("11")) {
                            Routerfit.register(AppRouter.class).skipAllMeasureActivity(22, true);
                        }
                    }
                });
            } else if (TimeHelper.getSecondsFromDate(TimeHelper.getCUSeconds()) < item.remindStart) {
                helper.setVisible(R.id.tv_task_action, true);
                helper.setVisible(R.id.iv_task_action, false);
                helper.setText(R.id.tv_task_action, "未开启");
                helper.setTextColor(R.id.tv_task_action, mContext.getResources().getColor(R.color.task_color_base));
                helper.setBackgroundRes(R.id.tv_task_action, R.drawable.btn_task_action2);
            } else if (TimeHelper.getSecondsFromDate(TimeHelper.getCUSeconds()) > item.remindEnd) {
                helper.setVisible(R.id.tv_task_action, true);
                helper.setVisible(R.id.iv_task_action, false);
                helper.setText(R.id.tv_task_action, "已过期");
                helper.setTextColor(R.id.tv_task_action, mContext.getResources().getColor(R.color.config_color_quarantine));
                helper.setBackgroundRes(R.id.tv_task_action, R.drawable.btn_task_action3);
            }
        } else {
            helper.setVisible(R.id.tv_task_action, false);
            helper.setVisible(R.id.iv_task_action, true);
        }
    }
}
