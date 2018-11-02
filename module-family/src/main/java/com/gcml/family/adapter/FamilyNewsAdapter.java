package com.gcml.family.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.family.R;
import com.gcml.family.bean.NewsBean;

import java.util.List;

public class FamilyNewsAdapter extends BaseQuickAdapter<NewsBean, BaseViewHolder> {

    public FamilyNewsAdapter(int layoutResId, @Nullable List<NewsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsBean item) {
        helper.setText(R.id.tv_family_msg, item.msg);
        if (item.type == 0) {

        } else if (item.type == 1) {

        } else if (item.type == 2) {

        } else if (item.type == 3) {

        }
    }
}
