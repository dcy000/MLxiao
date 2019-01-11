package com.gcml.module_auth_hospital.ui.register;


import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class IDCardRegisterInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private TranslucentToolBar authRegisterInfoTb;
    private TextView etRegisterName;
    private TextView etRegisterMinzu;
    private TextView etRegisterIdcrad;
    private TextView etRegisterNowAddress;
    private TextView etRegisterDetailAddress;
    private TextView tvAuthNext;
    private TextView tvMan;
    private TextView woman;

    private String name;
    private String gender;
    private String nation;
    private String number;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);
        initExtra();
        initView();
    }

    private void initExtra() {
        Intent data = getIntent();
        if (data != null) {
            name = data.getStringExtra(ScanIdCardRegisterActivity.REGISTER_REAL_NAME);
            gender = data.getStringExtra(ScanIdCardRegisterActivity.REGISTER_SEX);
            nation = data.getStringExtra(ScanIdCardRegisterActivity.REGISTER_IDCARD_MINZU);
            number = data.getStringExtra(ScanIdCardRegisterActivity.REGISTER_IDCARD_NUMBER);
            address = data.getStringExtra(ScanIdCardRegisterActivity.REGISTER_ADDRESS);
        }
    }

    private void initView() {
        authRegisterInfoTb = findViewById(R.id.auth_register_info_tb);
        etRegisterName = findViewById(R.id.et_register_name);
        etRegisterMinzu = findViewById(R.id.et_register_minzu);
        etRegisterIdcrad = findViewById(R.id.et_register_idcrad);
        etRegisterNowAddress = findViewById(R.id.et_register_now_address);
        etRegisterDetailAddress = findViewById(R.id.et_register_detail_address);
        etRegisterDetailAddress = findViewById(R.id.et_register_detail_address);
        tvMan = findViewById(R.id.auth_tv_man);
        woman = findViewById(R.id.auth_tv_woman);
        tvAuthNext = findViewById(R.id.tv_auth_next);
        tvAuthNext.setOnClickListener(this);

        etRegisterName.setText(name);
        etRegisterMinzu.setText(nation);
        etRegisterIdcrad.setText(number);
        etRegisterNowAddress.setText(address);
        setGender();
        authRegisterInfoTb.setData("账 号 注 册",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {

                    }
                });


        RxUtils.rxWifiLevel(getApplication(), 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        authRegisterInfoTb.setImageLevel(integer);
                    }
                });
    }

    private void setGender() {
        Drawable drawable = getResources().getDrawable(R.drawable.ic_select);
        drawable.setBounds(0, 0, 56, 56);
        Drawable drawable1 = getResources().getDrawable(R.drawable.ic_not_select);
        drawable1.setBounds(0, 0, 56, 56);
        if (TextUtils.equals("男", gender)) {
            tvMan.setCompoundDrawables(drawable, null, null, null);
            woman.setCompoundDrawables(drawable1, null, null, null);

        } else if (TextUtils.equals("女", gender)) {
            tvMan.setCompoundDrawables(drawable1, null, null, null);
            woman.setCompoundDrawables(drawable, null, null, null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_auth_next:

                break;
        }
    }
}
