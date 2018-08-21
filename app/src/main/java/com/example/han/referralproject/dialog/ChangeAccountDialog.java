package com.example.han.referralproject.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.gcml.old.auth.signin.ChooseLoginTypeActivity;
import com.example.han.referralproject.adapter.ChangeAccountAdapter;
import com.gcml.old.auth.entity.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.medlink.danbogh.utils.JpushAliasUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;


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
        mRecyclerView = findViewById(R.id.rl_account);
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
//                //为了解决人脸识别加组缓慢的解决方式，这样做是不规范的
//                if (LocalShared.getInstance(mContext).isAccountOverflow()) {
//                    NDialog1 dialog = new NDialog1(mContext);
//                    dialog.setMessageCenter(true)
//                            .setMessage("本机登录账号数量达到上限，是否快速清理？")
//                            .setMessageSize(35)
//                            .setCancleable(false)
//                            .setButtonCenter(true)
//                            .setPositiveTextColor(mContext.getResources().getColor(R.color.toolbar_bg))
//                            .setNegativeTextColor(Color.parseColor("#999999"))
//                            .setButtonSize(40)
//                            .setOnConfirmListener(new NDialog1.OnConfirmListener() {
//                                @Override
//                                public void onClick(int which) {
//                                    if (which == 1) {
//                                        LocalShared.getInstance(mContext).deleteAllAccount();
//                                        mContext.startActivityForResult(new Intent(mContext, SignInActivity.class));
//                                        ((Activity) mContext).finish();
//                                    }
//                                }
//                            }).create(NDialog.CONFIRM).showShort();
//                }else{
//                    mContext.startActivityForResult(new Intent(mContext, SignInActivity.class));
//                    ((Activity) mContext).finish();
//                }
                CC.obtainBuilder("com.gcml.old.user.auth").build().callAsync();
//                mContext.startActivity(new Intent(mContext, ChooseLoginTypeActivity.class));
                ((Activity) mContext).finish();
                break;
            case R.id.btn_logout:
                MobclickAgent.onProfileSignOff();
                NimAccountHelper.getInstance().logout();//退出网易IM
                //LocalShared.getInstance(mContext).deleteAccount(MyApplication.getInstance().userId,MyApplication.getInstance().xfid);//删除当前这个人的账号
//                //为了解决人脸识别加组缓慢的解决方式，这样做是不规范的
//                if (LocalShared.getInstance(mContext).isAccountOverflow()) {
//                    LocalShared.getInstance(mContext).deleteAllAccount();
//                }
                LocalShared.getInstance(mContext).loginOut();
                CC.obtainBuilder("com.gcml.old.user.auth").build().callAsync();
//                mContext.startActivity(new Intent(mContext, ChooseLoginTypeActivity.class));
                ((Activity) mContext).finish();
                break;
        }
    }
}
