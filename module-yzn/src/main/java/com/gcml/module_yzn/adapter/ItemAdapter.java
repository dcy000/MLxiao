package com.gcml.module_yzn.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.module_yzn.R;
import com.gcml.module_yzn.bean.OutBean;

import java.util.List;


public class ItemAdapter extends BaseQuickAdapter<OutBean.LinksBean.DataBean, BaseViewHolder> {
    public ItemAdapter(int layoutResId, @Nullable List<OutBean.LinksBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OutBean.LinksBean.DataBean item) {
        helper.setText(R.id.name, item.title);
    }

}
