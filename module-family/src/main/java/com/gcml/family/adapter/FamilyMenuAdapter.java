package com.gcml.family.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.family.R;
import com.gcml.family.bean.FamilyBean;


import java.util.List;

public class FamilyMenuAdapter extends BaseQuickAdapter<FamilyBean, BaseViewHolder> {

    public FamilyMenuAdapter(int layoutResId, @Nullable List<FamilyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FamilyBean item) {
        helper.setText(R.id.tv_family_name, item.name);
        helper.setText(R.id.tv_family_tag, item.tag);
    }
}
