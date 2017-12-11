package com.medlink.danbogh.signin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.AgreementActivity;
import com.medlink.danbogh.register.SignUp1NameActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.et_sign_in_phone)
    EditText etPhone;
    @BindView(R.id.et_sign_in_password)
    EditText etPassword;
    @BindView(R.id.tv_sign_in_sign_in)
    TextView tvSignIn;
    @BindView(R.id.cb_sign_in_agree)
    CheckBox cbAgree;
    @BindView(R.id.tv_sign_in_agree)
    TextView tvAgree;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mUnbinder = ButterKnife.bind(this);
        etPhone.addTextChangedListener(inputWatcher);
        etPassword.addTextChangedListener(inputWatcher);
        cbAgree.setOnCheckedChangeListener(onCheckedChangeListener);
        SpannableStringBuilder agreeBuilder = new SpannableStringBuilder("我同意用户协议");
        agreeBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF380000")),
                3, 7, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        agreeBuilder.setSpan(agreeClickableSpan, 3, 7, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tvAgree.setMovementMethod(LinkMovementMethod.getInstance());
        tvAgree.setText(agreeBuilder);
        checkInput();
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    checkInput();
                }
            };

    private TextWatcher inputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkInput();
        }
    };

    private boolean checkInput() {
        boolean validPassword = false;
        boolean validPhone = false;
        boolean checked = cbAgree.isChecked();
        String phone = etPhone.toString().trim();
        if (!TextUtils.isEmpty(phone)) {
            validPassword = true;
        }
        String password = etPassword.toString().trim();
        if (!TextUtils.isEmpty(password)) {
            validPhone = true;
        }
        boolean enabled = validPassword && validPhone && checked;
        tvSignIn.setEnabled(enabled);
        return enabled;
    }

    private ClickableSpan agreeClickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            startActivity(new Intent(SignInActivity.this, AgreementActivity.class));
        }
    };

    @OnClick(R.id.tv_sign_in_sign_in)
    public void onTvSignInClicked() {

    }

    @OnClick(R.id.tv_sign_in_sign_up)
    public void onTvSignUpClicked() {
        startActivity(new Intent(SignInActivity.this, SignUp1NameActivity.class));
    }

    @OnClick(R.id.tv_sign_in_forget_password)
    public void onTvForgetPasswordClicked() {

    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
