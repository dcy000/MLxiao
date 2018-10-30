package com.example.han.referralproject.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.han.referralproject.R;
import com.example.han.referralproject.cc.CCHealthMeasureActions;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.call2.NimAccountHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.schedulers.Schedulers;

public class ChangeUserActivity extends AppCompatActivity {

    TranslucentToolBar mToolBar;
    RecyclerView mRecycler;
    ChangeUserAdapter mAdapter;
    List<UserEntity> mList = new ArrayList<>();
    Disposable mDisposable = Disposables.empty();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);
        // 解决ScrollView默认位置不是最顶部
        getWindow().getDecorView().setFocusable(true);
        getWindow().getDecorView().setFocusableInTouchMode(true);
        getWindow().getDecorView().requestFocus();

        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_change_user);
        mRecycler = findViewById(R.id.rv_change_user);
    }

    private void bindData() {
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人，请选择用户进行测量。", false);
        mToolBar.setData("选 择 用 户", com.gcml.task.R.drawable.common_btn_back, "返回", com.gcml.task.R.drawable.common_btn_home, null, new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                CC.obtainBuilder("app").setActionName("ToMainActivity").build().callAsync();
                finish();
            }
        });
        mRecycler.setLayoutManager(new LinearLayoutManager(ChangeUserActivity.this, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new ChangeUserAdapter(R.layout.item_change_user, mList);
        mRecycler.setAdapter(mAdapter);
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
                        mList = users;
                        mAdapter.addData(users);
                    }
                });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserEntity user = mList.get(position);
                String userId = user.id;
                NimAccountHelper.getInstance().logout();

                // Token 1.0
                UserSpHelper.setUserId(userId);
                UserSpHelper.setEqId(user.deviceId);
                sendBroadcast(new Intent("change_account"));
                CCHealthMeasureActions.jump2MeasureChooseDeviceActivity();
                finish();
            }
        });
    }
}
