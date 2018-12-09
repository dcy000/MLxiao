package com.example.han.referralproject.person;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.module_login.ui.ChooseLoginTypeActivity;
import com.example.module_person.R;
import com.example.han.referralproject.person.adapter.ChangeAccountAdapter;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.model.HttpResult;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.room.UserDatabase;
import com.gzq.lib_core.service.CommonAPI;
import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.example.han.referralproject.util.JpushAliasUtils;

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
                return Box.getRetrofit(CommonAPI.class)
                        .queryAllLocalUsers(s);
            }
        })
                .compose(RxUtils.<ArrayList<UserInfoBean>>httpResponseTransformer())
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
        int i = v.getId();//添加账号
        if (i == R.id.view_login) {

        } else if (i == R.id.btn_logout) {//清除Session
            KVUtils.clear();
            Box.getSessionManager().clear();
        }
        ActivityUtils.skipActivity(ChooseLoginTypeActivity.class);
        ((Activity) mContext).finish();
    }
}
