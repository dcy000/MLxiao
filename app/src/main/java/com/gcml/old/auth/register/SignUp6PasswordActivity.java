package com.gcml.old.auth.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.util.LocalShared;
import com.gcml.lib_utils.display.ToastUtils;
import com.medlink.danbogh.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUp6PasswordActivity extends BaseActivity {

    EditText etPassword;

    EditText etConfirmPassword;

    TextView tvGoBack;

    TextView tvGoForward;
    private ConstraintLayout clRoot;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SignUp6PasswordActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setShowVoiceView(true);
        setContentView(R.layout.auth_activity_sign_up6_password);
        etPassword = (EditText) findViewById(R.id.et_sign_up_password);
        etConfirmPassword = (EditText) findViewById(R.id.et_sign_up_confirm_password);
        tvGoBack = (TextView) findViewById(R.id.tv_sign_up_go_back);
        tvGoForward = (TextView) findViewById(R.id.tv_sign_up_go_forward);
        clRoot = (ConstraintLayout) findViewById(R.id.cl_sign_up_root_password);
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
        tvGoForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvGoForwardClicked();
            }
        });
        mToolbar.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        speak(R.string.sign_up_password_tip);
    }

    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            Utils.hideKeyBroad(view);
        }
    }

    public void onTvGoBackClicked() {
        finish();
    }

    public void onTvGoForwardClicked() {
        String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)
                || !TextUtils.isDigitsOnly(password)
                || password.length() != 6) {
            ToastUtils.showShort(R.string.sign_up_password_tip);
            speak(R.string.sign_up_password_tip);
            return;
        }

        LocalShared.getInstance(this.getApplicationContext()).setSignUpPassword(password);
        navToNext();
    }

    private void navToNext() {
        startActivity(new Intent(this, SignUp7HeightActivity.class));
    }

    public static final String REGEX_IN_DEL = "(quxiao|qingchu|sandiao|shandiao|sancu|shancu|sanchu|shanchu|budui|cuole|cuole)";
    public static final String REGEX_IN_DEL_ALL = ".*(chongxin|quanbu|suoyou|shuoyou).*";
    public static final String REGEX_IN_NUMBER = "(\\d+)";
    public static final String REGEX_IN_GO_BACK = ".*(上一步|上一部|后退|返回).*";
    public static final String REGEX_IN_GO_FORWARD = ".*(下一步|下一部|确定|完成).*";

    @Override
    protected void onSpeakListenerResult(String result) {
        ToastUtils.showShort(result);

        if (result.matches(REGEX_IN_GO_BACK)) {
            onTvGoBackClicked();
            return;
        }
        if (result.matches(REGEX_IN_GO_FORWARD)) {
            onTvGoForwardClicked();
            return;
        }

        Pattern patternInPassword = Pattern.compile(REGEX_IN_NUMBER);
        String in = Utils.isNumeric(result) ? result : Utils.removeNonnumeric(Utils.chineseMapToNumber(result));
        Matcher matcherInIdCard = patternInPassword.matcher(in);
        if (matcherInIdCard.find()) {
            String s = etPassword.getText().toString() + matcherInIdCard.group(matcherInIdCard.groupCount());
            s = s.length() > 6 ? s.substring(0, 6) : s;
            etPassword.setText(s);
            etPassword.setSelection(s.length());
            return;
        }

        String inSpell = PinYinUtils.converterToSpell(result);
        if (inSpell.matches(REGEX_IN_DEL_ALL)) {
            etPassword.setText("");
            etPassword.setSelection(0);
            return;
        }

        if (inSpell.matches(REGEX_IN_DEL)) {
            String target = etPassword.getText().toString().trim();
            if (!TextUtils.isEmpty(target)) {
                etPassword.setText(target.substring(0, target.length() - 1));
                etPassword.setSelection(target.length() - 1);
            }
        }
    }
}
