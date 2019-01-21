package com.gcml.module_auth_hospital.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;

/**
 * Created by lenovo on 2019/1/18.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected TranslucentToolBar toolBar;
    private FrameLayout childView;
    private PermissionDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initTitle();
        initChidView();
        initData();
    }

    private void initChidView() {
        childView = findViewById(R.id.fl_child_view);
        childView.addView(View.inflate(this, layout(), null));
    }

    private void initTitle() {
        toolBar = findViewById(R.id.tb_base_title);
        toolBar.setData(title(),
                leftRes(), leftString()
                , rightRes(), rightString()
                , new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        BaseActivity.this.onLeftClick();
                    }

                    @Override
                    public void onRightClick() {
                        BaseActivity.this.onRightClick();
                    }
                });

    }

    protected void onRightClick() {
        if (needPermission()) {
            onRightClickWithPermission();
        } else {
            onRightClickWithNoPermission();
        }
    }


    protected void onRightClickWithPermission() {
        if (dialog == null) {
            dialog = new PermissionDialog();
        }
        dialog.setOnClickListener(passWord -> {
            if (TextUtils.equals("123456", passWord)) {
                onRightClickWithNoPermission();
            } else {
                ToastUtils.showShort("密码不正确");
            }
        });
        if (dialog.isAdded()) {
//            dialog.dismiss();
        } else {
            dialog.show(getFragmentManager(), "permission");
        }
    }

    protected void onRightClickWithNoPermission() {

    }

    private void onLeftClick() {
        finish();
    }

    protected int leftRes() {
        return R.drawable.common_ic_wifi_state;
    }

    protected int rightRes() {
        return R.drawable.common_btn_back;
    }

    protected String leftString() {
        return "返回";
    }

    protected String rightString() {
        return "";
    }

    protected LoadingDialog mLoadingDialog;

    protected void showLoading(String tips) {
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

    private void dismissLoading() {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
    }

    abstract String title();

    abstract boolean needPermission();

    abstract int layout();

    protected abstract void initData();

    /*<T> void  initData(Class < T> tClass ) {
        T service = RetrofitHelper.service(tClass);
    }*/
}
