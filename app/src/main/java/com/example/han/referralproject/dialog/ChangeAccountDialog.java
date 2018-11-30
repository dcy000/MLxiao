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
import com.gzq.lib_core.room.UserDatabase;
import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.medlink.danbogh.signin.SignInActivity;
import com.umeng.analytics.MobclickAgent;
import com.medlink.danbogh.utils.JpushAliasUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public class ChangeAccountDialog extends Dialog implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private ChangeAccountAdapter mChangeAccountAdapter;
    private ArrayList<UserInfoBean> mDataList = new ArrayList<>();
    private Context mContext;
    private List<String> oldUserIds = new ArrayList<>();
    private List<String> newUserIds = new ArrayList<>();

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
                List<UserInfoBean> users = Box.getUsersFromRoom();
                if (users == null) {
                    emitter.onError(new Throwable("未检测到您的登录历史，请输入账号和密码登录"));
                    return;
                }
                StringBuilder userIds = new StringBuilder();
                for (UserInfoBean user : users) {
                    oldUserIds.add(user.bid);
                    userIds.append(user.bid + ",");
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
                .doOnNext(new Consumer<ArrayList<UserInfoBean>>() {
                    @Override
                    public void accept(ArrayList<UserInfoBean> userInfoBeans) throws Exception {
                        for (UserInfoBean user : userInfoBeans) {
                            newUserIds.add(user.bid);
                        }
                        //先删除存在的账号，剩下的就是后台手动注销过的账号
                        oldUserIds.removeAll(newUserIds);
                        if (oldUserIds.size() > 0) {
                            UserDatabase userDatabase = Box.getRoomDataBase(UserDatabase.class);
                            for (String id : oldUserIds) {
                                userDatabase.userDao().deleteById(id);
                            }
                        }
                    }
                })
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
                KVUtils.clear();
                Box.getSessionManager().clear();
                //退出友盟
                MobclickAgent.onProfileSignOff();
                //退出网易IM
                NimAccountHelper.getInstance().logout();
                //清除Room中的缓存
                UserDatabase userDatabase = Box.getRoomDataBase(UserDatabase.class);
                userDatabase.userDao().deleteById(Box.getUserId());
                //回退到登录页面
//                LocalShared.getInstance(mContext).loginOut();
                mContext.startActivity(new Intent(mContext, ChooseLoginTypeActivity.class));
                ((Activity) mContext).finish();
                break;
        }
    }
}
