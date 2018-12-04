package com.example.module_mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.module_mall.R;
import com.example.module_mall.bean.Orders;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by han on 2017/11/30.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderVH> {

    List<Orders> mList;
    private Context context;

    public OrderAdapter(List<Orders> mListGood, Context context) {
        this.mList = mListGood;
        this.context = context;

    }

    @Override
    public OrderVH onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = View.inflate(parent.getContext(), R.layout.order_fall_item, null);
        return new OrderVH(view, onItemClistListener);
    }

    @Override
    public void onBindViewHolder(OrderVH holder, final int position) {

        holder.mContext = context;
        holder.mPosition = position;

        Glide.with(Box.getApp())
                .applyDefaultRequestOptions(new RequestOptions()
                        .placeholder(R.drawable.avatar_placeholder)
                        .error(R.drawable.avatar_placeholder))
                .load(mList.get(position).getPhoto())
                .into(holder.mImageView);


        String date = TimeUtils.milliseconds2String(Long.parseLong(mList.get(position).getTime()), new SimpleDateFormat("yyyy-MM-dd"));
        holder.mTextView1.setText(date);

        holder.mTextView2.setText("订单号：" + mList.get(position).getOrderid());

        holder.mTextView3.setText(mList.get(position).getDelivery_state());

        holder.mTextView4.setText(mList.get(position).getArticles());

        int price = Integer.parseInt(mList.get(position).getPrice()) / Integer.parseInt(mList.get(position).getNumber());

        holder.mTextView5.setText(price + "");
        holder.mTextView6.setText("×" + mList.get(position).getNumber());
        holder.mTextView7.setText("共" + mList.get(position).getNumber() + "件商品¥");

        holder.mTextView8.setText(mList.get(position).getPrice());

    }


    @Override
    public int getItemCount() {

        if (null == mList) {
            return 0;

        } else {
            return mList.size();
        }
    }


    private OrderAdapter.OnItemClickListener onItemClistListener;

    public interface OnItemClickListener {
        void onItemClick(int postion);
    }

    public void setOnItemClistListener(OrderAdapter.OnItemClickListener itemClistListener) {
        this.onItemClistListener = itemClistListener;
    }


}
