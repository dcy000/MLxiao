package com.example.han.referralproject.market;

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

public class Goods1Adapter extends BaseQuickAdapter<Goods,BaseViewHolder> {
    public Goods1Adapter(int layoutResId, @Nullable List<Goods> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods goods) {
        helper.setText(R.id.name,goods.goodsname);
        helper.setText(R.id.price,"￥ "+goods.goodsprice);
        Picasso.with(mContext)
                .load(goods.goodsimage)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .tag(mContext)
                .fit()
                .into((ImageView) helper.getView(R.id.img));
    }
}
