package com.gcml.module_auth_hospital.ui.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.common.data.UserEntity;
import com.gcml.common.data.UserSpHelper;
import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.FilterClickListener;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.model.UserRepository;
import com.gcml.module_auth_hospital.postinputbean.SignUpBean;
import com.gcml.module_auth_hospital.ui.findPassWord.CodeRepository;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BindPhoneActivity extends ToolbarBaseActivity {

    private TextView notice;
    private TextView next;
    private TextView sendCode;
    private EditText phone;
    private EditText code;
    private TranslucentToolBar tb;
    private Intent data;
    private String codeNumer = "";
    private String passWord;
    private String idCardNumber;

    private String fromWhere;
    private UserEntity user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowToolbar = false;
        setContentView(R.layout.activity_bind_phone);
        fromWhere = getIntent().getStringExtra("fromWhere");
        user = getIntent().getParcelableExtra("data");
        initView();
    }

    public static void startMe(Context context, String passWord, String from) {
        Intent intent = new Intent(context, BindPhoneActivity.class);
        intent.putExtra("passWord", passWord);
        intent.putExtra("fromWhere", from);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    private void initView() {
        notice = findViewById(R.id.tv_notice);
        tb = findViewById(R.id.tb_bind_phone);
        phone = findViewById(R.id.et_phone);
        code = findViewById(R.id.et_code);
        next = findViewById(R.id.tv_next);
        next.setOnClickListener(new FilterClickListener(v -> {
            String phoneNumber = phone.getText().toString().trim();
            signUp(phoneNumber);
        }));
        sendCode = findViewById(R.id.tv_send_code);
        data = getIntent();
        if ("updatePhone".equals(fromWhere)) {
            next.setText("确 定");
        }
        String title = "updatePhone".equals(fromWhere) ? "修 改 手 机 号" : "绑 定 手 机";
        tb.setData(title,
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
        setWifiLevel(tb);

        phone.addTextChangedListener(watcher);
        sendCode.setOnClickListener(new FilterClickListener(v -> sendCode()));
    }


    UserRepository repository = new UserRepository();

    private void signUp(String phoneNumber) {
        if (TextUtils.isEmpty(code.getText().toString())) {
            ToastUtils.showShort("请输入验证码");
            return;
        }

        if (!this.codeNumer.equals(code.getText().toString())) {
            ToastUtils.showShort("验证码错误");
            return;
        }

        if ("updatePhone".equals(fromWhere)) {
            updatePhone(phoneNumber);
            return;
        }

      /*  if (!"123456".equals(code.getText().toString())) {
            ToastUtils.showShort("验证码错误");
            return;
        }*/

        Intent data = getIntent();
        if (data != null) {
            passWord = data.getStringExtra("passWord");
            idCardNumber = data.getStringExtra("idCardNumber");

            SignUpBean bean = new SignUpBean();
            bean.setIdNo(idCardNumber);
            bean.setTel(phoneNumber);

            repository.signUp(bean, passWord)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .as(RxUtils.autoDisposeConverter(this))
                    .subscribe(new DefaultObserver<UserEntity>() {
                        @Override
                        public void onNext(UserEntity userEntity) {
                            UserSpHelper.setUserId(userEntity.id);
                            super.onNext(userEntity);
                            vertifyFace();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            super.onError(throwable);
                            ToastUtils.showShort(throwable.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            super.onComplete();
                        }
                    });
        }

    }

    private void updatePhone(String phone) {
        if (user == null) {
            ToastUtils.showShort("请重新登陆！");
            return;
        }

        user.phone = phone;
        Routerfit.register(AppRouter.class)
                .getUserProvider()
                .updateUserEntity(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<UserEntity>() {
                    @Override
                    public void onNext(UserEntity o) {
                        speak("修改成功");
                        ToastUtils.showShort("修改成功");
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort("修改失败");
                        speak("修改失败");
                    }
                });
    }

    private void vertifyFace() {
        Routerfit.register(AppRouter.class)
                .skipFaceBd3SignUpActivity(UserSpHelper.getUserId(), new ActivityCallback() {
                    @Override
                    public void onActivityResult(int result, Object data) {
                        if (result == Activity.RESULT_OK) {
                            String sResult = data.toString();
                            if (TextUtils.isEmpty(sResult)) return;
                            if (sResult.equals("success")) {
                                startActivity(new Intent(BindPhoneActivity.this
                                        , RegisterSuccessActivity.class)
                                        .putExtra("passWord", passWord)
                                        .putExtra("idCardNumber", idCardNumber)
                                );
                            } else if (sResult.equals("failed")) {
                                ToastUtils.showShort("录入人脸失败");
                            }
                        }
                    }
                });
    }

    private CodeRepository codeRepository = new CodeRepository();
    Disposable countDownDisposable = Disposables.empty();

    private void sendCode() {
        final String phoneNumer = phone.getText().toString().trim();
        if (!Utils.isValidPhone(phoneNumer)) {
            MLVoiceSynthetize.startSynthesize(getApplicationContext(), "请输入正确的手机号码", false);
            ToastUtils.showShort("请输入正确的手机号码");
            return;
        }

        codeRepository.fetchCode(phone.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        startTimer();
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<String>() {
                    @Override
                    public void onNext(String code) {
                        BindPhoneActivity.this.codeNumer = code;
                        ToastUtils.showShort("获取验证码成功");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        countDownDisposable.dispose();
                        ToastUtils.showShort(throwable.getMessage());
                    }
                });

    }


    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(phone.getText().toString()) && TextUtils.isEmpty(code.getText().toString())) {
                next.setEnabled(false);
            } else {
                next.setEnabled(true);
            }

        }
    };

    private void startTimer() {
        countDownDisposable = RxUtils.rxCountDown(1, 60)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        sendCode.setEnabled(false);
                    }
                })
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        sendCode.setText("获取验证码");
                        sendCode.setEnabled(true);
                    }
                })
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        sendCode.setText("获取验证码");
                        sendCode.setEnabled(true);
                    }
                })
                .as(RxUtils.autoDisposeConverter(this))
                .subscribeWith(new DefaultObserver<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        sendCode.setText(
                                String.format(Locale.getDefault(), "已发送（%d）", integer));
                    }
                });
    }

    private void speak(String text) {
        MLVoiceSynthetize.startSynthesize(this, text, false);
    }
}
