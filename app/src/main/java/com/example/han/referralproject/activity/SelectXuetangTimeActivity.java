package com.example.han.referralproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
    @BindView(R.id.kongfu_img)
    ImageView kongfuImg;
    @BindView(R.id.onehour_img)
    ImageView onehourImg;
    @BindView(R.id.twohour_img)
    ImageView twohourImg;

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
        switch (v.getId()) {
            case R.id.one_hour:
                startActivity(new Intent(this, DetectActivity.class)
                        .putExtra("time", 1).putExtra("type", "xuetang"));
                break;
            case R.id.two_hour:
                startActivity(new Intent(this, DetectActivity.class)
                        .putExtra("time", 2).putExtra("type", "xuetang"));
                break;
            case R.id.kongfu:
                startActivity(new Intent(this, DetectActivity.class)
                        .putExtra("time", 0).putExtra("type", "xuetang"));
                break;
        }
    }
}
