package com.gcml.common.recommend.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.business.R;
import com.gcml.common.imageloader.ImageLoader;
import com.gcml.common.recommend.bean.get.GoodBean;


import java.util.List;

/**
 * Created by lenovo on 2018/9/19.
 */

public class RecommendRorMarketAdapter extends BaseQuickAdapter<GoodBean, BaseViewHolder> {
    public RecommendRorMarketAdapter(int layoutResId, @Nullable List<GoodBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder holder, GoodBean bean) {
        holder.setText(R.id.tv_good_name, bean.goodsname);
        holder.setText(R.id.tv_good_price, bean.goodsprice + "");

        ImageLoader.with(holder.getView(R.id.iv_good_image).getContext())
                .load(bean.goodsimage)
                .into(holder.getView(R.id.iv_good_image));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showLong(holder.getPosition() + "");
                if (listener != null) {
                    listener.onlick(bean, holder.getPosition());
                }

                CC.obtainBuilder("gcml.market.good.detail")
                        .addParam("goods", bean).build().call();
            }
        });

    }

    interface ClickListener {
        void onlick(GoodBean bean, int position);
    }

    public void setListener(ClickListener listener) {
        this.listener = listener;
    }

    public ClickListener listener;


}
