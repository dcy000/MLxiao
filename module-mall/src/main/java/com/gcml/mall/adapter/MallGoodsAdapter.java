package com.gcml.mall.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.mall.R;
import com.gcml.common.recommend.bean.get.GoodsBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by gzq on 2018/1/5.
 */

public class MallGoodsAdapter extends BaseQuickAdapter<GoodsBean,BaseViewHolder> {

    public MallGoodsAdapter(int layoutResId, @Nullable List<GoodsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBean item) {
        helper.setText(R.id.tv_goods_name,item.goodsname);
        helper.setText(R.id.tv_goods_price,"¥" + item.goodsprice);
        Picasso.with(mContext)
                .load(item.goodsimage)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .tag(mContext)
                .fit()
                .into((ImageView) helper.getView(R.id.iv_goods_icon));
    }
}
