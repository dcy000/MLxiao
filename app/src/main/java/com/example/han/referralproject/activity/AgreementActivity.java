package com.example.han.referralproject.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;

public class AgreementActivity extends BaseActivity implements View.OnClickListener {

    public ImageView mImageView;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        findViewById(R.id.btn_sure).setOnClickListener(this);
        findViewById(R.id.icon_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                finish();
                break;
            case R.id.icon_back:
                finish();
                break;
        }
    }
}
