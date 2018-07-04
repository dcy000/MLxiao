package com.example.han.referralproject.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.han.referralproject.R;
import com.example.han.referralproject.market.GoodsFragment;
import com.example.han.referralproject.network.NetworkApi;
import com.example.han.referralproject.util.Utils;
import com.example.han.referralproject.yisuotang.bean.GoodResultBean;
import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarketActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_goods)
    ViewPager vpGoods;

    //    @BindView(R.id.rb_shanshi)
//    RadioButton rbShanshi;
//    @BindView(R.id.rb_yaopin)
//    RadioButton rbYaopin;
//    @BindView(R.id.rb_yiliaoshebei)
//    RadioButton rbYiliaoshebei;
//    @BindView(R.id.rb_baojianpin)
//    RadioButton rbBaojianpin;
//    @BindView(R.id.rb_liliao)
//    RadioButton rbLiliao;
    @BindView(R.id.rg_health_goods)
    RadioGroup rgHealthGoods;

    private List<Fragment> fragments;
    private int checkedId;

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
        vpGoods.setOffscreenPageLimit(1);
        setEnableListeningLoop(false);
        getGoodType();
//        vpGoods.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public int getCount() {
//                return fragments == null ? 0 : fragments.size();
//            }
//
//            @Override
//            public Fragment getItem(int i) {
//                return fragments.get(i);
//            }
//        });
//        rgHealthGoods.check(R.id.rb_shanshi);

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

    public void getGoodType() {
        NetworkApi.getGoodType(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                if (response != null) {
                    GoodResultBean resultBean = new Gson().fromJson(response.body(), GoodResultBean.class);
                    if (resultBean != null) {
                        if (resultBean.tag) {
                            List<GoodResultBean.DataBean> data = resultBean.data;
                            if (data != null && data.size() != 0) {
                                initRadioGroup(data);

                                //初始化fragment
                                initFragments(data);
                                //初始化左侧栏目
                            }
                        } else {

                        }
                    }

                }
            }
        });

    }

    private void initRadioGroup(List<GoodResultBean.DataBean> data) {
        for (int i = 0; i < data.size(); i++) {
            RadioButton button = new RadioButton(this);
            button.setTextSize(28);
            button.setText(data.get(i).name + "");
            button.setButtonDrawable(android.R.color.transparent);
            ViewCompat.setBackground(button, ResourcesCompat.getDrawable(getResources(), R.drawable.bg_rb_history_record, getTheme()));
            button.setTextColor(getResources().getColorStateList(R.color.bg_rb_history_record));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utils.dp2px(this, 70f));
            button.setGravity(Gravity.CENTER);
            rgHealthGoods.addView(button, lp);
        }
        rgHealthGoods.check(rgHealthGoods.getChildAt(0).getId());
    }

    private void initFragments(List<GoodResultBean.DataBean> data) {
        fragments = new ArrayList<>();
        int size = data.size();
        for (int i = 0; i < size; i++) {
            fragments.add(GoodsFragment.newInstance(data.get(i).mallProductTypeId));
        }

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
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton childAt = (RadioButton) group.getChildAt(i);
            if (childAt.getId() == checkedId) {
                vpGoods.setCurrentItem(i);
            }
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
//        switch (i) {
//            case 0:
//                rgHealthGoods.check(R.id.rb_shanshi);
//                break;
//            case 1:
//                rgHealthGoods.check(R.id.rb_yaopin);
//                break;
//            case 2:
//                rgHealthGoods.check(R.id.rb_yiliaoshebei);
//                break;
//            case 3:
//                rgHealthGoods.check(R.id.rb_baojianpin);
//                break;
//            case 4:
//                rgHealthGoods.check(R.id.rb_liliao);
//                break;
//        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
