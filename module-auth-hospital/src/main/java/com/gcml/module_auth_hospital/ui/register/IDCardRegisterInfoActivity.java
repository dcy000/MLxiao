package com.gcml.module_auth_hospital.ui.register;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gcml.common.data.AppManager;
import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class IDCardRegisterInfoActivity extends ToolbarBaseActivity implements View.OnClickListener {
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
        AppManager.getAppManager().addActivity(this);
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
                        Routerfit.register(AppRouter.class).skipSettingActivity();
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
        super.onClick(v);
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
                        UserSpHelper.setUserId(userEntity.id);
                        ToastUtils.showLong("身份证注册成功");
                        Routerfit.register(AppRouter.class)
                                .skipFaceBdSignUpActivity(UserSpHelper.getUserId(), new ActivityCallback() {
                                    @Override
                                    public void onActivityResult(int result, Object data) {
                                        if (result == Activity.RESULT_OK) {
                                            String sResult = data.toString();
                                            if (TextUtils.isEmpty(sResult)) return;
                                            if (sResult.equals("success")) {
                                                startActivity(new Intent(IDCardRegisterInfoActivity.this, RegisterSuccessActivity.class)
                                                        .putExtra("idcard", number));
                                            } else if (sResult.equals("failed")) {
                                                toLogin();
                                            }
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

    private void toLogin() {
        //关闭 注册时的 扫面sfz和信息录入页面
        finish();
        startActivity(new Intent(this, ScanIdCardRegisterActivity.class));
    }
}
