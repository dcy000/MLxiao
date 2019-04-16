package com.gcml.module_auth_hospital.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.gcml.common.base.BaseActivity;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.app.ActivityHelper;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.gcml.module_auth_hospital.ui.dialog.AcountInfoDialog;
import com.gcml.module_auth_hospital.ui.login.ScanIdCardLoginActivity;
import com.gcml.module_auth_hospital.wrap.CanClearEditText;
import com.iflytek.synthetize.MLVoiceSynthetize;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.gcml.module_auth_hospital.ui.register.ScanIdCardRegisterActivity.REGISTER_FORM_IDCARD_NUMBER;
import static com.gcml.module_auth_hospital.ui.register.ScanIdCardRegisterActivity.REGISTER_FORM_WHERE;
import static com.gcml.module_auth_hospital.ui.register.ScanIdCardRegisterActivity.REGISTER_IDCARD_NUMBER;

public class IDCardNuberRegisterActivity extends BaseActivity implements View.OnClickListener, CanClearEditText.OnTextChangeListener, AcountInfoDialog.OnFragmentInteractionListener {


    private CanClearEditText ccetPhone;
    private TextView tvNext;
    private UserRepository userRepository = new UserRepository();
    private TranslucentToolBar translucentToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_by_idcard_nuber);
        initView();
        ActivityHelper.addActivity(this);
    }

    private void initView() {
        translucentToolBar = findViewById(R.id.auth_idcard_numer_register__tb);
        ccetPhone = (CanClearEditText) findViewById(R.id.ccet_phone);
        tvNext = (TextView) findViewById(R.id.tv_next);
        tvNext.setText(getString(R.string.common_next));
        tvNext.setOnClickListener(this);
        ccetPhone.setListener(this);

//        ccetPhone.setValue("340321199112256551");

        translucentToolBar.setData(getString(R.string.loign_register_id_number_titl),
                R.drawable.common_btn_back, getString(R.string.common_back_title),
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        onRightClickWithPermission(new IAction() {
                            @Override
                            public void action() {
                                CC.obtainBuilder("com.gcml.old.setting").build().call();
                            }
                        });

                    }
                });

        setWifiLevel(translucentToolBar);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_next) {
            checkIdCard();
        }
    }

    private void checkIdCard() {
        String idCardNumber = ccetPhone.getPhone();
        if (TextUtils.isEmpty(idCardNumber)) {
            speak(getString(R.string.loign_id_number_input_tips));
            return;
        }
        if (!Utils.checkIdCard1(idCardNumber)) {
            speak(getString(R.string.loign_id_number_confirm_tips));
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

    private AcountInfoDialog dialog;

    private void showAccountInfoDialog() {
        if (dialog == null) {
            dialog = AcountInfoDialog.newInstance(getString(R.string.loign_register_has_registed_tips), null);
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
