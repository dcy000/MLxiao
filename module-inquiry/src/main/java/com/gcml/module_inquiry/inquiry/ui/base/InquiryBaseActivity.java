package com.gcml.module_inquiry.inquiry.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.gcml.common.App;
import com.gcml.common.base.BaseActivity;

/**
 * Created by lenovo on 2019/3/21.
 */

public abstract class InquiryBaseActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        initView();
    }

    private void intentTo(Class<? extends AppCompatActivity> tClass) {
        startActivity(new Intent(this, tClass));
    }

    private void intentWithTo(Class<? extends AppCompatActivity> tClass, Bundle data) {
        Intent intent = new Intent(this, tClass);
        intent.putExtras(data);
        startActivity(intent);
    }


    protected abstract int layoutId();

    protected abstract void initView();


}
