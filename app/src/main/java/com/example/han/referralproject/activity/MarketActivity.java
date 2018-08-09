package com.example.han.referralproject.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.han.referralproject.R;
import com.example.han.referralproject.market.GoodsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarketActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_goods)
    ViewPager vpGoods;

    @BindView(R.id.rb_shanshi)
    RadioButton rbShanshi;
    @BindView(R.id.rb_yaopin)
    RadioButton rbYaopin;
    @BindView(R.id.rb_yiliaoshebei)
    RadioButton rbYiliaoshebei;
    @BindView(R.id.rb_baojianpin)
    RadioButton rbBaojianpin;
    @BindView(R.id.rb_liliao)
    RadioButton rbLiliao;
    @BindView(R.id.rg_health_goods)
    RadioGroup rgHealthGoods;

    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        ButterKnife.bind(this);
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健康商城");
        speak("主人，欢迎来到健康商城");

        rgHealthGoods.setOnCheckedChangeListener(this);
        vpGoods.addOnPageChangeListener(this);

        vpGoods.setOffscreenPageLimit(3);

        setEnableListeningLoop(false);
        initFragment();
        vpGoods.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments == null ? 0 : fragments.size();
            }

            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }
        });
        rgHealthGoods.check(R.id.rb_shanshi);

    }

    private void initFragment() {
        fragments = new ArrayList<>();

        fragments.add(GoodsFragment.newInstance(1));

//        for (int i = 0; i < 5; i++) {
//
//            switch (i) {
//                case 0:
//                    fragments.add(GoodsFragment.newInstance(4));
//                    break;
//                case 1:
//                    fragments.add(GoodsFragment.newInstance(2));
//                    break;
//                case 2:
//                    fragments.add(GoodsFragment.newInstance(1));
//                    break;
//                case 3:
//                    fragments.add(GoodsFragment.newInstance(3));
//                    break;
//                case 4:
//                    fragments.add(GoodsFragment.newInstance(5));
//                    break;
//            }
//
//        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_shanshi:
                vpGoods.setCurrentItem(0);
                break;
            case R.id.rb_yaopin:
                vpGoods.setCurrentItem(1);
                break;
            case R.id.rb_yiliaoshebei:
                vpGoods.setCurrentItem(2);
                break;
            case R.id.rb_baojianpin:
                vpGoods.setCurrentItem(3);
                break;
            case R.id.rb_liliao:
                vpGoods.setCurrentItem(4);
                break;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        switch (i) {
            case 0:
                rgHealthGoods.check(R.id.rb_shanshi);
                break;
            case 1:
                rgHealthGoods.check(R.id.rb_yaopin);
                break;
            case 2:
                rgHealthGoods.check(R.id.rb_yiliaoshebei);
                break;
            case 3:
                rgHealthGoods.check(R.id.rb_baojianpin);
                break;
            case 4:
                rgHealthGoods.check(R.id.rb_liliao);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
