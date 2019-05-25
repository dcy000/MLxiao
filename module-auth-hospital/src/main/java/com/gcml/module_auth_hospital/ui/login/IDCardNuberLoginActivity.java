package com.gcml.module_auth_hospital.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.common.data.AppManager;
import com.gcml.common.data.UserEntity;
import com.gcml.common.http.ApiException;
import com.gcml.common.router.AppRouter;
import com.gcml.common.user.UserPostBody;
import com.gcml.common.user.UserToken;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.JpushAliasUtils;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model2.UserBean;
import com.gcml.module_auth_hospital.model2.UserRepository;
import com.gcml.module_auth_hospital.ui.dialog.AcountInfoDialog;
import com.gcml.module_auth_hospital.ui.register.UserRegisters2Activity;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class IDCardNuberLoginActivity extends ToolbarBaseActivity implements View.OnClickListener, AcountInfoDialog.OnFragmentInteractionListener {


    private EditText ccetPhone;
    private TextView tvNext;
    private EditText etPsw;
    private UserRepository userRepository = new UserRepository();
    private TranslucentToolBar translucentToolBar;
    private TextWatcher watcher = new TextWatcher() {
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
    };
    private String idCardNumber;
    private String trim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowToolbar = false;
        setContentView(R.layout.activity_login_by_idcard_nuber);
        initView();
        AppManager.getAppManager().addActivity(this);
    }

    private void initView() {
        translucentToolBar = findViewById(R.id.auth_idcard_numer_tb);
        ccetPhone = findViewById(R.id.ccet_phone);
        tvNext = (TextView) findViewById(R.id.tv_next);
        etPsw = findViewById(R.id.et_psw);
        tvNext.setOnClickListener(this);

        ccetPhone.addTextChangedListener(watcher);
        etPsw.addTextChangedListener(watcher);
//        ccetPhone.setValue("340321199112256552");

        translucentToolBar.setData("身 份 证 号 码 登 录",
                R.drawable.common_btn_back, "返回",
                R.drawable.common_ic_wifi_state, null,
                new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {
                        Routerfit.register(AppRouter.class).skipSettingActivity();
                    }
                });
        setWifiLevel(translucentToolBar);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int id = view.getId();
        if (id == R.id.tv_next) {
            checkIdCard();
        }
    }

    private void checkIdCard() {
        idCardNumber = ccetPhone.getText().toString().replaceAll(" ", "");
        trim = etPsw.getText().toString().trim();
        if (TextUtils.isEmpty(idCardNumber)) {
            speak("请输入您的身份证号");
            return;
        }
        if (!Utils.checkIdCard1(idCardNumber)) {
            speak("请输入正确的身份证号码");
            return;
        }

        if (TextUtils.isEmpty(trim)) {
            speak("请输入6位数字密码");
            return;
        }

//        checkIdCardIsRegisterOrNot(idCardNumber);
        signIn();
    }


    private void speak(String content) {
        MLVoiceSynthetize.startSynthesize(this, content);
    }

    private void checkIdCardIsRegisterOrNot(String idCardNumber) {
       /* String deviceId = Utils.getDeviceId(getContentResolver());
        userRepository
                .isIdCardNotExit(ccetPhone.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
//                        ToastUtils.showShort("未注册,请先去注册");
                        showAccountInfoDialog();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
        signIn(deviceId, idCardNumber);*/
    }

    private void signIn() {

        UserPostBody body = new UserPostBody();
        body.password=trim;
        body.sfz=idCardNumber;
        userRepository
                .signInByIdCard(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> showLoading("正在登录..."))
                .doOnTerminate(() -> dismissLoading())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserBean>() {
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }

                    @Override
                    public void onNext(UserBean userBean) {
                        super.onNext(userBean);
                        ToastUtils.showShort("登录成功");
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                });



               /* .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity user) {
                        JpushAliasUtils.setAlias(user.id);
                        ToastUtils.showLong("登录成功");
                        Routerfit.register(AppRouter.class).skipMainActivity();
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (throwable instanceof ApiException) {
                            int code = ((ApiException) throwable).code();
                            if (code == 1002) {
                                showAccountInfoDialog();
                            }
                        }
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });*/
    }

    public void onTextChange(Editable phone) {
        if (!TextUtils.isEmpty(phone.toString()) && (!Utils.checkIdCard1(etPsw.toString()))) {
            tvNext.setEnabled(true);
        } else {
            tvNext.setEnabled(false);
        }
    }

    private AcountInfoDialog dialog;

    private void showAccountInfoDialog() {
        if (dialog == null) {
            dialog = new AcountInfoDialog();
        }

        if (dialog.isAdded()) {
            dialog.dismiss();
        } else {
            dialog.setListener(this);
            dialog.show(getSupportFragmentManager(), "IDCardNuberLoginActivity");
        }
    }

    @Override
    public void onCancle() {

    }

    @Override
    public void onConfirm() {
        startActivity(new Intent(this, UserRegisters2Activity.class));
    }

}
