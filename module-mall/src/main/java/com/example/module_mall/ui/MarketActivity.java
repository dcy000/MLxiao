package com.example.module_mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.module_mall.R;
import com.example.module_mall.R2;
import com.gcml.lib_widget.ToolbarBaseActivity;
import com.gzq.lib_core.base.ui.BasePresenter;
import com.gzq.lib_core.base.ui.IPresenter;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MarketActivity extends ToolbarBaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    @BindView(R2.id.vp_goods)
    ViewPager vpGoods;

    @BindView(R2.id.rb_shanshi)
    RadioButton rbShanshi;
    @BindView(R2.id.rb_yaopin)
    RadioButton rbYaopin;
    @BindView(R2.id.rb_yiliaoshebei)
    RadioButton rbYiliaoshebei;
    @BindView(R2.id.rb_baojianpin)
    RadioButton rbBaojianpin;
    @BindView(R2.id.rb_liliao)
    RadioButton rbLiliao;
    @BindView(R2.id.rg_health_goods)
    RadioGroup rgHealthGoods;

    private List<Fragment> fragments;


    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_market;
    }

    @Override
    public void initParams(Intent intentArgument) {
        MLVoiceSynthetize.startSynthesize("主人，欢迎来到健康商城");
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        mTitleText.setText("健 康 商 城");

        rgHealthGoods.setOnCheckedChangeListener(this);
        vpGoods.addOnPageChangeListener(this);

        vpGoods.setOffscreenPageLimit(3);

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

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {
        };
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
        if (checkedId == R.id.rb_shanshi) {
            vpGoods.setCurrentItem(0);

        } else if (checkedId == R.id.rb_yaopin) {
            vpGoods.setCurrentItem(1);

        } else if (checkedId == R.id.rb_yiliaoshebei) {
            vpGoods.setCurrentItem(2);

        } else if (checkedId == R.id.rb_baojianpin) {
            vpGoods.setCurrentItem(3);

        } else if (checkedId == R.id.rb_liliao) {
            vpGoods.setCurrentItem(4);

        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if (i == 0) {
            rgHealthGoods.check(R.id.rb_shanshi);

        } else if (i == 1) {
            rgHealthGoods.check(R.id.rb_yaopin);

        } else if (i == 2) {
            rgHealthGoods.check(R.id.rb_yiliaoshebei);

        } else if (i == 3) {
            rgHealthGoods.check(R.id.rb_baojianpin);

        } else if (i == 4) {
            rgHealthGoods.check(R.id.rb_liliao);

        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
