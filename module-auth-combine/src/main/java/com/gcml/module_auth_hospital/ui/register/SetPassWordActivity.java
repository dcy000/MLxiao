package com.gcml.module_auth_hospital.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.gcml.module_auth_hospital.wrap.NumeriKeypadLayout;
import com.gcml.module_auth_hospital.wrap.NumeriKeypadLayoutHelper;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

public class SetPassWordActivity extends ToolbarBaseActivity implements View.OnClickListener {


    private TextView tvNext;
    private EditText etPsw;
    private TranslucentToolBar translucentToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowToolbar = false;
        setContentView(R.layout.activity_set_pass_word);
        initView();
        useNumberKeyPad();
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
            toBindPhone();
        }
    }


    private void toBindPhone() {
        String passWord = etPsw.getText().toString().trim();
        if (TextUtils.isEmpty(passWord)) {
            speak("请输入6位数字密码");
            return;
        }
        if (passWord.length() < 6) {
            speak("请输入6位数字密码");
            return;
        }
        startActivity(new Intent(this, BindPhoneActivity.class)
                .putExtra("passWord", passWord)
                .putExtras(getIntent()));
    }

    private void speak(String content) {
        MLVoiceSynthetize.startSynthesize(this, content);
    }

    public void onTextChange(Editable phone) {
        if (TextUtils.isEmpty(etPsw.getText().toString())) {
            tvNext.setEnabled(false);
        } else {
            tvNext.setEnabled(true);
        }
    }


    private NumeriKeypadLayoutHelper layoutHelper;
    private NumeriKeypadLayoutHelper.Builder builder;

    private void useNumberKeyPad() {
        hideKeyboard(etPsw);
        etPsw.requestFocus();
        NumeriKeypadLayout numeriKeypadLayout = findViewById(R.id.imageView2);
        builder = new NumeriKeypadLayoutHelper.Builder()
                .layout(numeriKeypadLayout)
                .showX(false)
                .textChageListener(text -> {
                    if (etPsw.isFocused()) {
                        etPsw.setText(text);
                    }
                });
        layoutHelper = builder.build();
        etPsw.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                layoutHelper.setLayoutText(etPsw.getText().toString());
                layoutHelper.setLayoutinputLength(6);
            }
        });

        layoutHelper.showX(false);
    }

}
