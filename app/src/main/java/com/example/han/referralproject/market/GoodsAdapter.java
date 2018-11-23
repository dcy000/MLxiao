package com.example.han.referralproject.market;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.GoodsBean;
import com.gzq.lib_core.base.Box;

import java.util.List;

/**
 * Created by gzq on 2018/1/5.
 */

public class GoodsAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder> {
    public GoodsAdapter(int layoutResId, @Nullable List<GoodsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBean item) {
        helper.setText(R.id.name, item.getName());
        helper.setText(R.id.price, "￥ " + item.getPrice());
        Glide.with(Box.getApp())
                .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder))
                .load(item.getImg())
                .into((ImageView) helper.getView(R.id.img));
    }
}
