package com.example.han.referralproject.yiyuan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.MainActivity;
import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.util.LocalShared;
import com.medlink.danbogh.call2.NimAccountHelper;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InquiryAndFileEndActivity extends BaseActivity {

    public static final String FROM_TAG = "tag";
    @BindView(R.id.tv_to_home_page)
    ImageView tvToHomePage;
    @BindView(R.id.tv_exit)
    ImageView tvExit;
    @BindView(R.id.textView9)
    TextView textView9;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_and_file_and);
        ButterKnife.bind(this);
        initTitle();
        speak("主人,您可以选择进入主页或退出账号");
    }

    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        title = getIntent().getStringExtra(FROM_TAG);
        mTitleText.setText(title);
        textView9.setText(title + "完成，请选择下一步操作");

        mLeftText.setVisibility(View.GONE);
        mLeftView.setVisibility(View.GONE);

        mRightText.setVisibility(View.GONE);
        mRightView.setVisibility(View.GONE);

    }

    /**
     * @param context
     * @param tag     title的名字
     */
    public static void startMe(Context context, String tag) {
        context.startActivity(new Intent(context, InquiryAndFileEndActivity.class).putExtra(FROM_TAG, tag));
    }


    @OnClick({R.id.tv_to_home_page, R.id.tv_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_to_home_page:
                startActivity(new Intent(this, MainActivity.class));
                if ("建档".equals(title)) {
                    finish();
                } else {
                    finishAffinity();
                }
                break;
            case R.id.tv_exit:
                tuichu();
                break;
        }
    }

    private void tuichu() {
        MobclickAgent.onProfileSignOff();
        NimAccountHelper.getInstance().logout();
        LocalShared.getInstance(this).loginOut();
        startActivity(new Intent(this, YiYuanLoginActivity.class));
        finishAffinity();
    }

    @Override
    protected void backLastActivity() {
        finishAffinity();
    }
}
