package com.gcml.lib_widget;

import android.support.annotation.CallSuper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gcml.lib_widget.dialog.LoadingDialog;
import com.gzq.lib_core.base.ui.BaseActivity;

public abstract class ToolbarBaseActivity extends BaseActivity implements View.OnClickListener {
    protected View mToolbar = null;
    protected TextView mTitleText;
    protected TextView mRightText;
    protected ImageView mLeftView;
    protected ImageView mRightView;
    protected TextView mLeftText;
    protected LinearLayout mllBack;
    private long lastclicktime = 0L;
    private LoadingDialog mLoadingDialog;

    @Override
    public void setContentView(int layoutResID) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        viewGroup.removeAllViews();
        LinearLayout parent = new LinearLayout(this);
        parent.setOrientation(LinearLayout.VERTICAL);
        viewGroup.addView(parent);
        if (isShowToolbar()) {
            mToolbar = LayoutInflater.from(this).inflate(R.layout.toolbar_layout, parent, true);
            initToolbar();
        }
        LayoutInflater.from(this).inflate(layoutResID, parent, true);
    }

    protected void initToolbar() {
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
        if (v.getId() == R.id.ll_back) {
            backLastActivity();
        } else if (v.getId() == R.id.iv_top_right) {
            if (System.currentTimeMillis() - lastclicktime > 2000) {
                lastclicktime = System.currentTimeMillis();
                backMainActivity();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideLoadingDialog();
    }

    protected void backLastActivity() {
        finish();
    }

    protected void backMainActivity() {
        emitEvent("skip2MainActivity");
    }

    protected boolean isShowToolbar() {
        return true;
    }

    public void showLoadingDialog(String tips) {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
        mLoadingDialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(tips)
                .create();
        mLoadingDialog.show();
    }

    public void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
    }
}
