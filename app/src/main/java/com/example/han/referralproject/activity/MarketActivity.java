package com.example.han.referralproject.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.example.han.referralproject.market.network.GoodsRepository;
import com.example.han.referralproject.market.network.bean.GoodsTypeBean;
import com.example.han.referralproject.searchmaket.activity.SearchGoodsActivity;
import com.gcml.common.recommend.fragment.RencommendForMarketFragment;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.utils.ui.UiUtils;
import com.gcml.common.widget.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MarketActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private List<Fragment> fragments;
    private RadioGroup mRgMenu;
    private ViewPager mVpGoods;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("健康商城");
        speak("欢迎来到健康商城");
        mRightView.setImageResource(R.drawable.common_search_good);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MarketActivity.this, SearchGoodsActivity.class));
            }
        });
        mRgMenu = (RadioGroup) findViewById(R.id.rg_menu);
        mVpGoods = (ViewPager) findViewById(R.id.vp_goods);
        mRgMenu.setOnCheckedChangeListener(this);
        mVpGoods.setOffscreenPageLimit(1);
        setEnableListeningLoop(false);

        dialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();

        GoodsRepository repository = new GoodsRepository();
        repository.getGoodsTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> dialog.show())
                .doOnTerminate(() -> dialog.dismiss())
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(listApiResult -> {
                    initRadioGroup(listApiResult);
                    //初始化fragment
                    initFragments(listApiResult);
                }, Timber::e);


    }

    private void initFragments(List<GoodsTypeBean> data) {
        fragments = new ArrayList<>();
        fragments.add(new RencommendForMarketFragment());
        int size = data.size();
        for (int i = 0; i < size; i++) {
            fragments.add(GoodsFragment.newInstance(data.get(i).mallProductTypeId));
        }

        mVpGoods.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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

    /**
     * 商品列表
     *
     * @param data
     */
    private void initRadioGroup(List<GoodsTypeBean> data) {
        initFirstRadioButton();
        for (int i = 0; i < data.size(); i++) {
            RadioButton button = new RadioButton(this);
            button.setTextSize(28);
            button.setText(data.get(i).name + "");
            button.setButtonDrawable(android.R.color.transparent);
            ViewCompat.setBackground(button, ResourcesCompat.getDrawable(getResources(), R.drawable.bg_rb_history_record, getTheme()));
            button.setTextColor(getResources().getColorStateList(R.color.good_menu_text_color));

            Drawable drawableLeft = getResources().getDrawable(
                    R.drawable.bg_rb_history_record_shape);
            button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                    null, null, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UiUtils.pt(160f));
            button.setGravity(Gravity.CENTER);
            mRgMenu.addView(button, lp);
        }
        mRgMenu.check(mRgMenu.getChildAt(0).getId());
    }

    private void initFirstRadioButton() {
        RadioButton button = new RadioButton(this);
        button.setTextSize(28);
        button.setText("小E推荐");
        button.setButtonDrawable(android.R.color.transparent);
        ViewCompat.setBackground(button, ResourcesCompat.getDrawable(getResources(), R.drawable.bg_rb_history_record, getTheme()));
        button.setTextColor(getResources().getColorStateList(R.color.good_menu_text_color));

        Drawable drawableLeft = getResources().getDrawable(
                R.drawable.bg_rb_history_record_shape);
        button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                null, null, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UiUtils.pt(160f));
        button.setGravity(Gravity.CENTER);
        mRgMenu.addView(button, lp);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton childAt = (RadioButton) group.getChildAt(i);
            if (childAt.getId() == checkedId) {
                mVpGoods.setCurrentItem(i);
                childAt.setTextSize(32);
            }else {
                childAt.setTextSize(28);
            }
        }


    }
}
