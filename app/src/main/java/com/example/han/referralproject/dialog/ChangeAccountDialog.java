package com.example.han.referralproject.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.WelcomeActivity;
import com.example.han.referralproject.activity.ChooseLoginTypeActivity;
import com.example.han.referralproject.adapter.ChangeAccountAdapter;
import com.example.han.referralproject.application.MyApplication;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.facerecognition.DeleteGroupListener;
import com.example.han.referralproject.facerecognition.FaceAuthenticationUtils;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.iflytek.cloud.IdentityResult;
import com.iflytek.cloud.SpeechError;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.medlink.danbogh.signin.SignInActivity;
import com.umeng.analytics.MobclickAgent;
import com.medlink.danbogh.utils.JpushAliasUtils;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;


public class ChangeAccountDialog extends Dialog implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private ChangeAccountAdapter mChangeAccountAdapter;
    private ArrayList<UserInfoBean> mDataList = new ArrayList<>();
    private Context mContext;

    public ChangeAccountDialog(Context context) {
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
        StringBuilder userIds = new StringBuilder();
        for (String item : mAccountIds) {
            userIds.append(item.split(",")[0]).append(",");
        }
        NetworkApi.getAllUsers(userIds.substring(0, userIds.length() - 1), mListener);
        //手动删除组
        findViewById(R.id.deleteGroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceAuthenticationUtils.getInstance(mContext).
                        deleteGroup(LocalShared.getInstance(mContext).getGroupId(), LocalShared.getInstance(mContext).getGroupFirstXfid());
                FaceAuthenticationUtils.getInstance(mContext).setOnDeleteGroupListener(deleteGroupListener);
            }
        });
    }

    private NetworkManager.SuccessCallback<ArrayList<UserInfoBean>> mListener = new NetworkManager.SuccessCallback<ArrayList<UserInfoBean>>() {
        @Override
        public void onSuccess(ArrayList<UserInfoBean> response) {
            if (response == null) {
                return;
            }
            mDataList.addAll(response);
            mChangeAccountAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onClick(View v) {
        new JpushAliasUtils(mContext).deleteAlias();
        //删除在MainActivity中的组
//        FaceAuthenticationUtils.getInstance(mContext).
//                deleteGroup(LocalShared.getInstance(mContext).getGroupId(), LocalShared.getInstance(mContext).getGroupFirstXfid());
//        FaceAuthenticationUtils.getInstance(mContext).setOnDeleteGroupListener(deleteGroupListener);
        switch (v.getId()) {

            case R.id.view_login://添加账号

                mContext.startActivity(new Intent(mContext, SignInActivity.class));
                ((Activity) mContext).finish();

                break;
            case R.id.btn_logout:
                MobclickAgent.onProfileSignOff();
                NimAccountHelper.getInstance().logout();//退出网易IM
                //LocalShared.getInstance(mContext).deleteAccount(MyApplication.getInstance().userId,MyApplication.getInstance().xfid);//删除当前这个人的账号
                LocalShared.getInstance(mContext).loginOut();
                mContext.startActivity(new Intent(mContext, ChooseLoginTypeActivity.class));
                ((Activity) mContext).finish();
                break;
        }
    }

    private DeleteGroupListener deleteGroupListener = new DeleteGroupListener() {
        @Override
        public void onResult(IdentityResult result, boolean islast) {
            Log.e("删除成功", "onResult: ");
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
//            Log.e("删除", "onEvent:第一个参数 "+eventType+"第二个参数"+arg1+"第三个参数"+arg2+"第四个参数"+obj.toString());
        }

        @Override
        public void onError(SpeechError error) {
            Log.e("删除失败", "onError: " + error.getPlainDescription(true));
        }
    };
}
