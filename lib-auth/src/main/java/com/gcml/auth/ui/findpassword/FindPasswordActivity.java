package com.gcml.auth.ui.findpassword;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.billy.cc.core.component.CC;
import com.gcml.auth.BR;
import com.gcml.auth.R;
import com.gcml.auth.databinding.AuthActivityFindPasswordBinding;
import com.gcml.common.mvvm.BaseActivity;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.lib_utils.display.KeyboardUtils;
import com.gcml.lib_utils.display.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FindPasswordActivity extends BaseActivity<AuthActivityFindPasswordBinding, FindPasswordViewModel> {

    private String mPhone = "";

    @Override
    protected int layoutId() {
        return R.layout.auth_activity_find_password;
    }

    @Override
    protected int variableId() {
        return BR.viewModel;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        binding.setPresenter(this);
        mPhone = getIntent().getStringExtra("phone");
        if (!TextUtils.isEmpty(mPhone)) {
            binding.etPhone.setText(mPhone);
            binding.etPhone.setSelection(mPhone.length());
        }
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
        CC.obtainBuilder("com.gcml.old.wifi").build().callAsync();
    }

    public void goNext() {
        final String phone = binding.etPhone.getText().toString().trim();
        if (!Utils.isValidPhone(phone)) {
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人，请输入正确的手机号码", true);
            ToastUtils.showShort("主人，请输入正确的手机号码");
            return;
        }
        viewModel.hasAccount(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean has) {
                        if (has) {
                            CC.obtainBuilder("com.gcml.auth.setpassword")
                                    .addParam("phone", phone)
                                    .setContext(FindPasswordActivity.this)
                                    .build()
                                    .callAsync();
                        } else {
                            ToastUtils.showShort("账号不存在！");
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLVoiceSynthetize.startSynthesize(getApplicationContext(), "主人，请输入您的手机号码", true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLVoiceSynthetize.stop();
    }
}
