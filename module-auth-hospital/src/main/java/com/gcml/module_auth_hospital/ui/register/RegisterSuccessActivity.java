package com.gcml.module_auth_hospital.ui.register;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by lenovo on 2019/1/14.
 */

public class RegisterSuccessActivity extends AppCompatActivity implements View.OnClickListener {
    private TranslucentToolBar tbAuthRegisterSuccess;
    private TextView tvAuthRegisterSuccessComplete;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_register_success);
        initView();
        toOtherPage();
    }

    private void toOtherPage() {
        RxUtils.rxTimer(3)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(aLong -> {
                    ToastUtils.showShort("时间到,跳转到主页面");
                });


    }

    private void initView() {
        tbAuthRegisterSuccess = (TranslucentToolBar) findViewById(R.id.tb_auth_register_success);
        tvAuthRegisterSuccessComplete = (TextView) findViewById(R.id.tv_auth_register_success_complete);
        tvAuthRegisterSuccessComplete.setOnClickListener(this);

        tbAuthRegisterSuccess.setData("账 户 注 册",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_btn_home, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        CC.obtainBuilder("com.gcml.old.home");
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_auth_register_success_complete) {

        }
    }
}
