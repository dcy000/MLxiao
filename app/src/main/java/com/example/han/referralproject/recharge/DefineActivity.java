package com.example.han.referralproject.recharge;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

public class DefineActivity extends BaseActivity {


    EditText mEditText;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define);

        mToolbar.setVisibility(View.VISIBLE);

        mTitleText.setText(getString(R.string.pay));

        speak(getString(R.string.chongzhi_define));


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();


        mButton = (Button) findViewById(R.id.deifine_pay);

        mEditText = (EditText) findViewById(R.id.deifine_mount);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mEditText.getText().toString())) {
                    float value = Float.parseFloat(mEditText.getText().toString());
                    if (value > 5000) {
                        Toast.makeText(getApplicationContext(), "充值金额最大为5000元", Toast.LENGTH_SHORT).show();
                    } else if (value <= 0.0f) {
                        Toast.makeText(getApplicationContext(), "充值金额必须大于为0元", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), PayInfoActivity.class);
                        intent.putExtra("number", (int) (value * 100) + "");
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请输入金额", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    /**
     * 返回上一页
     */
    protected void backLastActivity() {
        finish();
    }

    /**
     * 返回到主页面
     */
    protected void backMainActivity() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }


    public void init() {
        mEditText = (EditText) findViewById(R.id.deifine_mount);
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString("请输入充值金额");
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(45, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        mEditText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

}
