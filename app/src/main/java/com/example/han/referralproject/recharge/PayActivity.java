package com.example.han.referralproject.recharge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;

public class PayActivity extends AppCompatActivity implements View.OnClickListener {

    public Button mButton1;
    public Button mButton2;
    public Button mButton3;

    public ImageView mImageView1;
    public ImageView mImageView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        mButton1 = (Button) findViewById(R.id.pay_1);
        mButton2 = (Button) findViewById(R.id.pay_2);
        mButton3 = (Button) findViewById(R.id.pay_3);

        mImageView1 = (ImageView) findViewById(R.id.icon_back);
        mImageView2 = (ImageView) findViewById(R.id.icon_home);

        mButton1.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);

        mImageView1.setOnClickListener(this);
        mImageView2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), PayInfoActivity.class);
        switch (view.getId()) {
            case R.id.pay_1:
                intent.putExtra("number", "1");
                startActivity(intent);
                break;
            case R.id.pay_2:
                intent.putExtra("number", "5000");

                startActivity(intent);

                break;
            case R.id.pay_3:
                intent.putExtra("number", "10000");

                startActivity(intent);

                break;
            case R.id.icon_back:
                finish();
                break;
            case R.id.icon_home:
                Intent intents = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intents);
                finish();
                break;
        }

    }
}
