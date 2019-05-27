package com.gcml.module_auth_hospital.ui.findPassWord;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.Utils;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.display.ToastUtils;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SetPassWord2Activity extends ToolbarBaseActivity implements View.OnClickListener {


    private TextView tvNext;
    private EditText etPsw;
    private TranslucentToolBar translucentToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowToolbar = false;
        setContentView(R.layout.activity_set_pass_word2);
        initView();
    }

    private void initView() {
        translucentToolBar = findViewById(R.id.tb_set_pass_word2);
        tvNext = (TextView) findViewById(R.id.tv_next);
        etPsw = findViewById(R.id.et_psw);
        etPsw.addTextChangedListener(new TextWatcher() {
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
        tvNext.setOnClickListener(this);

        translucentToolBar.setData("密 码 设 置",
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
            setPassWord();
        }
    }

    private CodeRepository codeRepository = new CodeRepository();

    private void setPassWord() {
        String passWord = etPsw.getText().toString().trim();
        if (TextUtils.isEmpty(passWord)) {
            speak("请输入6位数字密码");
            return;
        }

        Intent data = getIntent();
        if (data == null) {
            return;
        }
        String phoneNumber = data.getStringExtra("phoneNumber");
        codeRepository.updatePassword(phoneNumber, passWord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DefaultObserver<Object>() {
                    @Override
                    public void onNext(Object o) {
                        Routerfit.register(AppRouter.class).skipUserLogins2Activity();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtils.showShort("设置密码失败");
                    }
                });

    }

    private void speak(String content) {
        MLVoiceSynthetize.startSynthesize(this, content);
    }

    public void onTextChange(Editable phone) {
        if (TextUtils.isEmpty(phone.toString()) && Utils.checkIdCard1(phone.toString())) {
            tvNext.setEnabled(false);
        } else {
            tvNext.setEnabled(true);
        }
    }

}