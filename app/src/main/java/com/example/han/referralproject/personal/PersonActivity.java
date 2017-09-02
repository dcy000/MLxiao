package com.example.han.referralproject.personal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.LoginActivity;
import com.example.han.referralproject.activity.RecordActivity;
import com.example.han.referralproject.util.LocalShared;

public class PersonActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        findViewById(R.id.btn_record).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_record:
                startActivity(new Intent(this, RecordActivity.class));
                break;
            case R.id.btn_logout:
                LocalShared.getInstance(this).loginOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }
}
