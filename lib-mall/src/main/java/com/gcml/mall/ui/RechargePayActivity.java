package com.gcml.mall.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.gcml.mall.R;

public class RechargePayActivity extends AppCompatActivity {

    ImageView alipayQrcode, wxpayQrcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_pay);

        alipayQrcode = findViewById(R.id.iv_recharge_alipay);
        wxpayQrcode = findViewById(R.id.iv_recharge_wxpay);
    }

}
