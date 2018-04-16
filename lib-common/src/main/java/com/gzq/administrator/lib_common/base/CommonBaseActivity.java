package com.gzq.administrator.lib_common.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzq.administrator.lib_common.R;

/**
 * Created by gzq on 2018/4/12.
 */

public abstract class CommonBaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected boolean isShowToolbar=true;
    protected View mToolbar = null;
    protected TextView mTitleText;
    protected TextView mRightText;
    protected ImageView mLeftView;
    protected ImageView mRightView;
    protected TextView mLeftText;
    protected LinearLayout mllBack;
    private static final String TAG=CommonBaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        LinearLayout parent = new LinearLayout(this);
        parent.setOrientation(LinearLayout.VERTICAL);
        viewGroup.addView(parent);
        if (isShowToolbar) {
            mToolbar = LayoutInflater.from(this).inflate(R.layout.common_custom_title_layout, parent, true);
            initToolbar();
        }
        LayoutInflater.from(this).inflate(layoutResID, parent, true);
    }

    private void initToolbar() {
        mllBack = (LinearLayout) mToolbar.findViewById(R.id.ll_back);
        mToolbar = (RelativeLayout) mToolbar.findViewById(R.id.toolbar);
        mTitleText = (TextView) mToolbar.findViewById(R.id.tv_top_title);
        mLeftText = (TextView) mToolbar.findViewById(R.id.tv_top_left);
        mRightText = (TextView) mToolbar.findViewById(R.id.tv_top_right);
        mLeftView = (ImageView) mToolbar.findViewById(R.id.iv_top_left);
        mRightView = (ImageView) mToolbar.findViewById(R.id.iv_top_right);
        mllBack.setOnClickListener(this);
        mRightView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.ll_back){
           backLastActivity();
        }else if (v.getId()==R.id.iv_top_right){
            backMainActivity();
        }
    }
    protected void backLastActivity() {
        finish();
    }
    protected void backMainActivity() {}
}
