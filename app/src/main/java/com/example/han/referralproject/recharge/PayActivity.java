package com.example.han.referralproject.recharge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

public class PayActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivCount50;
    private ImageView ivCount100;
    private ImageView ivCount200;
    private ImageView ivCount500;
    private ImageView ivCount1000;
    private ImageView ivCountOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hq_activity_pay);
        speak(getString(R.string.chongzhi));
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText(getString(R.string.pay));
        ivCount50 = (ImageView) findViewById(R.id.hq_pay_iv_count_50);
        ivCount100 = (ImageView) findViewById(R.id.hq_pay_iv_count_100);
        ivCount200 = (ImageView) findViewById(R.id.hq_pay_iv_count_200);
        ivCount500 = (ImageView) findViewById(R.id.hq_pay_iv_count_500);
        ivCount1000 = (ImageView) findViewById(R.id.hq_pay_iv_count_1000);
        ivCountOther = (ImageView) findViewById(R.id.hq_pay_iv_count_other);
        ivCount50.setOnClickListener(this);
        ivCount100.setOnClickListener(this);
        ivCount200.setOnClickListener(this);
        ivCount500.setOnClickListener(this);
        ivCount1000.setOnClickListener(this);
        ivCountOther.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), PayInfoActivity.class);
        switch (view.getId()) {
            case R.id.hq_pay_iv_count_50:
                intent.putExtra("number", "5000");
                startActivity(intent);
                break;
            case R.id.hq_pay_iv_count_100:
                intent.putExtra("number", "10000");
                startActivity(intent);
                break;
            case R.id.hq_pay_iv_count_200:
                intent.putExtra("number", "20000");
                startActivity(intent);
                break;
            case R.id.hq_pay_iv_count_500:
                intent.putExtra("number", "50000");
                startActivity(intent);
                break;
            case R.id.hq_pay_iv_count_1000:
                intent.putExtra("number", "100000");
                startActivity(intent);
                break;
            case R.id.hq_pay_iv_count_other:
                Intent intent1 = new Intent(getApplicationContext(), DefineActivity.class);
                startActivity(intent1);
                break;
        }
    }
}
