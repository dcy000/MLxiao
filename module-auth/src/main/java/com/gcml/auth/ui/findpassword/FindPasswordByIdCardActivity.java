package com.gcml.auth.ui.findpassword;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.gcml.auth.R;

import com.gcml.auth.databinding.AuthActivityFindPasswordByIdCardBinding;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.display.KeyboardUtils;
import com.gcml.common.utils.display.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FindPasswordByIdCardActivity extends BaseActivity<AuthActivityFindPasswordByIdCardBinding, FindPasswordByIdCardViewModel> {

    private String mPhone = "";

    @Override
    protected int layoutId() {
        return R.layout.auth_activity_find_password_by_id_card;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    protected void init(Bundle savedInstanceState) {
        binding.setPresenter(this);
        binding.setViewModel(viewModel);
        mPhone = getIntent().getStringExtra("phone");
        if (!TextUtils.isEmpty(mPhone)) {
            binding.etPhone.setText(mPhone);
            binding.etPhone.setSelection(mPhone.length());
        }
    }

    public void rootOnClick() {
        View view = getCurrentFocus();
        if (view != null) {
            KeyboardUtils.hideKeyboard(this, view);
        }
    }

    public void goBack() {
        finish();
    }

    public void goWifi() {
        Routerfit.register(AppRouter.class).skipWifiConnectActivity(false);
    }

    private String code = "";

    public void fetchCode() {
    }

    public void goNext() {
        final String phone = binding.etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || phone.length() != 18) {
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "请输入正确的身份证号码", false);
            ToastUtils.showShort("请输入正确的身份证号码");
            return;
        }

        final String name = binding.etCode.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "请输入姓名", false);
            ToastUtils.showShort("请输入姓名");
            return;
        }
        viewModel.hasAccount2(phone, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean has) {
                        if (has) {
                            Routerfit.register(AppRouter.class).skipSetPasswordActivity(phone);
                        } else {
                            ToastUtils.showShort("账号不存在！");
                        }
                    }
                });
    }

    private void checkCode(String phone) {
        String code = binding.etCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "请输入验证码", false);
            ToastUtils.showShort("请输入验证码");
            return;
        }

        if (!code.equals(this.code)) {
            ToastUtils.showShort("验证码错误");
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "验证码错误", false);
            return;
        }
        Routerfit.register(AppRouter.class).skipSetPasswordActivity(phone);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RxUtils.rxWifiLevel(getApplication(), 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        binding.ivWifiState.setImageLevel(integer);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        code = "";
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "请输入您的身份证号码");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }
}
