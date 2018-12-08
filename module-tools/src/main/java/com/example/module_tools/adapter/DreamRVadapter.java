package com.example.module_tools.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.module_tools.R;
import com.example.module_tools.bean.DreamBean;

import java.util.List;

/**
 * Created by lenovo on 2018/3/2.
 */

public class DreamRVadapter extends BaseQuickAdapter<DreamBean, BaseViewHolder> {
    public DreamRVadapter(int layoutResId, @Nullable List<DreamBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DreamBean bean) {
        helper.setText(R.id.dream_title, bean.name);
        helper.setText(R.id.item_dream_yuyi, bean.content);
    }

}
