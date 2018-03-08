package com.example.han.referralproject.tool.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.tool.ExpandableTextView;
import com.example.han.referralproject.tool.MixtureTextView;
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
    protected void convert(final BaseViewHolder holder, final HistoryTodayBean bean) {
        holder.setText(R.id.tv_content, bean.description);
        holder.setText(R.id.tv_title, bean.title);
        final ExpandableTextView close = holder.getView(R.id.expandableTextView);
        final MixtureTextView open = holder.getView(R.id.mt_result);
        open.setText(bean.description);
        ImageView openImg = holder.getView(R.id.img_pic);
        if (!bean.imgs.isEmpty()) {
            Glide.with(openImg.getContext()).load(bean.imgs.get(0)).into(openImg);
        }
        close.isOpen = bean.flag;
        if (bean.flag) {
            close.setVisibility(View.GONE);
            open.setVisibility(View.VISIBLE);
        } else {
            close.setVisibility(View.VISIBLE);
            open.setVisibility(View.GONE);
        }

        close.setClickListner(new ExpandableTextView.ClickListner() {
            @Override
            public void onclick(boolean isOpen) {
                getData().get(holder.getPosition()).flag = isOpen;
                if (isOpen) {
                    close.setVisibility(View.GONE);
                    open.setVisibility(View.VISIBLE);
                } else {
                    close.setVisibility(View.VISIBLE);
                    open.setVisibility(View.VISIBLE);
                }

            }
        });
    }
}
