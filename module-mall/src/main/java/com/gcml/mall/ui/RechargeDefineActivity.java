package com.gcml.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.billy.cc.core.component.CC;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.mall.R;

public class RechargeDefineActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mEditText;
    Button mButton;
    TranslucentToolBar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_define);

        bindView();
        bindData();
    }

    private void bindView() {
        mToolBar = findViewById(R.id.tb_recharge_define);
        mEditText = findViewById(R.id.deifine_mount);
        mButton = findViewById(R.id.deifine_pay);
        mButton.setOnClickListener(this);
    }

    private void bindData() {
        mToolBar.setData(getString(R.string.recharge_define), R.drawable.common_btn_back, "返回", R.drawable.common_btn_home, "", new ToolBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                CC.obtainBuilder("app").setActionName("ToMainActivity").build().callAsync();
                finish();
            }
        });

        mEditText = findViewById(R.id.deifine_mount);
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString("请输入充值金额");
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(45, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        mEditText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.deifine_pay) {
            if (!TextUtils.isEmpty(mEditText.getText().toString())) {
                float value = Float.parseFloat(mEditText.getText().toString());
                if (value > 5000) {
                    Toast.makeText(getApplicationContext(), "充值金额最大为5000元", Toast.LENGTH_SHORT).show();
                } else if (value <= 0.0f) {
                    Toast.makeText(getApplicationContext(), "充值金额必须大于为0元", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), RechargeQrcodeActivity.class);
                    intent.putExtra("billMoney", (int) (value * 100));
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "请输入金额", Toast.LENGTH_SHORT).show();
            }
        } else {

        }
    }
}
