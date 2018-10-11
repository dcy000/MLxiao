package com.gcml.mall.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gcml.common.utils.Utils;
import com.gcml.mall.R;
import com.gcml.mall.bean.OrderBean;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderListAdapter extends BaseQuickAdapter<OrderBean,BaseViewHolder> {

    private Context mContext;

    public OrderListAdapter(@Nullable List<OrderBean> data, Context context) {
        super(R.layout.item_order_list, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderBean item) {
        Picasso.with(mContext)
                .load(item.photo)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .tag(mContext)
                .fit()
                .into((ImageView) helper.getView(R.id.iv_order_image));
        String date = Utils.getDateToString(Long.parseLong(item.time), "yyyy-MM-dd");
        double price = Double.parseDouble(item.price) / Integer.parseInt(item.number);
        helper.setText(R.id.tv_order_time, date)
                .setText(R.id.tv_order_id, "订单号：" + item.orderid)
                .setText(R.id.tv_order_status, item.delivery_state)
                .setText(R.id.tv_order_name, item.articles)
                .setText(R.id.tv_order_price, String.format("%.2f", price))
                .setText(R.id.tv_order_mount, item.number)
                .setText(R.id.tv_order_tag, "共" + item.number + "件商品 ¥" + String.format("%.2f", Double.parseDouble(item.price)));
    }

}
