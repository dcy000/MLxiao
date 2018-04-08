package com.gzq.administrator.idcardreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by gzq on 2018/4/8.
 */

public class IdCardLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mBtnIdcard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard_login);
        initView();
    }

    private void initView() {
        mBtnIdcard = (Button) findViewById(R.id.btn_idcard);
        mBtnIdcard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_idcard:
                startActivity(new Intent(this,ReadIdCardActivity.class));
                break;
        }
    }
}
