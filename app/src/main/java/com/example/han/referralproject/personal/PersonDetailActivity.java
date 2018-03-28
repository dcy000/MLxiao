package com.example.han.referralproject.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonDetailActivity extends BaseActivity implements View.OnClickListener {


    List<Fragment> fragments = new ArrayList<>();
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("个人中心");
        mRightView.setImageResource(R.drawable.icon_wifi);
        mRightView.setOnClickListener(this);

        PersonDetailFragment detail = new PersonDetailFragment();
        PersonDetail2Fragment detail2 = new PersonDetail2Fragment();
        fragments.add(detail);
        fragments.add(detail2);

        vpContent.setAdapter(new PersonDetailFragmentPagerAdapter(getSupportFragmentManager(), fragments));
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, WifiConnectActivity.class));
    }
}
