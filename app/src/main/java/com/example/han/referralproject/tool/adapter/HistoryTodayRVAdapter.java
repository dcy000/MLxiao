package com.example.han.referralproject.tool.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.tool.ExpandableTextView;
import com.example.han.referralproject.tool.xfparsebean.HistoryTodayBean;

import java.util.List;

/**
 * Created by lenovo on 2018/3/7.
 */

public class HistoryTodayRVAdapter extends BaseQuickAdapter<HistoryTodayBean, BaseViewHolder> {
    public HistoryTodayRVAdapter(int layoutResId, @Nullable List<HistoryTodayBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, HistoryTodayBean bean) {
        holder.setText(R.id.tv_content, bean.description);
        holder.setText(R.id.tv_title, bean.title);
        ((ExpandableTextView)holder.getView(R.id.expandableTextView)).isOpen=bean.flag;
    }
}
