package com.gcml.mall.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.mall.bean.GoodsBean;
import com.gcml.mall.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by gzq on 2018/1/5.
 */

public class MallGoodsAdapter extends BaseQuickAdapter<GoodsBean,BaseViewHolder> {

    public MallGoodsAdapter(int layoutResId, @Nullable ArrayList<GoodsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBean item) {
        helper.setText(R.id.tv_goods_name,item.name);
        helper.setText(R.id.tv_goods_price,"¥"+item.price);
        Picasso.with(mContext)
                .load(item.img)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .tag(mContext)
                .fit()
                .into((ImageView) helper.getView(R.id.iv_goods_icon));
    }
}
