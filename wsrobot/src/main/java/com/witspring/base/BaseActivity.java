package com.witspring.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.witspring.mlrobot.R;
import com.witspring.model.entity.Result;
import com.witspring.util.StringUtil;

/**
 * 所有Activity基类
 * Created by Goven on 2014/9/26 0026.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected ActionBar actionBar;
    private boolean showing;// 是否正在前台显示
    private MaterialDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
        }
        if (getPresenter() != null) {
            getPresenter().start();
        }
    }

    public void setToolbar(View toolbar, String title) {
        setSupportActionBar((Toolbar) toolbar);
        TextView tvTitle = toolbar.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        toolbar.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void onResume() {
        super.onResume();
        showing = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        showing = false;
    }

    @Override
    protected void onDestroy() {
        if (getPresenter() != null) {
            getPresenter().stop();
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    protected BasePresenter getPresenter() {
        return null;
    }

    public View getContentView() {
        return ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
    }

    public Context getContext() {
        return this;
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public void showKeyboard(final View view, long delay) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                showKeyboard(view);
            }
        }, 100);
    }

    public void showToastShort(String value) {
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
    }

    public void showToastLong(String value) {
        Toast.makeText(this, value, Toast.LENGTH_LONG).show();
    }

    public void warningNoConnect() {
        showToastShort("网络故障，请检查！");
    }

    public void warningUnknow(Result result) {
        if (result.getStatus() == Result.STATUS_NETWORK_ERROR) {
            warningNoConnect();
        } else {
            if (StringUtil.isNotTrimBlank(result.getMsg())) {
                showToastLong(result.getMsg());
            } else {
                showToastLong("未知错误，请联系在线客服！");
            }
        }
    }

    public void warningNoData() {
        showToastShort("没有数据！");
    }

    public boolean isLoading() {
        return loadingDialog != null && loadingDialog.isShowing();
    }

    public boolean isShowing() {
        return showing;
    }

    public void startLoading() {
        startLoading(getString(R.string.ws_data_loading));
    }

    public void startLoadingAlway() {
        startLoading(getString(R.string.ws_data_loading), false);
    }

    public void startLoading(String message) {
        startLoading(message, true);
    }

    public void startLoading(String content, boolean cancelable) {
        if (loadingDialog == null) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
            if (StringUtil.isNotTrimBlank(content)) {
                builder.content(content);
            }
            builder.progress(true, 0);
            builder.cancelable(cancelable);
            builder.canceledOnTouchOutside(cancelable);
            loadingDialog = builder.show();
        } else {
            loadingDialog.setContent(content);
            loadingDialog.setCancelable(cancelable);
            loadingDialog.setCanceledOnTouchOutside(cancelable);
            if (!loadingDialog.isShowing()) {
                loadingDialog.show();
            }
        }
    }

    public void stopLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    public void showDialog(String message) {
        showDialog(null, message, getString(R.string.ws_ensure), null, false, null);
    }

    public void showDialog(String message, String positive, String negative, DialogActionListener listener) {
        showDialog(null, message, positive, negative, true, listener);
    }

    public void showDialog(String title, String content, String positive, String negative, boolean cancelable, final DialogActionListener listener) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        if (StringUtil.isNotTrimBlank(title)) {
            builder.title(title);
        }
        if (StringUtil.isNotTrimBlank(content)) {
            builder.content(content);
        }
        if (StringUtil.isNotTrimBlank(positive)) {
            builder.positiveText(positive);
            if (listener != null) {
                builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        listener.onPositive();
                    }
                });
            }
        }
        if (StringUtil.isNotTrimBlank(negative)) {
            builder.negativeText(negative);
            if (listener != null) {
                builder.onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        listener.onNegative();
                    }
                });
            }
        }
        builder.autoDismiss(true);
        builder.canceledOnTouchOutside(cancelable);
        builder.cancelable(cancelable);
        builder.show();
    }

    public abstract class DialogActionListener {
        public void onPositive() {}
        public void onNegative() {}
        public void onNeutral() {}
        public void onAny(String name) {}
    }

}
