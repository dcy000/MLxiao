package com.example.han.referralproject.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.util.LocalShared;
import com.medlink.danbogh.call2.NimCallActivity;
import com.medlink.danbogh.register.SignUp1NameActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private EditText mAccountTv;
    private EditText mPwdTv;
    private CheckBox mCheckBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAccountTv = (EditText) findViewById(R.id.et_phone);
        mPwdTv = (EditText) findViewById(R.id.et_password);
        mCheckBox = (CheckBox) findViewById(R.id.cb_agree);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.tv_register).setOnClickListener(this);
        findViewById(R.id.tv_agreement).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                if (TextUtils.isEmpty(mAccountTv.getText())){
                    Toast.makeText(mContext, R.string.empty_account, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mPwdTv.getText())){
                    Toast.makeText(mContext, R.string.empty_pwd, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mCheckBox.isChecked()){
                    Toast.makeText(mContext, R.string.agree_sure, Toast.LENGTH_SHORT).show();
                    return;
                }
                showLoadingDialog(getString(R.string.do_login));
                NetworkApi.login(mAccountTv.getText().toString(), mPwdTv.getText().toString(), new NetworkManager.SuccessCallback<UserInfoBean>() {
                    @Override
                    public void onSuccess(UserInfoBean response) {
                        LocalShared.getInstance(mContext).setUserInfo(response);
                        hideLoadingDialog();
                        startActivity(new Intent(mContext, MainActivity.class));
                        finish();
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        hideLoadingDialog();
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.tv_register:
//                startActivity(new Intent(mContext, SignUp1NameActivity.class));
                NimCallActivity.launch(this, "doctor_18940866148");
                break;
            case R.id.tv_agreement:
                startActivity(new Intent(mContext, AgreementActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        speak(R.string.tips_login);
    }

    @Override
    protected void onSpeakListenerResult(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();

        if (result.matches(".*注册.*") || PinYinUtils.converterToSpell(result).matches(".*zhuce.*")) {
            findViewById(R.id.tv_register).performClick();
        } else if (result.matches(".*登录.*") || PinYinUtils.converterToSpell(result).matches(".*denglu.*")) {
            findViewById(R.id.btn_login).performClick();
        }
    }

}
