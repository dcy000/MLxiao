package com.example.han.referralproject.inquiry.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.example.han.referralproject.healthmanage.HealthManageActivity;
import com.example.han.referralproject.healthmanage.HealthManageTipActivity;
import com.gcml.common.IConstant;
import com.gcml.common.base.BaseActivity;
import com.gcml.common.data.UserEntity;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.app.ActivityHelper;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.gcml.common.IConstant.KEY_BIND_DOCTOR;
import static com.gcml.common.IConstant.KEY_INQUIRY;

public class InquiryEntranceActivity extends BaseActivity implements View.OnClickListener {

    private com.gcml.common.widget.toolbar.TranslucentToolBar tb_inquiry_home;
    private RelativeLayout rl_inquiry_home_manage;
    private RelativeLayout rl_inquiry_home_file;
    private RelativeLayout rl_inquiry_home_home;

    private boolean bindDoctor;
    private boolean bindWacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_entrance);
        bindViews();
        getFileInfo();
        ActivityHelper.finishAll();
    }

    private void getFileInfo() {
    }


    @Override
    protected void onResume() {
        super.onResume();
        bindData();
    }

    private void bindData() {
        Observable<UserEntity> data = CC.obtainBuilder(IConstant.KEY_GET_USER_INFO)
                .build()
                .call()
                .getDataItem("data");
        data.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("");
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        if (user != null && user.doctorId != null) {
                            bindDoctor = true;
                        }
                        if (!TextUtils.isEmpty(user.watchCode)) {
                            bindWacher = true;
                        } else {
                            bindWacher = false;
                        }
                        dismissLoading();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                        dismissLoading();
                    }
                });
    }

    private void bindViews() {
        tb_inquiry_home = (com.gcml.common.widget.toolbar.TranslucentToolBar) findViewById(R.id.tb_inquiry_home);
        rl_inquiry_home_manage = (RelativeLayout) findViewById(R.id.rl_inquiry_home_manage);
        rl_inquiry_home_file = (RelativeLayout) findViewById(R.id.rl_inquiry_home_file);
        rl_inquiry_home_home = (RelativeLayout) findViewById(R.id.rl_inquiry_home_home);

        rl_inquiry_home_manage.setOnClickListener(this);
        rl_inquiry_home_file.setOnClickListener(this);
        rl_inquiry_home_home.setOnClickListener(this);

        tb_inquiry_home.setData("健 康 管 理",
                0, null,
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {

                    }

                    @Override
                    public void onRightClick() {
                        onRightClickWithPermission(new IAction() {
                            @Override
                            public void action() {
                                CC.obtainBuilder("com.gcml.old.setting").build().call();
                            }
                        });
                    }
                });

        setWifiLevel(tb_inquiry_home);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_inquiry_home_manage) {
            if (bindWacher) {
                startActivity(new Intent(this, HealthManageActivity.class));
            } else {
                startActivity(new Intent(this, HealthManageTipActivity.class));
            }
        } else if (id == R.id.rl_inquiry_home_file) {
//            if (bindDoctor) {
//                CC.obtainBuilder("health.profile.file").addParam("fromPage", "qianyue").build().callAsync();
//            } else {
//                CC.obtainBuilder(KEY_BIND_DOCTOR).build().callAsync();
//            }

            CC.obtainBuilder(KEY_INQUIRY).build().callAsync();


        } else if (id == R.id.rl_inquiry_home_home) {
            CC.obtainBuilder("com.gcml.old.home").build().callAsync();
        }
    }
}
