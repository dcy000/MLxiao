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
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.gzq.lib_core.utils.Handlers;
import com.medlink.danbogh.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SetPasswordActivity extends BaseActivity {

    @BindView(R.id.et_set_password_find_code)
    EditText etCode;
    @BindView(R.id.tv_set_password_fetch_code)
    TextView tvFetchCode;
    @BindView(R.id.et_set_password_password)
    EditText etPassword;
    @BindView(R.id.tv_set_password_go_back)
    TextView tvGoBack;
    @BindView(R.id.et_find_next)
    TextView etNext;
    private Unbinder mUnbinder;
    private String mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        mToolbar.setVisibility(View.GONE);
        mUnbinder = ButterKnife.bind(this);
        mPhone = getIntent().getStringExtra("phone");

    }

    @OnClick(R.id.cl_root)
    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            Utils.hideKeyBroad(view);
        }
    }

    private String code;

    @OnClick(R.id.tv_set_password_fetch_code)
    public void onTvFetchCodeClicked() {
        tvFetchCode.setEnabled(false);
        NetworkApi.getCode(mPhone, new NetworkManager.SuccessCallback<String>() {
            @Override
            public void onSuccess(String code) {
                SetPasswordActivity.this.code = code;
                ToastUtils.showShort("获取验证码成功");
                MLVoiceSynthetize.startSynthesize("获取验证码成功");
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                ToastUtils.showShort("获取验证码失败");
                MLVoiceSynthetize.startSynthesize("获取验证码失败");
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

    @OnClick(R.id.tv_set_password_go_back)
    public void onTvGoBackClicked() {
        Intent intent = new Intent(SetPasswordActivity.this, FindPasswordActivity.class);
        intent.putExtras(getIntent());
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.et_find_next)
    public void onEtNextClicked() {
        String code = etCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            MLVoiceSynthetize.startSynthesize(R.string.sign_up_code_tip);
            return;
        }

        if (this.code.contains(code)) {
            onValidPhone();
        } else {
            ToastUtils.showShort("验证码错误");
            MLVoiceSynthetize.startSynthesize("验证码错误");
        }
    }

    private void onValidPhone() {
        showLoadingDialog("加载中...");
        String pwd = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(pwd) || pwd.length() != 6) {
            ToastUtils.showShort("请输入6位数字密码");
            MLVoiceSynthetize.startSynthesize("请输入6位数字密码");
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
        setDisableWakeup(true);
        MLVoiceSynthetize.startSynthesize("主人，请输入验证码及新的6位数字密码");
    }

    @Override
    protected void onDestroy() {
        if (countDown != null) {
            Handlers.ui().removeCallbacks(countDown);
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
