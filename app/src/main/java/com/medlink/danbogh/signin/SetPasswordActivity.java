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
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.Utils;
import com.mob.MobSDK;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

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
        initSms();
    }

    private void initSms() {
        MobSDK.init(this, Utils.SMS_KEY, Utils.SMS_SECRETE);
        SMSSDK.registerEventHandler(smsHandler);
    }

    @OnClick(R.id.cl_root)
    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            Utils.hideKeyBroad(view);
        }
    }

    @OnClick(R.id.tv_set_password_fetch_code)
    public void onTvFetchCodeClicked() {
        tvFetchCode.setEnabled(false);
        SMSSDK.getVerificationCode("86", mPhone);
        i = 30;
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
            speak(R.string.sign_up_code_tip);
            return;
        }
        SMSSDK.submitVerificationCode("86", mPhone, code);
    }

    private EventHandler smsHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                switch (event) {
                    case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                        T.show("正在获取验证码...");
                        break;
                    case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                        T.show("验证码正确");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onValidPhone();
                            }
                        });
                        break;
                    default:
                        break;
                }
            } else {
                switch (event) {
                    case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                        T.show("无法获取验证码");
                        speak("主人,当前手机号的验证码获取次数已超过5次,请更换手机号码或改天再试");
                        break;
                    case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                        T.show("验证码错误");
                        speak("主人，您输入的验证码有误，请重新输入");
                        break;
                }
            }
        }
    };

    private void onValidPhone() {
        showLoadingDialog("加载中...");
        String pwd = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(pwd)) {
            T.show("请输入6位数字密码");
            speak("请输入6位数字密码");
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
                T.show(message);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        speak("主人，请输入验证码及新的6位数字密码");
    }

    @Override
    protected void onDestroy() {
        if (smsHandler != null) {
            SMSSDK.unregisterEventHandler(smsHandler);
        }
        if (countDown != null) {
            Handlers.ui().removeCallbacks(countDown);
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
