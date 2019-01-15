package com.gcml.module_auth_hospital.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.gcml.module_auth_hospital.ui.dialog.AcountInfoDialog;
import com.gcml.module_auth_hospital.ui.login.ScanIdCardLoginActivity;
import com.gcml.module_auth_hospital.wrap.CanClearEditText;
import com.iflytek.synthetize.MLVoiceSynthetize;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.gcml.module_auth_hospital.ui.register.ScanIdCardRegisterActivity.REGISTER_FORM_IDCARD_NUMBER;
import static com.gcml.module_auth_hospital.ui.register.ScanIdCardRegisterActivity.REGISTER_FORM_WHERE;
import static com.gcml.module_auth_hospital.ui.register.ScanIdCardRegisterActivity.REGISTER_IDCARD_NUMBER;

public class IDCardNuberRegisterActivity extends AppCompatActivity implements View.OnClickListener, CanClearEditText.OnTextChangeListener, AcountInfoDialog.OnFragmentInteractionListener {


    private CanClearEditText ccetPhone;
    private TextView tvNext;
    private UserRepository userRepository = new UserRepository();
    private TranslucentToolBar translucentToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_idcard_nuber);
        initView();
    }

    private void initView() {
        translucentToolBar = findViewById(R.id.auth_idcard_numer_tb);
        ccetPhone = (CanClearEditText) findViewById(R.id.ccet_phone);
        tvNext = (TextView) findViewById(R.id.tv_next);
        tvNext.setText("下一步");
        tvNext.setOnClickListener(this);
        ccetPhone.setListener(this);

        ccetPhone.setValue("340321199112256551");

        translucentToolBar.setData("身 份 证 扫 描 注 册",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {

                    }
                });

        RxUtils.rxWifiLevel(getApplication(), 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        translucentToolBar.setImageLevel(integer);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_next) {
            checkIdCard();

        } else {
        }
    }

    private void checkIdCard() {
        String idCardNumber = ccetPhone.getPhone();
        if (TextUtils.isEmpty(idCardNumber)) {
            speak("请输入您的身份证号码");
            return;
        }
        if (!Utils.checkIdCard1(idCardNumber)) {
            speak("请输入正确的身份证号码");
            return;
        }
        onCheckRegistered(idCardNumber);
    }


    private void speak(String content) {
        MLVoiceSynthetize.startSynthesize(this, content);
    }

    private void onCheckRegistered(String idCardNumber) {
        userRepository
                .isIdCardNotExit(idCardNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        //身份证未被绑定后其他异常情况
                        toFilllRegisterInfo(idCardNumber);
                    }


                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });
    }


    private void toFilllRegisterInfo(String idCardNumber) {
        startActivity(new Intent(this, IDCardNumberRegisterInfoActivity.class)
                .putExtra(REGISTER_FORM_WHERE, REGISTER_FORM_IDCARD_NUMBER)
                .putExtra(REGISTER_IDCARD_NUMBER, idCardNumber)
        );
    }

    @Override
    public void onTextChange(Editable phone) {
        if (TextUtils.isEmpty(phone.toString()) && Utils.checkIdCard1(phone.toString())) {
            tvNext.setEnabled(false);
        } else {
            tvNext.setEnabled(true);
        }
    }

    private LoadingDialog mLoadingDialog;

    private void showLoading(String tips) {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
        mLoadingDialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(tips)
                .create();
        mLoadingDialog.show();
    }

    private void dismissLoading() {
        if (mLoadingDialog != null) {
            LoadingDialog loadingDialog = mLoadingDialog;
            mLoadingDialog = null;
            loadingDialog.dismiss();
        }
    }


    private AcountInfoDialog dialog;

    private void showAccountInfoDialog() {
        if (dialog == null) {
            dialog = AcountInfoDialog.newInstance("身份证已注册，是否直接登录？", null);
        }
        if (dialog.isAdded()) {
            dialog.dismiss();
        } else {
            dialog.setListener(this);
            dialog.show(getSupportFragmentManager(), "ScanIDCardRegister");
        }
    }

    @Override
    public void onCancle() {

    }

    @Override
    public void onConfirm() {
        startActivity(new Intent(this, ScanIdCardLoginActivity.class));
    }

}
