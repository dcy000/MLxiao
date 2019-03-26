package com.gcml.old.auth.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.gcml.common.utils.display.ToastUtils;
import com.medlink.danbogh.utils.Utils;

public class FindPasswordActivity extends BaseActivity {

    TextView tvTitle;

    EditText etPhone;

    TextView tvGoBack;

    TextView tvNext;

    private String mPhone;
    private ConstraintLayout clRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity_find_password_old);
        clRoot = (ConstraintLayout) findViewById(R.id.cl_root);
        tvTitle = (TextView) findViewById(R.id.tv_find_password_title);
        etPhone = (EditText) findViewById(R.id.et_find_password_phone);
        tvGoBack = (TextView) findViewById(R.id.tv_find_password_go_back);
        tvNext = (TextView) findViewById(R.id.et_find_next);
        mToolbar.setVisibility(View.GONE);
        clRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClRootClicked();
            }
        });
        tvGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvGoBackClicked();
            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvNextClicked();
            }
        });
        mPhone = getIntent().getStringExtra("phone");
        if (!TextUtils.isEmpty(mPhone)) {
            etPhone.setText(mPhone);
            etPhone.setSelection(mPhone.length());
        }
    }

    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            Utils.hideKeyBroad(view);
        }
    }

    public void onTvGoBackClicked() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public void onTvNextClicked() {
        final String phone = etPhone.getText().toString().trim();
        if (!Utils.isValidPhone(phone)) {
            speak("请输入正确的手机号码");
            ToastUtils.showShort("请输入正确的手机号码");
            return;
        }
        showLoadingDialog("加载中...");
        NetworkApi.findAccount("3", phone,
                new NetworkManager.SuccessCallback<String>() {
                    @Override
                    public void onSuccess(String response) {
                        hideLoadingDialog();
                        Intent intent = new Intent(FindPasswordActivity.this, SetPasswordActivity.class);
                        intent.putExtra("phone", phone);
                        startActivity(intent);
                        finish();
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        hideLoadingDialog();
                        ToastUtils.showShort(message);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        speak("请输入您的手机号码");
    }

}
