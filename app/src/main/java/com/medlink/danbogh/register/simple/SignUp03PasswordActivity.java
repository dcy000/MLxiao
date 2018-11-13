package com.medlink.danbogh.register.simple;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.bean.UserInfoBean;
import com.example.han.referralproject.facerecognition.RegisterVideoActivity;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.network.NetworkManager;
import com.example.han.referralproject.util.LocalShared;
import com.gzq.administrator.lib_common.utils.PinYinUtils;
import com.medlink.danbogh.utils.JpushAliasUtils;
import com.medlink.danbogh.utils.T;
import com.medlink.danbogh.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SignUp03PasswordActivity extends BaseActivity {

    @BindView(R.id.et_sign_up_password)
    EditText etPassword;
    @BindView(R.id.et_sign_up_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.tv_sign_up_go_back)
    TextView tvGoBack;
    @BindView(R.id.tv_sign_up_go_forward)
    TextView tvGoForward;
    public Unbinder mUnbinder;

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
        setContentView(R.layout.activity_sign_up6_password);
        mToolbar.setVisibility(View.GONE);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableGlobalListen(true);
        speak(R.string.sign_up_password_tip);
    }

    @OnClick(R.id.cl_sign_up_root_password)
    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            Utils.hideKeyBroad(view);
        }
    }

    @OnClick(R.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)
                || !TextUtils.isDigitsOnly(password)
                || password.length() != 6) {
            T.show(R.string.sign_up_password_tip);
            speak(R.string.sign_up_password_tip);
            return;
        }
        signUp(password);
        LocalShared.getInstance(this.getApplicationContext()).setSignUpPassword(password);
    }

    private void navToNext() {
        Intent intent = new Intent(this, RegisterVideoActivity.class);
        intent.putExtra("isFast", true);
        startActivity(intent);
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
                        LocalShared.getInstance(mContext).setUserPhoto(response.user_photo);
                        LocalShared.getInstance(mContext).setUserAge(response.age);
                        LocalShared.getInstance(mContext).setUserHeight(response.height);
                        new JpushAliasUtils(SignUp03PasswordActivity.this).setAlias("user_" + response.bid);
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
                        T.show(message);
                        speak("主人," + message);
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
        T.show(result);

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
