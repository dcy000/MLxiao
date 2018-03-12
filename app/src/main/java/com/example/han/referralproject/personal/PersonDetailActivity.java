package com.example.han.referralproject.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.activity.WifiConnectActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonDetailActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.fl_content)
    FrameLayout flContent;

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


        PersonDetailFragment fragment = new PersonDetailFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fl_content, fragment).commit();


    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, WifiConnectActivity.class));
    }
}
