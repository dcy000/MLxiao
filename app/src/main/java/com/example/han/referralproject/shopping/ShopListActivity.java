package com.example.han.referralproject.shopping;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;

public class ShopListActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout mLinearLayout1;
    LinearLayout mLinearLayout2;
    TextView mTextView1;
    TextView mTextView2;
    ImageView mImageView1;
    ImageView mImageView2;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_list);

        mLinearLayout1 = (LinearLayout) findViewById(R.id.linearlayout1);
        mLinearLayout2 = (LinearLayout) findViewById(R.id.linearlayout2);
        mTextView1 = (TextView) findViewById(R.id.healthy_equ);
        mTextView2 = (TextView) findViewById(R.id.healthy_con);

        mImageView1 = (ImageView) findViewById(R.id.line_1);
        mImageView2 = (ImageView) findViewById(R.id.line_2);


        mLinearLayout1.setOnClickListener(this);
        mLinearLayout2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearlayout1:

                mTextView1.setTextColor(Color.parseColor("#3E85FC"));
                mTextView2.setTextColor(Color.parseColor("#000000"));
                mImageView1.setVisibility(View.VISIBLE);
                mImageView2.setVisibility(View.GONE);
                break;
            case R.id.linearlayout2:
                mTextView1.setTextColor(Color.parseColor("#999999"));
                mTextView2.setTextColor(Color.parseColor("#000000"));
                mImageView1.setVisibility(View.GONE);
                mImageView2.setVisibility(View.VISIBLE);
                break;

        }
    }
}
