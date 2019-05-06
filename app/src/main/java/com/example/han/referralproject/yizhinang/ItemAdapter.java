package com.example.han.referralproject.yizhinang;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.shopping.Goods;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by afirez on 18-1-6.
 */

public class ItemAdapter extends BaseQuickAdapter<OutBean.LinksBean.DataBean, BaseViewHolder> {
    public ItemAdapter(int layoutResId, @Nullable List<OutBean.LinksBean.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OutBean.LinksBean.DataBean item) {
        helper.setText(R.id.name, item.title);
    }

}
