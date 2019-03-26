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
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.utils.Utils;

public class SetPasswordActivity extends BaseActivity {

    EditText etCode;

    TextView tvFetchCode;

    EditText etPassword;

    TextView tvGoBack;

    TextView etNext;

    private String mPhone;
    private ConstraintLayout clRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity_set_password_old);
        clRoot = (ConstraintLayout) findViewById(R.id.cl_root);
        etCode = (EditText) findViewById(R.id.et_set_password_find_code);
        tvFetchCode = (TextView) findViewById(R.id.tv_set_password_fetch_code);
        etPassword = (EditText) findViewById(R.id.et_set_password_password);
        tvGoBack = (TextView) findViewById(R.id.tv_set_password_go_back);
        etNext = (TextView) findViewById(R.id.et_find_next);
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
        tvFetchCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvFetchCodeClicked();
            }
        });
        etNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEtNextClicked();
            }
        });
        mPhone = getIntent().getStringExtra("phone");

    }

    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            Utils.hideKeyBroad(view);
        }
    }

    private String code;

    public void onTvFetchCodeClicked() {
        tvFetchCode.setEnabled(false);
        NetworkApi.getCode(mPhone, new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String code) {
                SetPasswordActivity.this.code = code;
                ToastUtils.showShort("获取验证码成功");
                speak("获取验证码成功");
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.showShort("获取验证码失败");
                speak("获取验证码失败");
            }
        });
        i = 60;
        Handlers.ui().postDelayed(countDown, 1000);
    }

    private int i;

    private Runnable countDown = new Runnable() {
        @Override
        public void run() {
            if (i == 0) {
                tvFetchCode.setText("获取验证码");
                tvFetchCode.setEnabled(true);
                return;
            }
            tvFetchCode.setText("已发送（" + i + "）");
            i--;
            Handlers.ui().postDelayed(countDown, 1000);
        }
    };

    public void onTvGoBackClicked() {
        Intent intent = new Intent(SetPasswordActivity.this, FindPasswordActivity.class);
        intent.putExtras(getIntent());
        startActivity(intent);
        finish();
    }

    public void onEtNextClicked() {
        String code = etCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            speak(R.string.sign_up_code_tip);
            return;
        }

        if (this.code.contains(code)) {
            onValidPhone();
        } else {
            ToastUtils.showShort("验证码错误");
            speak("验证码错误");
        }
    }

    private void onValidPhone() {
        showLoadingDialog("加载中...");
        String pwd = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(pwd) || pwd.length() != 6) {
            ToastUtils.showShort("请输入6位数字密码");
            speak("请输入6位数字密码");
            hideLoadingDialog();
            return;
        }
        NetworkApi.setPassword(mPhone, pwd, new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String response) {
                hideLoadingDialog();
                Intent intent = new Intent(SetPasswordActivity.this, SignInActivity.class);
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
        speak("请输入验证码及新的6位数字密码");
    }

    @Override
    protected void onDestroy() {
        if (countDown != null) {
            Handlers.ui().removeCallbacks(countDown);
        }
        super.onDestroy();
    }
}
