package com.gcml.module_auth_hospital.ui.findPassWord;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

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
import com.gcml.module_auth_hospital.wrap.NumeriKeypadLayout;
import com.gcml.module_auth_hospital.wrap.NumeriKeypadLayoutHelper;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FindPassWordActivity extends ToolbarBaseActivity {

    private TextView notice;
    private TextView next;
    private TextView sendCode;
    private EditText phone;
    private EditText code;
    private TranslucentToolBar tb;
    private String codeNumer = "";

    public static void startMe(Context context) {
        Intent intent = new Intent(context, FindPassWordActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowToolbar = false;
        setContentView(R.layout.activity_find_password);
        initView();
    }


    private void initView() {
        notice = findViewById(R.id.tv_notice);
        tb = findViewById(R.id.tb_find_password);
        phone = findViewById(R.id.et_phone);
        code = findViewById(R.id.et_code);
        next = findViewById(R.id.tv_next);
        next.setOnClickListener(new FilterClickListener(v -> {
            toSetPassWord();
        }));
        sendCode = findViewById(R.id.tv_send_code);
        tb.setData("忘 记 密 码",
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
        useNumberKeyPad();
    }

    private NumeriKeypadLayoutHelper layoutHelper;
    private NumeriKeypadLayoutHelper.Builder builder;
    private void useNumberKeyPad() {
        hideKeyboard(phone);
        hideKeyboard(code);

        NumeriKeypadLayout numeriKeypadLayout = findViewById(R.id.nk_numberkey_pad);
        builder = new NumeriKeypadLayoutHelper.Builder()
                .layout(numeriKeypadLayout)
                .showX(true)
                .textChageListener(text -> {
                    if (phone.isFocused()) {
                        phone.setText(text);
                    } else if (code.isFocused()) {
                        code.setText(text);
                    }
                    layoutHelper = builder.newBuilder(builder.clearAll(false)).build();
                });
        layoutHelper = builder.build();

        phone.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                layoutHelper.setLayoutText(phone.getText().toString());
                layoutHelper.setLayoutinputLength(11);
                layoutHelper.showX(false);
            }
        });
        code.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                layoutHelper.setLayoutText(code.getText().toString());
                layoutHelper.setLayoutinputLength(6);
                layoutHelper.showX(false);
            }
        });
    }

    private void toSetPassWord() {
        if (TextUtils.isEmpty(code.getText().toString())) {
            ToastUtils.showShort("请输入验证码");
            return;
        }
        if (!this.codeNumer.equals(code.getText().toString())) {
            ToastUtils.showShort("验证码错误");
            layoutHelper = builder.newBuilder(builder.clearAll(true)).build();
            return;
        }
        startActivity(new Intent(this, SetPassWord2Activity.class)
                .putExtra("phoneNumber", phone.getText().toString()));
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
                        FindPassWordActivity.this.codeNumer = code;
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
}
