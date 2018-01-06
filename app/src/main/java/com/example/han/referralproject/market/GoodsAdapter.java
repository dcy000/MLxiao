package com.example.han.referralproject.market;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.GoodsBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by gzq on 2018/1/5.
 */

public class GoodsAdapter extends BaseQuickAdapter<GoodsBean,BaseViewHolder>{
    public GoodsAdapter(int layoutResId, @Nullable List<GoodsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBean item) {
        helper.setText(R.id.name,item.getName());
        helper.setText(R.id.price,"￥ "+item.getPrice());
        Picasso.with(mContext)
                .load(item.getImg())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .tag(mContext)
                .fit()
                .into((ImageView) helper.getView(R.id.img));
    }
}
