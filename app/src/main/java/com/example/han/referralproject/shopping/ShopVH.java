package com.example.han.referralproject.shopping;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;

/**
 * Created by han on 2017/11/24.
 */

public class ShopVH extends RecyclerView.ViewHolder {

    ImageView mImageView;
    public TextView mTextView;
    TextView mTextView1;
    public int mPosition;

    public Context mContext;

    public ShopVH(View itemView) {
        super(itemView);
    }

    public ShopVH(View itemView, final ShopAdapter.OnItemClickListener onItemClistListener) {
        super(itemView);
        mImageView = (ImageView) itemView.findViewById(R.id.shop_image);
        mTextView = (TextView) itemView.findViewById(R.id.goods_name);
        mTextView1 = (TextView) itemView.findViewById(R.id.goods_amount);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != onItemClistListener) {
                    onItemClistListener.onItemClick(mPosition);
                }
            }
        });

    }

}
