package com.gcml.module_auth_hospital.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.common.data.AppManager;
import com.gcml.common.http.ApiException;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.ValidateIDCard;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.gcml.module_auth_hospital.ui.dialog.AcountInfoDialog;
import com.gcml.module_auth_hospital.ui.login.ScanIdCardLoginActivity;
import com.gcml.module_auth_hospital.wrap.NumeriKeypadLayout;
import com.gcml.module_auth_hospital.wrap.NumeriKeypadLayoutHelper;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Route(path = "/auth/hospital/user/register2/activity")
public class IDCardNuberRegisterActivity extends ToolbarBaseActivity implements View.OnClickListener, AcountInfoDialog.OnFragmentInteractionListener {


    private EditText ccetPhone;
    private TextView tvNext;
    private UserRepository repository = new UserRepository();
    private TranslucentToolBar translucentToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowToolbar = false;
        setContentView(R.layout.activity_register_by_idcard_nuber);
        initView();
        useNumberKeyPad();
        AppManager.getAppManager().addActivity(this);
    }

    private void initView() {
        translucentToolBar = findViewById(R.id.auth_idcard_numer_register__tb);
        ccetPhone = findViewById(R.id.ccet_phone);
        tvNext = findViewById(R.id.tv_next);
        tvNext.setText("下一步");
        tvNext.setOnClickListener(this);
        ccetPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                onTextChange(s);
            }
        });


        translucentToolBar.setData("身 份 证 号 码 注 册",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
//                        Routerfit.register(AppRouter.class).skipSettingActivity();
                        Routerfit.register(AppRouter.class).skipWifiConnectActivity(false);
                    }
                });

        setWifiLevel(translucentToolBar);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.tv_next) {
            checkIdCard();
        }
    }

    private void checkIdCard() {
        String idCardNumber = ccetPhone.getText().toString().replaceAll(" ", "");
        if (TextUtils.isEmpty(idCardNumber)) {
            speak("请输入您的身份证号码");
            return;
        }
        if (!ValidateIDCard.validateCard(idCardNumber)) {
            speak("请输入正确的身份证号码");
            return;
        }
        checkIdCard(idCardNumber);
    }

    private void toSetPassWord(String idCardNumber) {
        startActivity(new Intent(this, InputNameActivity.class)
                .putExtra("idCardNumber", idCardNumber));

        //输入姓名-->设置密码
    }


    private void speak(String content) {
        MLVoiceSynthetize.startSynthesize(this, content);
    }

    private void checkIdCard(final String idCard) {
        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .isAccountExist(idCard, 3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        ToastUtils.showShort("身份证已存在");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (throwable instanceof ApiException) {
                            if (((ApiException) throwable).code() == 1002) {
                                toSetPassWord(idCard);
                                return;
                            }
                        }
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });
    }


    private void toFilllRegisterInfo(String idCardNumber) {
    }

    public void onTextChange(Editable phone) {
        if (TextUtils.isEmpty(ccetPhone.getText().toString())
                && !Utils.checkIdCard1(ccetPhone.getText().toString())) {
            tvNext.setEnabled(false);
        } else {
            tvNext.setEnabled(true);
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

    private NumeriKeypadLayoutHelper layoutHelper;
    private NumeriKeypadLayoutHelper.Builder builder;

    private void useNumberKeyPad() {
        hideKeyboard(ccetPhone);
        ccetPhone.requestFocus();

        NumeriKeypadLayout numeriKeypadLayout = findViewById(R.id.imageView2);
        builder = new NumeriKeypadLayoutHelper.Builder()
                .layout(numeriKeypadLayout)
                .showX(true)
                .textChageListener(text -> {
                    if (ccetPhone.isFocused()) {
                        ccetPhone.setText(text);
                    }
                    layoutHelper = builder.newBuilder(builder.clearAll(false)).build();
                });
        layoutHelper = builder.build();

        ccetPhone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                layoutHelper.setLayoutText(ccetPhone.getText().toString());
                layoutHelper.setLayoutinputLength(18);
            }
        });
    }


}
