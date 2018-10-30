package com.example.han.referralproject.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gzq on 2018/3/8.
 */

public class MessageDetailsActivity extends BaseActivity {
    @BindView(R.id.doctor)
    TextView doctor;
    @BindView(R.id.content)
    TextView content;
    private String doctorName;
    private String message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("消息中心");

        doctorName = getIntent().getStringExtra("doctorName");
        message = getIntent().getStringExtra("message");

        if (TextUtils.isEmpty(doctorName)){
            doctor.setText("某顾问");
        }else{
            doctor.setText(doctorName);
        }
        if (TextUtils.isEmpty(message)){
            content.setText("暂无内容");
        }else{
            content.setText(message);
        }
    }
}
