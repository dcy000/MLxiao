package com.medlink.danbogh.register.simple;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.util.LocalShared;
import com.medlink.danbogh.utils.Handlers;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lenovo on 2017/10/12.
 */

public class SignUp02MobileVerificationActivity extends BaseActivity {
    @BindView(R.id.et_sign_up_phone)
    EditText etPhone;
    @BindView(R.id.et_sign_up_code)
    EditText etCode;
    @BindView(R.id.tv_sign_up_fetch_code)
    TextView tvFetchCode;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvGoForward;
    public Unbinder mUnbinder;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SignUp02MobileVerificationActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    private boolean forResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            forResult = intent.getBooleanExtra("forResult", false);
        }
        setShowVoiceView(true);
        setContentView(R.layout.activity_sign_up5_mobile_verification);
        mToolbar.setVisibility(View.GONE);
        mUnbinder = ButterKnife.bind(this);
        initView();
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

    private void initView() {

    }


    @Override
    protected void onResume() {
        setEnableListeningLoop(false);
        setDisableGlobalListen(true);
        super.onResume();
        speak(inPhone ? R.string.sign_up_phone_tip : R.string.sign_up_code_tip);
        EditText editText = inPhone ? etPhone : etCode;
        editText.requestFocus();
        Utils.hideKeyBroad(editText);
    }

    @OnClick(R.id.cl_sign_up_root_mobile_verification)
    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            Utils.hideKeyBroad(view);
        }
    }

    private volatile boolean inPhone = true;

    private String mCode;

    @OnClick(R.id.tv_sign_up_fetch_code)
    public void onTvFetchCodeClicked() {
        inPhone = false;
        etCode.requestFocus();
        speak(R.string.sign_up_fetch_code_tip);
        final String phone = etPhone.getText().toString().trim();
        if (!Utils.isValidPhone(phone)) {
            speak("您好，手机号码输入有误，请重新输入");
            inPhone = true;
            etPhone.setText("");
            etPhone.requestFocus();
            return;
        }
        tvFetchCode.setEnabled(false);
        NetworkApi.canRegister(phone, "3", new NetworkManager.SuccessCallback<Object>() {
            @Override
            public void onSuccess(Object response) {
                etCode.requestFocus();
                NetworkApi.getCode(phone, new NetworkManager.SuccessCallback<String>() {
                    @Override
                    public void onSuccess(String code) {
                        mCode = code;
                        T.show("获取验证码成功");
                        speak("获取验证码成功");
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        T.show("获取验证码失败");
                        speak("获取验证码失败");
                    }
                });

                i = 60;
                Handlers.ui().postDelayed(countDown, 1000);
            }
        }, new NetworkManager.FailedCallback() {
            @Override
            public void onFailed(String message) {
                speak("您好，手机号码已注册");
                inPhone = true;
                etPhone.setText("");
                etPhone.requestFocus();
                tvFetchCode.setEnabled(true);
            }
        });
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

    @OnClick(R.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        if (forResult) {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @OnClick(R.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        String code = etCode.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(code) || !Utils.isValidPhone(phone)) {
            speak(inPhone ? R.string.sign_up_phone_tip : R.string.sign_up_code_tip);
            return;
        }

        if (mCode.contains(code)) {
            T.show("验证码正确");
            if (!forResult) {
                navToNext();
            } else {
                setResult(RESULT_OK, new Intent().putExtra("phone", phone));
                finish();
            }
        } else {
            T.show("验证码错误");
            speak("验证码错误");
        }
        LocalShared.getInstance(this.getApplicationContext()).setSignUpPhone(phone);
    }

    private void navToNext() {
        Intent intent = SignUp03PasswordActivity.newIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public static final String REGEX_DEL = "(quxiao|qingchu|sandiao|shandiao|sancu|shancu|sanchu|shanchu|budui|cuole|cuole)";
    public static final String REGEX_DEL_ALL = ".*(chongxin|quanbu|suoyou|shuoyou).*";
    public static final String REGEX_IN_NUMBER = "(\\d+)";
    public static final String REGEX_IN_GO_BACK = ".*(上一步|上一部|后退|返回).*";
    public static final String REGEX_IN_GO_FORWARD = ".*(下一步|下一部|确定|完成).*";
    public static final String REGEX_IN_PHONE = ".*(shu|su)(ru|lu|lv)(shou|sou)ji.*";
    public static final String REGEX_FETCH_CODE = ".*(fasong|huoqu)yanzhengma.*";
    public static final String REGEX_IN_CODE = ".*(shu|su)(ru|lu|lv)(yanzhengma)";

    @Override
    protected void onSpeakListenerResult(String result) {
        T.show(result);

        if (result.matches(REGEX_IN_GO_BACK)) {
            onTvGoBackClicked();
            return;
        }
        if (result.matches(REGEX_IN_GO_FORWARD)) {
            onTvGoForwardClicked();
            return;
        }

        Pattern patternInNumber = Pattern.compile(REGEX_IN_NUMBER);
        String in = Utils.isNumeric(result) ? result : Utils.removeNonnumeric(Utils.chineseMapToNumber(result));
        Matcher matcherInNumber = patternInNumber.matcher(in);
        if (matcherInNumber.find()) {
            EditText et = inPhone ? this.etPhone : this.etCode;
            int length = inPhone ? 11 : 6;
            String s = et.getText().toString() + matcherInNumber.group(matcherInNumber.groupCount());
            if (s.length() > length) {
                s = s.substring(0, length);
            }
            et.setText(s);
            et.setSelection(s.length());
            return;
        }

        String inSpell = PinYinUtils.converterToSpell(result);

        if (inSpell.matches(REGEX_IN_PHONE) && !inPhone) {
            inPhone = true;
            etPhone.requestFocus();
            speak(R.string.sign_up_phone_tip);
            return;
        }

        if (inSpell.matches(REGEX_FETCH_CODE) && tvFetchCode.isEnabled()) {
            onTvFetchCodeClicked();
            return;
        }

        if (inSpell.matches(REGEX_IN_CODE) && inPhone) {
            inPhone = false;
            speak(R.string.sign_up_code_tip);
            etCode.requestFocus();
            return;
        }

        if (inSpell.matches(REGEX_DEL_ALL)) {
            EditText editText = inPhone ? etPhone : etCode;
            editText.setText("");
            editText.setSelection(0);
            return;
        }

        if (inSpell.matches(REGEX_DEL)) {
            EditText editText = inPhone ? etPhone : etCode;
            String target = editText.getText().toString().trim();
            if (!TextUtils.isEmpty(target)) {
                editText.setText(target.substring(0, target.length() - 1));
                editText.setSelection(target.length() - 1);
            }
        }
    }
}