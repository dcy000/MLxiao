package com.gcml.old.auth.personal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.example.han.referralproject.imageview.CircleImageView;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.imageloader.ImageLoader;
import com.medlink.danbogh.call2.NimAccountHelper;

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
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return mUserData == null ? 0 : mUserData.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        public TextView mNameView;
        public CircleImageView mHeaderIv;

        public MyHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            mNameView = view.findViewById(R.id.tv_name);
            mHeaderIv = view.findViewById(R.id.iv_header);
        }

        public void onBind(int position) {
            final UserEntity user = mUserData.get(position);
            mNameView.setText(user.name);
            ImageLoader.with(mHeaderIv)
                    .load(user.avatar)
                    .placeholder(R.drawable.common_ic_avatar_placeholder)
                    .error(R.drawable.common_ic_avatar_placeholder)
                    .into(mHeaderIv);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            UserEntity user = mUserData.get(position);
            String userId = user.id;
            NimAccountHelper.getInstance().logout();

            // Token 1.0
            UserSpHelper.setUserId(userId);
            UserSpHelper.setEqId(user.deviceId);
            mContext.sendBroadcast(new Intent("change_account"));
            CC.obtainBuilder("com.gcml.zzb.common.push.setTag")
                    .addParam("userId", user.id)
                    .build()
                    .callAsync();

            // Token 2.0
//            Observable<UserEntity> rxUser = CC.obtainBuilder("com.gcml.auth.refreshToken")
//                    .addParam("userId", userId)
//                    .build()
//                    .call()
//                    .getDataItem("data");
//            rxUser.subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new DefaultObserver<UserEntity>() {
//                        @Override
//                        public void onNext(UserEntity user) {
//                            mContext.sendBroadcast(new Intent("change_account"));
//                            CC.obtainBuilder("com.gcml.zzb.common.push.setTag")
//                                    .addParam("userId", user.id)
//                                    .build()
//                                    .callAsync();
//                        }
//
//                        @Override
//                        public void onError(Throwable throwable) {
//                            super.onError(throwable);
//                        }
//                    });
        }
    }
}
