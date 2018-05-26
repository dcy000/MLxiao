package com.example.han.referralproject.yiyuan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InquiryAndFileActivity extends BaseActivity {
    @BindView(R.id.iv_wenzhen)
    ImageButton ivWenzhen;
    @BindView(R.id.iv_jiandang)
    ImageButton ivJiandang;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textView6)
    TextView textView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_and_file);
        ButterKnife.bind(this);
        initTitle();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("问诊建档");
    }

    @OnClick({R.id.iv_wenzhen, R.id.iv_jiandang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_wenzhen:
                InquiryAndFileEndActivity.startMe(this,"问诊");
                break;
            case R.id.iv_jiandang:
                InquiryAndFileEndActivity.startMe(this,"建档");
                break;
        }
    }

}
