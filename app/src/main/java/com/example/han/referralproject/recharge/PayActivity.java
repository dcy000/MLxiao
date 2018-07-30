package com.example.han.referralproject.recharge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.homepage.MainActivity;

public class PayActivity extends BaseActivity implements View.OnClickListener {

    public Button mButton1;
    public Button mButton2;
    public Button mButton3;
    public Button mButton4;
    public Button mButton5;
    public Button mButton6;

    //public ImageView mImageView1;
    //public ImageView mImageView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        speak(getString(R.string.chongzhi));

        mToolbar.setVisibility(View.VISIBLE);

        mTitleText.setText(getString(R.string.pay));

        mButton1 = (Button) findViewById(R.id.pay_1);
        mButton2 = (Button) findViewById(R.id.pay_2);
        mButton3 = (Button) findViewById(R.id.pay_3);
        mButton4 = (Button) findViewById(R.id.pay_4);
        mButton5 = (Button) findViewById(R.id.pay_5);
        mButton6 = (Button) findViewById(R.id.pay_6);

       /* mImageView1 = (ImageView) findViewById(R.id.health_record_icon_back);
        mImageView2 = (ImageView) findViewById(R.id.health_record_icon_home);
*/
        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
        mButton6.setOnClickListener(this);

      /*  mImageView1.setOnClickListener(this);
        mImageView2.setOnClickListener(this);*/
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
            case R.id.pay_1:
                intent.putExtra("number", "5000");
                startActivity(intent);
                break;
            case R.id.pay_2:
                intent.putExtra("number", "10000");
                startActivity(intent);

                break;
            case R.id.pay_3:
                intent.putExtra("number", "20000");

                startActivity(intent);

                break;
            case R.id.pay_4:
                intent.putExtra("number", "50000");

                startActivity(intent);

                break;
            case R.id.pay_5:
                intent.putExtra("number", "100000");

                startActivity(intent);

                break;
            case R.id.pay_6:


                Intent inten = new Intent(getApplicationContext(), DefineActivity.class);
                startActivity(inten);

                break;
           /* case R.id.health_record_icon_back:
                finish();
                break;
            case R.id.health_record_icon_home:
                Intent intents = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intents);
                finish();
                break;*/
        }

    }
}
