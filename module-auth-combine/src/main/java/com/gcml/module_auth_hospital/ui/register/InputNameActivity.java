package com.gcml.module_auth_hospital.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.gcml.common.router.AppRouter;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_auth_hospital.R;
import com.iflytek.synthetize.MLVoiceSynthetize;
import com.sjtu.yifei.route.Routerfit;

public class InputNameActivity extends ToolbarBaseActivity {

    private TranslucentToolBar tb;
    private EditText etName;
    private TextView tvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowToolbar = false;
        setContentView(R.layout.activity_input);
        initView();
    }

    private void initView() {
        tb = findViewById(R.id.tb_input_name);
        etName = findViewById(R.id.et_name);
        tvNext = findViewById(R.id.tv_next);

        tb.setData("身 份 证 号 码 注 册",
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


        tvNext.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                MLVoiceSynthetize.startSynthesize(this.getApplicationContext(), "请输入姓名");
                return;
            }
            startActivity(new Intent(this, SetPassWordActivity.class)
                    .putExtra("registerName", name)
                    .putExtras(getIntent()));

        });

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(etName.getText().toString().trim())) {
                    tvNext.setEnabled(false);
                } else {
                    tvNext.setEnabled(true);
                }
            }
        });
    }
}
