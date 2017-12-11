package com.example.han.referralproject.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.LoginActivity;
import com.example.han.referralproject.adapter.ChangeAccountAdapter;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.medlink.danbogh.signin.SignInActivity;

import java.util.ArrayList;


public class ChangeAccountDialog extends Dialog implements View.OnClickListener{
    private RecyclerView mRecyclerView;
    private ChangeAccountAdapter mChangeAccountAdapter;
    private ArrayList<UserInfoBean> mDataList = new ArrayList<>();
    private Context mContext;

    public ChangeAccountDialog(Context context){
        super(context, R.style.XDialog);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_change_account);
        mRecyclerView = (RecyclerView) findViewById(R.id.rl_account);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mChangeAccountAdapter = new ChangeAccountAdapter(mContext, mDataList);
        mRecyclerView.setAdapter(mChangeAccountAdapter);
        findViewById(R.id.view_login).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
        String[] mAccountIds = LocalShared.getInstance(mContext).getAccounts();
        StringBuilder mAccountIdBuilder = new StringBuilder();
        for (String item : mAccountIds){
            mAccountIdBuilder.append(item).append(",");
        }
        NetworkApi.getAllUsers(mAccountIdBuilder.substring(0, mAccountIdBuilder.length() - 1), mListener);
    }

    private NetworkManager.SuccessCallback<ArrayList<UserInfoBean>> mListener = new NetworkManager.SuccessCallback<ArrayList<UserInfoBean>>() {
        @Override
        public void onSuccess(ArrayList<UserInfoBean> response) {
            if (response == null){
                return;
            }
            mDataList.addAll(response);
            mChangeAccountAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_login:
                mContext.startActivity(new Intent(mContext, SignInActivity.class));
                ((Activity)mContext).finish();
                break;
            case R.id.btn_logout:
                LocalShared.getInstance(mContext).loginOut();
                mContext.startActivity(new Intent(mContext, SignInActivity.class));
                ((Activity)mContext).finish();
//                if (mDataList == null){
//                    return;
//                }
//                if (mDataList.size() == 1){
//                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
//                    ((Activity)mContext).finish();
//                    break;
//                } else {
//                    for (UserInfoBean itemBean : mDataList){
//                        if (!itemBean.bid.equals(MyApplication.getInstance().userId)){
//                            LocalShared.getInstance(mContext).loginOut();
//                            MyApplication.getInstance().userId = itemBean.bid;
//                            LocalShared.getInstance(mContext).setUserInfo(itemBean);
//                            mContext.sendBroadcast(new Intent("change_account"));
//                        }
//                    }
//                }
//                break;
        }
    }

}
