package com.gcml.old.auth.personal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.example.han.referralproject.application.MyApplication;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.repository.imageloader.ImageLoader;
import com.gcml.old.auth.entity.UserInfoBean;
import com.example.han.referralproject.constant.ConstantData;
import com.example.han.referralproject.imageview.CircleImageView;
import com.example.han.referralproject.util.LocalShared;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChangeAccountAdapter extends RecyclerView.Adapter<ChangeAccountAdapter.MyHolder> {
    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<UserEntity> mUserData;

    public ChangeAccountAdapter(Context context, ArrayList<UserEntity> userData) {
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
        final UserEntity user = mUserData.get(position);
        holder.mNameView.setText(user.name);
        ImageLoader.with(holder.mHeaderIv)
                .load(user.avatar)
                .placeholder(R.drawable.avatar_placeholder)
                .error(R.drawable.avatar_placeholder)
                .into(holder.mHeaderIv);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = user.id;
                if (!TextUtils.isEmpty(userId)) {
                    CC.obtainBuilder("com.gcml.zzb.common.push.setTag")
                            .addParam("userId", userId)
                            .build()
                            .callAsync();
                }
                NimAccountHelper.getInstance().logout();
                UserSpHelper.setUserId(userId);
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

        public MyHolder(View view) {
            super(view);
            mNameView = view.findViewById(R.id.tv_name);
            mHeaderIv = view.findViewById(R.id.iv_header);
        }
    }

}
