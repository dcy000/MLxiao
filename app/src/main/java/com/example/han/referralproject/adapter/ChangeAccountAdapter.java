package com.example.han.referralproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.util.LocalShared;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChangeAccountAdapter extends RecyclerView.Adapter<ChangeAccountAdapter.MyHolder> {
    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<UserInfoBean> mUserData;

    public ChangeAccountAdapter(Context context, ArrayList<UserInfoBean> userData){
        mUserData = userData;
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public ChangeAccountAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(mInflater.inflate(R.layout.item_account_layout, null));
    }

    @Override
    public void onBindViewHolder(ChangeAccountAdapter.MyHolder holder, final int position) {
        final UserInfoBean itemBean = mUserData.get(position);
        holder.mNameView.setText(itemBean.bname);
        Picasso.with(mContext)
                .load(itemBean.user_photo)
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .tag(this)
                .fit()
                .into(holder.mHeaderIv);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NimAccountHelper.getInstance().logout();
                MyApplication.getInstance().userId = itemBean.bid;
                MyApplication.getInstance().xfid=itemBean.xfid;
                MyApplication.getInstance().eqid=itemBean.eqid;
                LocalShared.getInstance(mContext).setUserInfo(itemBean);
                LocalShared.getInstance(mContext).setSex(itemBean.sex);
                LocalShared.getInstance(mContext).setUserPhoto(itemBean.user_photo);
                LocalShared.getInstance(mContext).setUserAge(itemBean.age);
                mContext.sendBroadcast(new Intent("change_account"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUserData == null ? 0 : mUserData.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public TextView mNameView;
        public CircleImageView mHeaderIv;

        public MyHolder(View view){
            super(view);
            mNameView = (TextView) view.findViewById(R.id.tv_name);
            mHeaderIv = (CircleImageView) view.findViewById(R.id.iv_header);
        }
    }
}
