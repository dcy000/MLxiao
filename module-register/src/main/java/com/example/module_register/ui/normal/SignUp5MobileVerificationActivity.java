package com.example.module_register.ui.normal;

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
import com.example.module_register.ui.base.VoiceToolBarActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.bean.PhoneCode;
import com.gzq.lib_core.bean.UserInfoBean;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.exception.ErrorTransformer;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.service.CommonAPI;
import com.gzq.lib_core.utils.DataUtils;
import com.gzq.lib_core.utils.Handlers;
import com.gzq.lib_core.utils.KeyboardUtils;
import com.gzq.lib_core.utils.PinYinUtils;
import com.gzq.lib_core.utils.RegularExpressionUtils;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo on 2017/10/12.
 */

public class SignUp5MobileVerificationActivity extends VoiceToolBarActivity {
    @BindView(R2.id.et_sign_up_phone)
    EditText etPhone;
    @BindView(R2.id.et_sign_up_code)
    EditText etCode;
    @BindView(R2.id.tv_sign_up_fetch_code)
    TextView tvFetchCode;
    @BindView(R2.id.tv_sign_up_go_back)
    TextView tvGoBack;
    @BindView(R2.id.tv_sign_up_go_forward)
    TextView tvGoForward;
    public Unbinder mUnbinder;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SignUp5MobileVerificationActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_sign_up5_mobile_verification;
    }

    @Override
    public void initParams(Intent intentArgument) {

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

    @Override
    public void initView() {
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    public boolean isShowVoiceView() {
        return true;
    }

    @Override
    protected boolean isShowToolbar() {
        return false;
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
    }


    @Override
    protected void onResume() {
        super.onResume();
        setDisableWakeup(true);
        robotStartListening();
        MLVoiceSynthetize.startSynthesize(inPhone ? R.string.sign_up_phone_tip : R.string.sign_up_code_tip);
        EditText editText = inPhone ? etPhone : etCode;
        editText.requestFocus();
        KeyboardUtils.hideKeyboard(editText);
    }

    @OnClick(R2.id.cl_sign_up_root_mobile_verification)
    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            KeyboardUtils.hideKeyboard(view);
        }
    }

    private volatile boolean inPhone = true;

    private String mCode;

    @OnClick(R2.id.tv_sign_up_fetch_code)
    public void onTvFetchCodeClicked() {
        inPhone = false;
        etCode.requestFocus();
        MLVoiceSynthetize.startSynthesize(R.string.sign_up_fetch_code_tip);
        final String phone = etPhone.getText().toString().trim();
        if (!RegularExpressionUtils.isMobile(phone)) {
            MLVoiceSynthetize.startSynthesize("主人，手机号码输入有误，请重新输入");
            inPhone = true;
            etPhone.setText("");
            etPhone.requestFocus();
            return;
        }
        tvFetchCode.setEnabled(false);

        Box.getRetrofit(CommonAPI.class)
                .isPhoneRegister(phone, "3")
                .onErrorReturn(new Function<Throwable, Object>() {
                    @Override
                    public Object apply(Throwable throwable) throws Exception {
                        Handlers.ui().post(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShort("主人，手机号码已注册");
                                MLVoiceSynthetize.startSynthesize("主人，手机号码已注册");
                                inPhone = true;
                                etPhone.setText("");
                                etPhone.requestFocus();
                                tvFetchCode.setEnabled(true);
                            }
                        });
                        return null;
                    }
                })
                .doOnNext(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        i = 60;
                        Handlers.ui().postDelayed(countDown, 1000);
                    }
                })
                .flatMap(new Function<Object, ObservableSource<PhoneCode>>() {
                    @Override
                    public ObservableSource<PhoneCode> apply(Object o) throws Exception {
                        return Box.getRetrofit(CommonAPI.class)
                                .getPhoneCode(phone)
                                .compose(ErrorTransformer.<PhoneCode>getInstance());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<PhoneCode>() {
                    @Override
                    public void onNext(PhoneCode o) {
                        mCode = o.getCode();
                        ToastUtils.showShort("获取验证码成功");
                        MLVoiceSynthetize.startSynthesize("获取验证码成功");
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        ToastUtils.showShort("获取验证码失败");
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

    @OnClick(R2.id.tv_sign_up_go_back)
    public void onTvGoBackClicked() {
        finish();
    }

    @OnClick(R2.id.tv_sign_up_go_forward)
    public void onTvGoForwardClicked() {
        String code = etCode.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(code) || !RegularExpressionUtils.isMobile(phone)) {
            MLVoiceSynthetize.startSynthesize(inPhone ? R.string.sign_up_phone_tip : R.string.sign_up_code_tip);
            return;
        }
        if (TextUtils.isEmpty(mCode)) {
            return;
        }
        if (mCode.contains(code)) {
            ToastUtils.showShort("验证码正确");
            navToNext();
        } else {
            ToastUtils.showShort("验证码错误");
            MLVoiceSynthetize.startSynthesize("验证码错误");
        }

        cacheUserInfo(phone);
    }

    private void cacheUserInfo(String photo) {
        UserInfoBean user = Box.getSessionManager().getUser();
        if (user == null) {
            user = new UserInfoBean();
        }
        user.tel = photo;
        Box.getSessionManager().setUser(user);
    }

    private void navToNext() {
        Intent intent = SignUp6PasswordActivity.newIntent(this);
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
        ToastUtils.showShort(result);

        if (result.matches(REGEX_IN_GO_BACK)) {
            onTvGoBackClicked();
            return;
        }
        if (result.matches(REGEX_IN_GO_FORWARD)) {
            onTvGoForwardClicked();
            return;
        }

        Pattern patternInNumber = Pattern.compile(REGEX_IN_NUMBER);
        String in = DataUtils.isInteger(result) ? result : DataUtils.removeNonnumeric(DataUtils.chineseMapToNumber(result));
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
            MLVoiceSynthetize.startSynthesize(R.string.sign_up_phone_tip);
            return;
        }

        if (inSpell.matches(REGEX_FETCH_CODE) && tvFetchCode.isEnabled()) {
            onTvFetchCodeClicked();
            return;
        }

        if (inSpell.matches(REGEX_IN_CODE) && inPhone) {
            inPhone = false;
            MLVoiceSynthetize.startSynthesize(R.string.sign_up_code_tip);
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