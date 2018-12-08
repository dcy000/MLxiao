package com.example.module_login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.module_login.R;
import com.example.module_login.R2;
import com.example.module_login.service.LoginAPI;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.KeyboardUtils;
import com.gzq.lib_core.utils.RegularExpressionUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.iflytek.synthetize.MLVoiceSynthetize;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class FindPasswordActivity extends ToolbarBaseActivity {


    @BindView(R2.id.tv_find_password_title)
    TextView tvTitle;
    @BindView(R2.id.et_find_password_phone)
    EditText etPhone;
    @BindView(R2.id.tv_find_password_go_back)
    TextView tvGoBack;
    @BindView(R2.id.et_find_next)
    TextView tvNext;
    private Unbinder mUnbinder;
    private String mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setVisibility(View.GONE);



    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_find_password;
    }

    @Override
    public void initParams(Intent intentArgument) {
        mPhone =intentArgument.getStringExtra("phone");
    }

    @Override
    public void initView() {
        mUnbinder = ButterKnife.bind(this);
        if (!TextUtils.isEmpty(mPhone)) {
            etPhone.setText(mPhone);
            etPhone.setSelection(mPhone.length());
        }
    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }

    @OnClick(R2.id.cl_root)
    public void onClRootClicked() {
        View view = getCurrentFocus();
        if (view != null) {
            KeyboardUtils.hideKeyboard(view);
        }
    }

    @OnClick(R2.id.tv_find_password_go_back)
    public void onTvGoBackClicked() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R2.id.et_find_next)
    public void onTvNextClicked() {
        final String phone = etPhone.getText().toString().trim();
        if (!RegularExpressionUtils.isMobile(phone)) {
            MLVoiceSynthetize.startSynthesize("主人，请输入正确的手机号码");
            ToastUtils.showShort("主人，请输入正确的手机号码");
            return;
        }
        showLoadingDialog("加载中...");
        Box.getRetrofit(LoginAPI.class)
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
