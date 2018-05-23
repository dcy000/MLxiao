package com.example.han.referralproject.yisuotang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.MarketActivity;
import com.example.han.referralproject.video.VideoListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HealthMallActivity extends BaseActivity {

    @BindView(R.id.iv_lecture_hall)
    ImageView ivLectureHall;
    @BindView(R.id.iv_lecture_care)
    ImageView ivLectureCare;
    @BindView(R.id.iv_yisuo_mall)
    ImageView ivYisuoMall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_mall);
        ButterKnife.bind(this);
        initTitle();
        speak("主人,欢迎来到健康商城");
    }


    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健康商城");
    }


    @OnClick({R.id.iv_lecture_hall, R.id.iv_lecture_care, R.id.iv_yisuo_mall})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_lecture_hall:
                startActivity(new Intent(this, VideoListActivity.class));
                break;
            case R.id.iv_lecture_care:
                startActivity(new Intent(this, MarketActivity.class));
                break;
            case R.id.iv_yisuo_mall:
                // TODO: 2018/5/23 他们的商城
                break;
        }
    }
}
