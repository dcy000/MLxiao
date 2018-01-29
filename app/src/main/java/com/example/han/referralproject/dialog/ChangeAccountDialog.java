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
import com.example.han.referralproject.WelcomeActivity;
import com.example.han.referralproject.activity.ChooseLoginTypeActivity;
import com.example.han.referralproject.adapter.ChangeAccountAdapter;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.medlink.danbogh.signin.SignInActivity;
import com.umeng.analytics.MobclickAgent;
import com.medlink.danbogh.utils.JpushAliasUtils;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;


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
        if (mAccountIds == null) {
            return;
        }
        StringBuilder mAccountIdBuilder = new StringBuilder();
        for (String item : mAccountIds){
            mAccountIdBuilder.append(item.split(",")[0]).append(",");
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
                new JpushAliasUtils(mContext).deleteAlias();
                mContext.startActivity(new Intent(mContext, SignInActivity.class));
                ((Activity)mContext).finish();
                break;
            case R.id.btn_logout:
                new JpushAliasUtils(mContext).deleteAlias();
                MobclickAgent.onProfileSignOff();
                NimAccountHelper.getInstance().logout();//退出网易IM
//                LocalShared.getInstance(mContext).loginOut();//清除SP中的信息
                MyApplication.getInstance().userId=null;
                mContext.startActivity(new Intent(mContext, ChooseLoginTypeActivity.class));
                ((Activity)mContext).finish();
                break;
        }
    }

}
