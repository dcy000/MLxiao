package com.gcml.module_auth_hospital.ui.register;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class IDCardNumberRegisterInfoActivity extends AppCompatActivity implements View.OnClickListener {


    private TranslucentToolBar translucentToolBar;
    private String name;
    private String gender;
    private String nation;
    private String number;
    private String address;
    private String fromWhere;
    /**
     * 姓名
     */
    private TextView tvRegisterName;
    /**
     * 民族
     */
    private TextView tvRegisterMinzu;
    /**
     * 请输入您的姓名
     */
    private EditText etRegisterName;
    /**
     * 请输入您的民族
     */
    private EditText etRegisterMinzu;
    /**
     * 性别
     */
    private TextView textView6tvRegisterSex;
    /**
     * 身份证号码
     */
    private TextView tvRegisterIdcard;
    /**
     * 男
     */
    private TextView authTvMan;
    /**
     * 女
     */
    private TextView authTvWoman;
    /**
     * 请输入您的身份证号码
     */
    private TextView etRegisterIdcrad;
    /**
     * 现住址
     */
    private TextView tvRegisterNowAddress;
    /**
     * 详细地址
     */
    private TextView tvRegisterDetailAddress;
    /**
     * 请输入您的现住址
     */
    private TextView etRegisterNowAddress;
    /**
     * 请输入您的详细地址
     */
    private EditText etRegisterDetailAddress;
    /**
     * 下一步
     */
    private TextView tvAuthNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard_number_register_info);
        initExtra();
        initView();
    }

    private void initExtra() {
        Intent data = getIntent();
        if (data != null) {
            number = data.getStringExtra(ScanIdCardRegisterActivity.REGISTER_IDCARD_NUMBER);
        }
    }

    private void initView() {
        translucentToolBar = (TranslucentToolBar) findViewById(R.id.auth_idcard_numer_register_info_tb);
        tvRegisterName = (TextView) findViewById(R.id.tv_register_name);
        tvRegisterMinzu = (TextView) findViewById(R.id.tv_register_minzu);
        etRegisterName = findViewById(R.id.et_register_name);
        etRegisterMinzu = (EditText) findViewById(R.id.et_register_minzu);
        etRegisterMinzu.setOnClickListener(this);
        textView6tvRegisterSex = (TextView) findViewById(R.id.textView6tv_register_sex);
        tvRegisterIdcard = (TextView) findViewById(R.id.tv_register_idcard);
        authTvMan = (TextView) findViewById(R.id.auth_tv_man);
        authTvMan.setOnClickListener(this);
        authTvWoman = (TextView) findViewById(R.id.auth_tv_woman);
        authTvWoman.setOnClickListener(this);
        etRegisterIdcrad = findViewById(R.id.et_register_idcrad);
        tvRegisterNowAddress = (TextView) findViewById(R.id.tv_register_now_address);
        tvRegisterDetailAddress = (TextView) findViewById(R.id.tv_register_detail_address);
        etRegisterNowAddress = findViewById(R.id.et_register_now_address);
        etRegisterNowAddress.setOnClickListener(this);
        etRegisterDetailAddress = (EditText) findViewById(R.id.et_register_detail_address);
        tvAuthNext = (TextView) findViewById(R.id.tv_auth_next);
        tvAuthNext.setOnClickListener(this);

        translucentToolBar.setData("账 号 注 册",
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
                        translucentToolBar.setImageLevel(integer);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.et_register_minzu:
                break;
            case R.id.auth_tv_man:
                break;
            case R.id.auth_tv_woman:
                break;
            case R.id.et_register_now_address:
                break;
            case R.id.tv_auth_next:
                break;
        }
    }
}
