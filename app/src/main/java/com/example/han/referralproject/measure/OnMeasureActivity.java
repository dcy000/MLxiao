package com.example.han.referralproject.measure;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.measure.fragment.SanheyiFragment;
import com.example.han.referralproject.measure.fragment.TiwenFragment;
import com.example.han.referralproject.measure.fragment.TizhongFragment;
import com.example.han.referralproject.measure.fragment.XindianFragment;
import com.example.han.referralproject.measure.fragment.XuetangFragment;
import com.example.han.referralproject.measure.fragment.XueyaFragment;
import com.example.han.referralproject.measure.fragment.XueyangFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnMeasureActivity extends BaseActivity {
    @BindView(R.id.container)
    FrameLayout container;
    private String type;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_measure);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        type = getIntent().getStringExtra("type");
        initFragment();
        initBluetooth();
    }

    private void initBluetooth() {

    }

    private void initFragment() {
        switch (type) {
            case "wendu":
                fragment = new TiwenFragment();
                mTitleText.setText("体温测量");
                break;
            case "xueya":
                fragment = new XueyaFragment();
                mTitleText.setText("血压测量");
                break;
            case "xuetang":
                fragment = new XuetangFragment();
                mTitleText.setText("血糖测量");
                break;
            case "xueyang":
                fragment = new XueyangFragment();
                mTitleText.setText("血氧测量");
                break;
            case "xindian":
                fragment = new XindianFragment();
                mTitleText.setText("心电测量");
                break;
            case "tizhong":
                fragment = new TizhongFragment();
                mTitleText.setText("体重测量");
                break;
            case "sanheyi":
                fragment = new SanheyiFragment();
                mTitleText.setText("三合一测量");
                break;
        }
        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
    }
}
