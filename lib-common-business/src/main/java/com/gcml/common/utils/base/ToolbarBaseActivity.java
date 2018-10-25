package com.gcml.common.utils.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.common.business.R;
import com.gcml.common.utils.click.ClickEventListener;

/**
 * Created by gzq on 2018/4/12.
 * decription:自带toolbar的Activity
 */

public abstract class ToolbarBaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected boolean isShowToolbar=true;
    protected View mToolbar = null;
    protected TextView mTitleText;
    protected TextView mRightText;
    protected ImageView mLeftView;
    protected ImageView mRightView;
    protected TextView mLeftText;
    protected LinearLayout mllBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        LinearLayout parent = new LinearLayout(this);
        parent.setOrientation(LinearLayout.VERTICAL);
        viewGroup.addView(parent);
        if (isShowToolbar) {
            mToolbar = LayoutInflater.from(this).inflate(R.layout.utils_title_layout, parent, true);
            initToolbar();
        }
        LayoutInflater.from(this).inflate(layoutResID, parent, true);
    }

    private void initToolbar() {
        mllBack = mToolbar.findViewById(R.id.ll_back);
        mToolbar = mToolbar.findViewById(R.id.toolbar);
        mTitleText = mToolbar.findViewById(R.id.tv_top_title);
        mLeftText = mToolbar.findViewById(R.id.tv_top_left);
        mRightText = mToolbar.findViewById(R.id.tv_top_right);
        mLeftView = mToolbar.findViewById(R.id.iv_top_left);
        mRightView = mToolbar.findViewById(R.id.iv_top_right);
        mllBack.setOnClickListener(this);
        mRightView.setOnClickListener(this);
    }

    @CallSuper
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.ll_back){
           backLastActivity();
        }else if (v.getId()==R.id.iv_top_right){
            backMainActivity();
        }
    }
    /**
     * Find出来的View，自带防抖功能
     */
    public <T extends View> T findClickView(int id) {

        T view = (T) findViewById(id);
        view.setOnClickListener(new ClickEventListener(this));
        return view;
    }
    protected void backLastActivity() {
        finish();
    }
    protected void backMainActivity() {}
}
