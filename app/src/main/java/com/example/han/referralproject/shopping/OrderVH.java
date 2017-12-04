package com.example.han.referralproject.shopping;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;

/**
 * Created by han on 2017/11/30.
 */

public class OrderVH extends RecyclerView.ViewHolder {


    ImageView mImageView;
    TextView mTextView1;
    TextView mTextView2;
    TextView mTextView3;
    TextView mTextView4;
    TextView mTextView5;
    TextView mTextView6;
    TextView mTextView7;
    TextView mTextView8;

    public int mPosition;

    public Context mContext;

    public OrderVH(View itemView) {
        super(itemView);
    }

    public OrderVH(View itemView, final OrderAdapter.OnItemClickListener onItemClistListener) {
        super(itemView);
        mImageView = (ImageView) itemView.findViewById(R.id.order_image);
        mTextView1 = (TextView) itemView.findViewById(R.id.order_time);
        mTextView2 = (TextView) itemView.findViewById(R.id.order_id);
        mTextView3 = (TextView) itemView.findViewById(R.id.order_status);
        mTextView4 = (TextView) itemView.findViewById(R.id.order_name);
        mTextView5 = (TextView) itemView.findViewById(R.id.order_price);
        mTextView6 = (TextView) itemView.findViewById(R.id.order_mount);
        mTextView7 = (TextView) itemView.findViewById(R.id.order_mounts);
        mTextView8 = (TextView) itemView.findViewById(R.id.order_sum);


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
