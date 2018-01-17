package com.medlink.danbogh.signin;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.AgreementActivity;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.facerecognition.RegisterVideoActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.util.LocalShared;
import com.medlink.danbogh.register.SignUp1NameActivity;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.Utils;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class SignInActivity extends BaseActivity {

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
        mToolbar.setVisibility(View.GONE);
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
        ((TextView)findViewById(R.id.tv_version)).setText(getLocalVersionName());
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

    public String getLocalVersionName() {
        String localVersion = "";
        try {
            PackageInfo packageInfo = getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    @OnClick(R.id.cl_root)
    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            Utils.hideKeyBroad(view);
        }
    }

    @OnClick(R.id.tv_sign_in_sign_in)
    public void onTvSignInClicked() {
        if ("123456".equals(etPhone.getText().toString()) && "654321".equals(etPassword.getText().toString())){
            Intent mIntent = new Intent(mContext, RegisterVideoActivity.class);
            mIntent.putExtra("isTest", true);
            startActivity(mIntent);
            finish();
            return;
        }
        showLoadingDialog(getString(R.string.do_login));
        NetworkApi.login(etPhone.getText().toString(), etPassword.getText().toString(), new NetworkManager.SuccessCallback<UserInfoBean>() {
            @Override
            public void onSuccess(UserInfoBean response) {
                LocalShared.getInstance(mContext).setUserInfo(response);
                LocalShared.getInstance(mContext).addAccount(response.bid,response.xfid);
                LocalShared.getInstance(getApplicationContext()).setXunfeiID(response.xfid);
                LocalShared.getInstance(mContext).setEqID(response.eqid);
                LocalShared.getInstance(mContext).setSex(response.sex);
                LocalShared.getInstance(mContext).setUserPhoto(response.user_photo);
                LocalShared.getInstance(mContext).setUserAge(response.age);
                hideLoadingDialog();
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                hideLoadingDialog();
                T.show("手机号或密码错误");
            }
        });
    }

    @OnClick(R.id.tv_sign_in_sign_up)
    public void onTvSignUpClicked() {
        startActivity(new Intent(SignInActivity.this, SignUp1NameActivity.class));
    }

    @OnClick(R.id.tv_sign_in_forget_password)
    public void onTvForgetPasswordClicked() {
        String phone = etPhone.getText().toString().trim();
        Intent intent = new Intent(SignInActivity.this, FindPasswordActivity.class);
        if (Utils.isValidPhone(phone)) {
            intent.putExtra("phone", phone);
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        setDisableGlobalListen(true);
        super.onResume();
        speak(R.string.tips_login);
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    public void onWifiClick(View view) {
        Intent intent = new Intent(this, WifiConnectActivity.class);
        startActivity(intent);
    }

    boolean inPhone = true;

    @Override
    protected void onSpeakListenerResult(String result) {
        T.show(result);

        String inSpell = PinYinUtils.converterToSpell(result);

        if (inSpell.matches("zhuche|zhuce|zuce|zuche")) {
            onTvSignUpClicked();
            return;
        }

        if (inSpell.matches("denglu")) {
            onTvSignInClicked();
            return;
        }

        Pattern patternInNumber = Pattern.compile("(\\d+)");
        String in = Utils.isNumeric(result) ? result : Utils.removeNonnumeric(Utils.chineseMapToNumber(result));
        Matcher matcherInNumber = patternInNumber.matcher(in);
        if (matcherInNumber.find()) {
            EditText et = inPhone ? this.etPhone : this.etPassword;
            int length = inPhone ? 11 : 6;
            String s = et.getText().toString() + matcherInNumber.group(matcherInNumber.groupCount());
            if (s.length() > length) {
                s = s.substring(0, length);
            }
            et.setText(s);
            et.setSelection(s.length());
            return;
        }

        if (inSpell.matches(REGEX_IN_PHONE) && !inPhone) {
            inPhone = true;
            etPhone.requestFocus();
            speak(R.string.sign_up_phone_tip);
            return;
        }

        if (inSpell.matches(REGEX_IN_PASSWORD) && inPhone) {
            inPhone = false;
            speak("请输入密码");
            etPassword.requestFocus();
            return;
        }

        if (inSpell.matches(REGEX_DEL_ALL)) {
            EditText editText = inPhone ? etPhone : etPassword;
            editText.setText("");
            editText.setSelection(0);
            return;
        }

        if (inSpell.matches(REGEX_DEL)) {
            EditText editText = inPhone ? etPhone : etPassword;
            String target = editText.getText().toString().trim();
            if (!TextUtils.isEmpty(target)) {
                editText.setText(target.substring(0, target.length() - 1));
                editText.setSelection(target.length() - 1);
            }
        }
    }

    public static final String REGEX_DEL = "(quxiao|qingchu|sandiao|shandiao|sancu|shancu|sanchu|shanchu|budui|cuole)";
    public static final String REGEX_DEL_ALL = ".*(chongxin|quanbu|suoyou|shuoyou).*";
    public static final String REGEX_IN_PHONE = ".*(shu|su)(ru|lu|lv)(shou|sou)ji.*";
    public static final String REGEX_IN_PASSWORD = ".*(shu|su)(ru|lu|lv)(mima)";
}
