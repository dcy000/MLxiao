package com.gcml.old.auth.personal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.DefaultObserver;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;


public class ChangeAccountDialog extends Dialog implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private ChangeAccountAdapter mChangeAccountAdapter;
    private ArrayList<UserEntity> mDataList = new ArrayList<>();
    private Context mContext;
    private Disposable mDisposable = Disposables.empty();

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

        Observable<List<UserEntity>> rxUsers = CC.obtainBuilder("com.gcml.auth.getUsers")
                .build()
                .call()
                .getDataItem("data");
        mDisposable = rxUsers.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(Observable.just(Collections.emptyList()))
                .subscribeWith(new DefaultObserver<List<UserEntity>>() {
                    @Override
                    public void onNext(List<UserEntity> users) {
                        mDataList.addAll(users);
                        mChangeAccountAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        CC.obtainBuilder("com.gcml.zzb.common.push.deleteTag")
                .build()
                .callAsync();
        UserSpHelper.clearPartData();
        switch (v.getId()) {
            case R.id.view_login://添加账号
                CC.obtainBuilder("com.gcml.auth").build().callAsync();
                ((Activity) mContext).finish();
                break;
            case R.id.btn_logout:
                MobclickAgent.onProfileSignOff();
                NimAccountHelper.getInstance().logout();//退出网易IM
                UserSpHelper.setToken("");
                UserSpHelper.setEqId("");
                UserSpHelper.setUserId("");
                CC.obtainBuilder("com.gcml.auth").build().callAsync();
                ((Activity) mContext).finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDisposable.dispose();
    }
}
