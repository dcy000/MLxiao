package com.gcml.common.base;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.gcml.common.business.R;
import com.gcml.common.widget.dialog.LoadingDialog;

/**
 * Created by lenovo on 2019/1/18.
 */

public class BaseActivity extends AppCompatActivity {
    private PermissionDialog dialog;

    protected interface IAction {
        void action();
    }

    public void onRightClickWithPermission(IAction action) {
        if (dialog == null) {
            dialog = new PermissionDialog();
        }
        dialog.setOnClickListener(passWord -> {
            if (TextUtils.equals("123456", passWord)) {
                if (action != null) {
                    action.action();
                }
            }
        });
        if (dialog.isAdded()) {
//            dialog.dismiss();
        } else {
            dialog.show(getFragmentManager(), "permission");
        }
    }

    public void onLeftClick() {
        finish();
    }

    public int leftRes() {
        return R.drawable.common_btn_back;
    }

    public int rightRes() {
        return R.drawable.common_ic_wifi_state;
    }

    public String leftString() {
        return "返回";
    }

    public String rightString() {
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
}
