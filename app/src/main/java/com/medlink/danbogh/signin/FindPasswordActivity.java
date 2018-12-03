package com.medlink.danbogh.signin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.service.API;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.medlink.danbogh.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class FindPasswordActivity extends BaseActivity {


    @BindView(R.id.tv_find_password_title)
    TextView tvTitle;
    @BindView(R.id.et_find_password_phone)
    EditText etPhone;
    @BindView(R.id.tv_find_password_go_back)
    TextView tvGoBack;
    @BindView(R.id.et_find_next)
    TextView tvNext;
    private Unbinder mUnbinder;
    private String mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);
        mToolbar.setVisibility(View.GONE);
        mUnbinder = ButterKnife.bind(this);
        mPhone = getIntent().getStringExtra("phone");
        if (!TextUtils.isEmpty(mPhone)) {
            etPhone.setText(mPhone);
            etPhone.setSelection(mPhone.length());
        }
    }

    @OnClick(R.id.cl_root)
    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            Utils.hideKeyBroad(view);
        }
    }

    @OnClick(R.id.tv_find_password_go_back)
    public void onTvGoBackClicked() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.et_find_next)
    public void onTvNextClicked() {
        final String phone = etPhone.getText().toString().trim();
        if (!Utils.isValidPhone(phone)) {
            MLVoiceSynthetize.startSynthesize("主人，请输入正确的手机号码");
            ToastUtils.showShort("主人，请输入正确的手机号码");
            return;
        }
        showLoadingDialog("加载中...");
        Box.getRetrofit(API.class)
                .isPhoneUsable("3", phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        hideLoadingDialog();
                    }
                })
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onNext(Object o) {

                        Intent intent = new Intent(FindPasswordActivity.this, SetPasswordActivity.class);
                        intent.putExtra("phone", phone);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDisableWakeup(true);
        MLVoiceSynthetize.startSynthesize("主人，请输入您的手机号码");
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}
