package com.gcml.common.recommend.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.business.R;
import com.gcml.common.recommend.bean.get.GoodBean;
import com.gcml.common.repository.imageloader.ImageLoader;
import com.gcml.lib_utils.display.ToastUtils;

import java.util.List;

/**
 * Created by lenovo on 2018/9/19.
 */

public class RecommendAdapter extends BaseQuickAdapter<GoodBean, BaseViewHolder> {
    public RecommendAdapter(int layoutResId, @Nullable List<GoodBean> data) {
        super(layoutResId, data);

    }

    @Override
    protected void convert(BaseViewHolder holder, GoodBean bean) {
        holder.setText(R.id.tv_good_name, bean.goodsname);
        holder.setText(R.id.tv_good_price, bean.goodsprice + "");

        ImageLoader.Options options = ImageLoader.newOptionsBuilder(
                holder.getView(R.id.iv_good_image), bean.goodsimage).build();
        ImageLoader.instance().load(options);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showLong(holder.getPosition() + "");
            }
        });

    }

}
