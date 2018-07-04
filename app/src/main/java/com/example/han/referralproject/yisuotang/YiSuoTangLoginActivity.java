package com.example.han.referralproject.yisuotang;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.AgreementActivity;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.facerecognition.AuthenticationActivity;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.util.LocalShared;
import com.example.han.referralproject.util.ToastTool;
import com.medlink.danbogh.register.SignUp1NameActivity;
import com.medlink.danbogh.register.simple.SignUp01NameActivity;
import com.medlink.danbogh.signin.SignInActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YiSuoTangLoginActivity extends BaseActivity implements View.OnClickListener {

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_yi_suo_tang_login);
//    }
    @BindView(R.id.tv_phone_sign_in)
    TextView tvPhoneSignIn;
    @BindView(R.id.et_sign_in_password)
    TextView etSignInPassword;
    @BindView(R.id.tv_face_sign_in)
    TextView tvFaceSignIn;
    @BindView(R.id.account_tip)
    TextView accountTip;
    @BindView(R.id.regist)
    TextView regist;
    @BindView(R.id.sign_up_fast)
    TextView signUpFast;
    @BindView(R.id.cv_sign_in)
    CardView cvSignIn;
    @BindView(R.id.cb_sign_in_agree)
    CheckBox cbSignInAgree;
    @BindView(R.id.tv_sign_in_agree)
    TextView tvSignInAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yi_suo_tang_login);
        ButterKnife.bind(this);
        speak("主人，想要登录，请说人脸登录或者手机登录。如果您还没有账号，请说我要注册。");
        tvPhoneSignIn.setOnClickListener(this);
        tvFaceSignIn.setOnClickListener(this);
        accountTip.setOnClickListener(this);
        regist.setOnClickListener(this);
        signUpFast.setOnClickListener(this);

        SpannableStringBuilder agreeBuilder = new SpannableStringBuilder("我同意用户协议");
        agreeBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF380000")),
                3, 7, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        agreeBuilder.setSpan(agreeClickableSpan, 3, 7, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tvSignInAgree.setMovementMethod(LinkMovementMethod.getInstance());
        tvSignInAgree.setText(agreeBuilder);
    }

    private ClickableSpan agreeClickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            startActivity(new Intent(YiSuoTangLoginActivity.this, AgreementActivity.class));
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_phone_sign_in:
                startActivity(new Intent(this, SignInActivity.class));
                break;
            case R.id.tv_face_sign_in:
                //获取所有账号
                String[] accounts = LocalShared.getInstance(YiSuoTangLoginActivity.this).getAccounts();
                if (accounts == null) {
                    ToastTool.showLong("未检测到您的登录历史，请输入账号和密码登录");
                    startActivity(new Intent(this, SignInActivity.class));
                } else {
                    startActivity(new Intent(this, AuthenticationActivity.class)
                            .putExtra("from", "Welcome"));
                }
                break;
            case R.id.account_tip://注册
                startActivity(new Intent(YiSuoTangLoginActivity.this, SignUp1NameActivity.class));
                break;
            case R.id.regist:
                //立即注册
                startActivity(new Intent(YiSuoTangLoginActivity.this, SignUp1NameActivity.class));
                break;
            case R.id.sign_up_fast://快速注册
                Intent intent = new Intent(YiSuoTangLoginActivity.this, SignUp01NameActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);

    }

    @Override
    protected void onSpeakListenerResult(String result) {
        String inSpell = PinYinUtils.converterToSpell(result);
        ToastTool.showShort(result);
        if (inSpell.matches(".*((shou|sou)ji).*")) {
            tvPhoneSignIn.performClick();
            return;
        }
        if (inSpell.matches(".*((ren|reng)lian).*")) {
            tvFaceSignIn.performClick();
            return;
        }

        if (inSpell.matches(".*((zu|zhu)ce).*")) {
            regist.performClick();
            return;
        }
    }
}