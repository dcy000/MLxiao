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
import com.example.han.referralproject.activity.ChooseLoginTypeActivity;
import com.example.han.referralproject.adapter.ChangeAccountAdapter;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.service.API;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.auth.face.ui.FaceSignInActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.model.HttpResult;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.medlink.danbogh.signin.SignInActivity;
import com.umeng.analytics.MobclickAgent;
import com.medlink.danbogh.utils.JpushAliasUtils;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;


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
        getUsers();
    }

    private void getUsers() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String[] mAccountIds = LocalShared.getInstance(mContext).getAccounts();

                if (mAccountIds == null) {
                    emitter.onError(new Throwable("未检测到您的登录历史，请输入账号和密码登录"));
                    return;
                }
                StringBuilder userIds = new StringBuilder();
                for (String item : mAccountIds) {
                    userIds.append(item.split(",")[0]).append(",");
                }
                String substring = userIds.substring(0, userIds.length() - 1);
                emitter.onNext(substring);
            }
        }).flatMap(new Function<String, ObservableSource<HttpResult<ArrayList<UserInfoBean>>>>() {
            @Override
            public ObservableSource<HttpResult<ArrayList<UserInfoBean>>> apply(String s) throws Exception {
                return Box.getRetrofit(API.class)
                        .queryAllLocalUsers(s);
            }
        }).compose(RxUtils.httpResponseTransformer())
                .subscribe(new CommonObserver<ArrayList<UserInfoBean>>() {
                    @Override
                    public void onNext(ArrayList<UserInfoBean> users) {
                        if (users != null) {
                            mDataList.addAll(users);
                            mChangeAccountAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                    }
                });
    }
    @Override
    public void onClick(View v) {
        new JpushAliasUtils(mContext).deleteAlias();
        switch (v.getId()) {
            case R.id.view_login://添加账号
                mContext.startActivity(new Intent(mContext, ChooseLoginTypeActivity.class));
                ((Activity) mContext).finish();
                break;
            case R.id.btn_logout:
                //清除Session
                Box.getSessionManager().clear();
                MobclickAgent.onProfileSignOff();
                NimAccountHelper.getInstance().logout();//退出网易IM
                LocalShared.getInstance(mContext).loginOut();
                mContext.startActivity(new Intent(mContext, ChooseLoginTypeActivity.class));
                ((Activity) mContext).finish();
                break;
        }
    }
}
