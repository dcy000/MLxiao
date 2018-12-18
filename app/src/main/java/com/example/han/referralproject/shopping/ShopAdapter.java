package com.example.han.referralproject.shopping;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.han.referralproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by han on 2017/11/24.
 */

public class ShopAdapter extends RecyclerView.Adapter<ShopVH> {


    List<Goods> mList;
    private Context context;

    public ShopAdapter(List<Goods> mListGood, Context context) {
        this.mList = mListGood;
        this.context = context;

    }

    @Override
    public ShopVH onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = View.inflate(parent.getContext(), R.layout.water_fall_shop, null);
        return new ShopVH(view, onItemClistListener);
    }

    @Override
    public void onBindViewHolder(ShopVH holder, final int position) {

        holder.mContext = context;
        holder.mPosition = position;
        Picasso.with(context)
                .load(mList.get(position).getGoodsimage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .tag(context)
                .fit()
                .into(holder.mImageView);
        //    holder.mImagine.setImageResource(R.drawable.common_ic_avatar_placeholder);
        holder.mTextView.setText(mList.get(position).getGoodsname());
        holder.mTextView1.setText("¥ " + mList.get(position).getGoodsprice());
    }

    @Override
    public int getItemCount() {

        if (null == mList) {
            return 0;

        } else {
            return mList.size();
        }
    }


    private OnItemClickListener onItemClistListener;

    public interface OnItemClickListener {
        void onItemClick(int postion);
    }

    public void setOnItemClistListener(OnItemClickListener itemClistListener) {
        this.onItemClistListener = itemClistListener;
    }


}
