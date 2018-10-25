package com.gcml.task.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.task.R;
import com.gcml.task.bean.get.TaskHealthBean;

import java.util.List;

public class TaskChoiceAdapter extends BaseQuickAdapter<TaskHealthBean.QuestionListBean.AnswerListBean, BaseViewHolder> {

    public TaskChoiceAdapter(int layoutResId, @Nullable List<TaskHealthBean.QuestionListBean.AnswerListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskHealthBean.QuestionListBean.AnswerListBean item) {
        if (item.isChoosed) {
            helper.getView(R.id.tv_answer).setSelected(true);
        } else {
            helper.getView(R.id.tv_answer).setSelected(false);
        }
        helper.setText(R.id.tv_answer, item.answerInfo);
    }
}
