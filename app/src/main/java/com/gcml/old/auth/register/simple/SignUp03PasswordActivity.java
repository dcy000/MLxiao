package com.gcml.old.auth.register.simple;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.cc.CCFaceRecognitionActions;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.old.auth.entity.UserInfoBean;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.speechsynthesis.PinYinUtils;
import com.example.han.referralproject.util.LocalShared;
import com.medlink.danbogh.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp03PasswordActivity extends BaseActivity {

    EditText etPassword;

    EditText etConfirmPassword;

    TextView tvGoBack;

    TextView tvGoForward;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SignUp03PasswordActivity.class);
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
        findViewById(R.id.cl_sign_up_root_password).setOnClickListener(new View.OnClickListener() {
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
        signUp(password);
        LocalShared.getInstance(this.getApplicationContext()).setSignUpPassword(password);
    }

    private void navToNext() {
        CCFaceRecognitionActions.jump2RegisterHead2XunfeiActivity(this);
    }

    private void signUp(String password) {
        showLoadingDialog(getString(R.string.do_register));
        final LocalShared shared = LocalShared.getInstance(this);
        String name = shared.getSignUpName();
        String gender = shared.getSignUpGender();
        String address = shared.getSignUpAddress();
        String idCard = shared.getSignUpIdCard();
        String phone = shared.getSignUpPhone();
        float height = shared.getSignUpHeight();
        float weight = shared.getSignUpWeight();
        String bloodType = shared.getSignUpBloodType();
        String eat = shared.getSignUpEat();
        String smoke = shared.getSignUpSmoke();
        String drink = shared.getSignUpDrink();
        String sport = shared.getSignUpSport();
        NetworkApi.registerUser(
                name,
                gender,
                address,
                idCard,
                phone,
                password,
                height,
                weight,
                bloodType,
                eat,
                smoke,
                drink,
                sport,
                new NetworkManager.SuccessCallback<UserInfoBean>() {
                    @Override
                    public void onSuccess(UserInfoBean response) {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        hideLoadingDialog();
                        shared.setUserInfo(response);
                        LocalShared.getInstance(mContext).setSex(response.sex);
                        LocalShared.getInstance(mContext).setUserPhoto(response.userPhoto);
                        LocalShared.getInstance(mContext).setUserAge(response.age);
                        LocalShared.getInstance(mContext).setUserHeight(response.height);
                        CC.obtainBuilder("com.gcml.zzb.common.push.setTag")
                                .addParam("userId", UserSpHelper.getUserId())
                                .build()
                                .callAsync();
                        NetworkApi.setUserMh("11", new NetworkManager.SuccessCallback<String>() {
                            @Override
                            public void onSuccess(String response) {
                                navToNext();
                                finishAffinity();
                            }
                        }, new NetworkManager.FailedCallback() {
                            @Override
                            public void onFailed(String message) {
                                navToNext();
                                finishAffinity();
                            }
                        });
                    }
                }, new NetworkManager.FailedCallback() {
                    @Override
                    public void onFailed(String message) {
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        hideLoadingDialog();
                        ToastUtils.showShort(message);
                        speak("您好，" + message);
                    }
                }
        );
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
