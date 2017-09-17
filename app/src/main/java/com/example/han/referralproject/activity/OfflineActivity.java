package com.example.han.referralproject.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.han.referralproject.R;

public class OfflineActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        findViewById(R.id.view_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_back:
                finish();
                break;
        }
    }
}
