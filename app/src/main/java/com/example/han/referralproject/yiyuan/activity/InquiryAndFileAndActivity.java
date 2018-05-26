package com.example.han.referralproject.yiyuan.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InquiryAndFileAndActivity extends BaseActivity {

    public static final String FROM_TAG = "tag";
    @BindView(R.id.tv_to_home_page)
    TextView tvToHomePage;
    @BindView(R.id.tv_exit)
    TextView tvExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_and_file_and);
        ButterKnife.bind(this);
        initTitle();
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        String title = getIntent().getStringExtra(FROM_TAG);
        mTitleText.setText(title);

    }

    /**
     * @param context
     * @param tag     title的名字
     */
    private static void starMe(Context context, String tag) {
        context.startActivity(new Intent(context, InquiryAndFileAndActivity.class).putExtra(FROM_TAG, tag));
    }


    @OnClick({R.id.tv_to_home_page, R.id.tv_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_to_home_page:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.tv_exit:
                startActivity(new Intent(this, YiYuanLoginActivity.class));
                break;
        }
    }
}
