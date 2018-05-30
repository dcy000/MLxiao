package com.medlink.danbogh.signin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FindPasswordActivity extends BaseActivity {


    @BindView(R.id.tv_find_password_title)
    TextView tvTitle;
    @BindView(R.id.et_find_password_phone)
    EditText etPhone;
    @BindView(R.id.tv_find_password_go_back)
    TextView tvGoBack;
    @BindView(R.id.et_find_next)
    TextView tvNext;
    private Unbinder mUnbinder;
    private String mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        mToolbar.setVisibility(View.GONE);
        mUnbinder = ButterKnife.bind(this);
        mPhone = getIntent().getStringExtra("phone");
        if (!TextUtils.isEmpty(mPhone)) {
            etPhone.setText(mPhone);
            etPhone.setSelection(mPhone.length());
        }
    }

    @OnClick(R.id.cl_root)
    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            Utils.hideKeyBroad(view);
        }
    }

    @OnClick(R.id.tv_find_password_go_back)
    public void onTvGoBackClicked() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.et_find_next)
    public void onTvNextClicked() {
        final String phone = etPhone.getText().toString().trim();
        if (!Utils.isValidPhone(phone)) {
            speak("您好，请输入正确的手机号码");
            T.show("您好，请输入正确的手机号码");
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
//                        T.show(message);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        setEnableListeningLoop(false);
        speak("您好，请输入您的手机号码");
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
