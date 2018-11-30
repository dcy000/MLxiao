package com.example.module_register.ui.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.module_register.R;
import com.example.module_register.R2;
import com.example.module_register.service.RegisterAPI;
import com.example.module_register.ui.base.VoiceToolBarActivity;
import com.gcml.auth.face.FaceConstants;
import com.gcml.auth.face.ui.FaceSignUpActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.ActivityUtils;
import com.gzq.lib_core.utils.DataUtils;
import com.gzq.lib_core.utils.DeviceUtils;
import com.gzq.lib_core.utils.KeyboardUtils;
import com.gzq.lib_core.utils.PinYinUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

public class SignUp03PasswordActivity extends VoiceToolBarActivity {

    @BindView(R2.id.et_sign_up_password)
    EditText etPassword;
    @BindView(R2.id.et_sign_up_confirm_password)
    EditText etConfirmPassword;
    @BindView(R2.id.tv_sign_up_go_back)
    TextView tvGoBack;
    @BindView(R2.id.tv_sign_up_go_forward)
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
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_sign_up6_password;
    }

    @Override
    public void initParams(Intent intentArgument) {

    }

    @Override
    public void initView() {
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected boolean isShowToolbar() {
        return false;
    }

    @Override
    public boolean isShowVoiceView() {
        return true;
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
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
        setDisableWakeup(true);
        robotStartListening();
        MLVoiceSynthetize.startSynthesize(R.string.sign_up_password_tip);
    }

    @OnClick(R2.id.cl_sign_up_root_password)
    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            KeyboardUtils.hideKeyboard(view);
        }
    }

    @OnClick(R2.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R2.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)
                || !TextUtils.isDigitsOnly(password)
                || password.length() != 6) {
            ToastUtils.showShort(R.string.sign_up_password_tip);
            MLVoiceSynthetize.startSynthesize(R.string.sign_up_password_tip);
            return;
        }
        signUp(password);
        cacheUserInfo(password);
    }

    private void cacheUserInfo(String password) {
        UserInfoBean user = Box.getSessionManager().getUser();
        if (user == null) {
            user = new UserInfoBean();
        }
        user.password = password;
        Box.getSessionManager().setUser(user);
    }

    private void navToNext() {
        ActivityUtils.skipActivityForResult(FaceSignUpActivity.class, 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (data != null) {
                int extra = data.getIntExtra(FaceConstants.KEY_AUTH_FACE_RESULT, 0);
                switch (extra) {
                    case FaceConstants.AUTH_FACE_SUCCESS:
                        //快速注册
                        emitEvent("skip2MainActivity");
                        break;
                    case FaceConstants.AUTH_FACE_FAIL:
                        break;
                }
            }
        }
    }

    private void signUp(String password) {
        showLoadingDialog(getString(R.string.do_register));
        UserInfoBean user = Box.getSessionManager().getUser();

        Box.getRetrofit(RegisterAPI.class)
                .registerAccount(
                        "50",
                        user.bname,
                        user.sex,
                        DeviceUtils.getIMEI(),
                        user.tel,
                        user.password,
                        user.dz,
                        user.sfz,
                        user.height,
                        user.weight,
                        user.bloodType,
                        user.eatingHabits,
                        user.smoke,
                        user.drink,
                        user.exerciseHabits
                )
                .compose(RxUtils.<UserInfoBean>httpResponseTransformer())
                .doOnNext(new Consumer<UserInfoBean>() {
                    @Override
                    public void accept(UserInfoBean userInfoBean) throws Exception {
                        Box.getSessionManager().setUser(userInfoBean);
                    }
                })
                .as(RxUtils.<UserInfoBean>autoDisposeConverter(this))
                .subscribe(new CommonObserver<UserInfoBean>() {
                    @Override
                    public void onNext(UserInfoBean userInfoBean) {
                        Box.getSessionManager().setUser(userInfoBean);
                        navToNext();
                        finishAffinity();
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        hideLoadingDialog();
                        MLVoiceSynthetize.startSynthesize("主人," + ex.message);
                    }
                });
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
        String in = DataUtils.isInteger(result) ? result : DataUtils.removeNonnumeric(DataUtils.chineseMapToNumber(result));
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
