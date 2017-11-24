package com.example.han.referralproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.personal.PersonActivity;
import com.example.han.referralproject.util.LocalShared;
import com.squareup.picasso.Picasso;

public class ChangeAccountAdapter extends RecyclerView.Adapter<ChangeAccountAdapter.MyHolder> {
    private String[] mAccountIds;
    private LayoutInflater mInflater;
    private Context mContext;

    public ChangeAccountAdapter(Context context){
        mAccountIds = LocalShared.getInstance(context).getAccounts();
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public ChangeAccountAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(mInflater.inflate(R.layout.item_account_layout, null));
    }

    @Override
    public void onBindViewHolder(ChangeAccountAdapter.MyHolder holder, final int position) {
        holder.mIdView.setText(mAccountIds[position]);
//        Picasso.with(mContext)
//                .load(response.getuser_photo())
//                .placeholder(R.drawable.avatar_placeholder)
//                .error(R.drawable.avatar_placeholder)
//                .tag(this)
//                .fit()
//                .into(mImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().userId = mAccountIds[position];
                UserInfoBean itemBean = new UserInfoBean();
                itemBean.bid = mAccountIds[position];
                LocalShared.getInstance(mContext).setUserInfo(itemBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAccountIds == null ? 0 : mAccountIds.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView mIdView;
        public CircleImageView mHeaderIv;

        public MyHolder(View view){
            super(view);
            mIdView = (TextView) view.findViewById(R.id.tv_id);
            mHeaderIv = (CircleImageView) view.findViewById(R.id.iv_header);
        }
    }
}
