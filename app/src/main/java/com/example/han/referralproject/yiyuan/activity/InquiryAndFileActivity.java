package com.example.han.referralproject.yiyuan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InquiryAndFileActivity extends BaseActivity {

    @BindView(R.id.tv_inquiry)
    TextView tvInquiry;
    @BindView(R.id.tv_file)
    TextView tvFile;

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

    @OnClick({R.id.tv_inquiry, R.id.tv_file})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_inquiry:
                //问诊
                InquiryAndFileEndActivity.startMe(this,"问诊");
                break;
            case R.id.tv_file:
                //建档
                InquiryAndFileEndActivity.startMe(this,"建档");
                break;
        }
    }
}
