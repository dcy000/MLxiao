package com.gcml.module_auth_hospital.ui.register;


import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.billy.cc.core.component.CC;
import com.billy.cc.core.component.CCResult;
import com.billy.cc.core.component.IComponentCallback;
import com.gcml.common.IConstant;
import com.gcml.common.base.BaseActivity;
import com.gcml.common.data.UserEntity;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.app.ActivityHelper;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class IDCardRegisterInfoActivity extends BaseActivity implements View.OnClickListener {
    private TranslucentToolBar authRegisterInfoTb;
    private TextView etRegisterName;
    private TextView etRegisterMinzu;
    private TextView etRegisterIdcrad;
    private TextView etRegisterNowAddress;
    private TextView etRegisterDetailAddress;
    private TextView tvAuthNext;
    private TextView tvMan;
    private TextView woman;

    private String name;
    private String gender;
    private String nation;
    private String number;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);
        initExtra();
        initView();
        ActivityHelper.addActivity(this);
    }

    private void initExtra() {
        Intent data = getIntent();
        if (data != null) {
            name = data.getStringExtra(ScanIdCardRegisterActivity.REGISTER_REAL_NAME);
            gender = data.getStringExtra(ScanIdCardRegisterActivity.REGISTER_SEX);
            nation = data.getStringExtra(ScanIdCardRegisterActivity.REGISTER_IDCARD_MINZU);
            number = data.getStringExtra(ScanIdCardRegisterActivity.REGISTER_IDCARD_NUMBER);
            address = data.getStringExtra(ScanIdCardRegisterActivity.REGISTER_ADDRESS);
        }
    }

    private void initView() {
        authRegisterInfoTb = findViewById(R.id.auth_register_info_tb);
        etRegisterName = findViewById(R.id.et_register_name);
        etRegisterMinzu = findViewById(R.id.et_register_minzu);
        etRegisterIdcrad = findViewById(R.id.et_register_idcrad);
        etRegisterNowAddress = findViewById(R.id.et_register_now_address);
        etRegisterDetailAddress = findViewById(R.id.et_register_detail_address);
        etRegisterDetailAddress = findViewById(R.id.et_register_detail_address);
        tvMan = findViewById(R.id.auth_tv_man);
        woman = findViewById(R.id.auth_tv_woman);
        tvAuthNext = findViewById(R.id.tv_auth_next);
        tvAuthNext.setOnClickListener(this);

        etRegisterName.setText(name);
        etRegisterMinzu.setText(nation);
        etRegisterIdcrad.setText(number);
        etRegisterNowAddress.setText(address);
        setGender();
        authRegisterInfoTb.setData("账 号 注 册",
                R.drawable.common_btn_back, "返回",
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

        setWifiLevel(authRegisterInfoTb);
    }

    private void setGender() {
        Drawable drawable = getResources().getDrawable(R.drawable.ic_select);
        drawable.setBounds(0, 0, 56, 56);
        Drawable drawable1 = getResources().getDrawable(R.drawable.ic_not_select);
        drawable1.setBounds(0, 0, 56, 56);
        if (TextUtils.equals("男", gender)) {
            tvMan.setCompoundDrawables(drawable, null, null, null);
            woman.setCompoundDrawables(drawable1, null, null, null);

        } else if (TextUtils.equals("女", gender)) {
            tvMan.setCompoundDrawables(drawable1, null, null, null);
            woman.setCompoundDrawables(drawable, null, null, null);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_auth_next) {
            register();
        }
    }


    private void register() {
        String deviceId = Utils.getDeviceId(getApplicationContext().getContentResolver());
        repository.signUpByIdCard(deviceId, name, gender, number, address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        showLoading("正在注册...");
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissLoading();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity userEntity) {
                        ToastUtils.showLong("身份证注册成功");
                        CC.obtainBuilder("com.gcml.auth.face2.signup")
                                .build()
                                .callAsyncCallbackOnMainThread(new IComponentCallback() {
                                    @Override
                                    public void onResult(CC cc, CCResult result) {
                                        if (result.isSuccess()) {
                                            startActivity(new Intent(IDCardRegisterInfoActivity.this, RegisterSuccessActivity.class)
                                                    .putExtra("idcard", number));
                                        } else {
                                            toLogin();
                                        }
                                    }
                                });

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        String message = throwable.getMessage();
                        ToastUtils.showShort(message);
                    }
                });
    }

    UserRepository repository = new UserRepository();

    private void toHome() {
        finish();
//        startActivity(new Intent(this, RegisterSuccessActivity.class));
        CC.obtainBuilder(IConstant.KEY_INUIRY_ENTRY).build().callAsync();
    }

    private void toLogin() {
        //关闭 注册时的 扫面sfz和信息录入页面
        finish();
        startActivity(new Intent(this, ScanIdCardRegisterActivity.class));
    }
}
