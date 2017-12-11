package com.example.han.referralproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.han.referralproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectXuetangTimeActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.kongfu)
    LinearLayout kongfu;
    @BindView(R.id.one_hour)
    LinearLayout oneHour;
    @BindView(R.id.two_hour)
    LinearLayout twoHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_xuetang_time);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("选择测量时间");
        kongfu.setOnClickListener(this);
        oneHour.setOnClickListener(this);
        twoHour.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.one_hour:
                startActivity(new Intent(this,DetectActivity.class)
                .putExtra("time",1));
                break;
            case R.id.two_hour:
                startActivity(new Intent(this,DetectActivity.class)
                        .putExtra("time",2));
                break;
            case R.id.kongfu:
                startActivity(new Intent(this,DetectActivity.class)
                        .putExtra("time",0));
                break;
        }
    }
}
