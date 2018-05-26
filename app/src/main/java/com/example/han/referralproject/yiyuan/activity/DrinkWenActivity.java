package com.example.han.referralproject.yiyuan.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

public class DrinkWenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_wen);
        initTilte();
    }

    private void initTilte() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("问诊");
    }

}
