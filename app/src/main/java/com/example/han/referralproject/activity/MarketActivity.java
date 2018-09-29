package com.example.han.referralproject.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import com.gcml.common.recommend.adapter.RecommendAdapter;
import com.gcml.common.recommend.bean.get.GoodBean;
import com.gcml.common.repository.http.ApiResult;
import com.gcml.common.repository.utils.DefaultObserver;
import com.gcml.common.utils.RxUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.lib_utils.ui.UiUtils;
import com.linheimx.app.library.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MarketActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    private List<Fragment> fragments;
    private TranslucentToolBar mTbMarket;
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
        speak("主人，欢迎来到健康商城");
        mRightView.setImageResource(R.drawable.common_search_good);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MarketActivity.this, SearchGoodsActivity.class));
            }
        });
        mRgMenu = (RadioGroup) findViewById(R.id.rg_menu);
        mVpGoods = (ViewPager) findViewById(R.id.vp_goods);
//        mTbMarket.setData("健 康 商 城", R.drawable.common_icon_back, "返回", R.drawable.common_search_good, null, new ToolBarClickListener() {
//            @Override
//            public void onLeftClick() {
//                finish();
//            }
//
//            @Override
//            public void onRightClick() {
//                startActivity(new Intent(MarketActivity.this, SearchGoodsActivity.class));
//            }
//        });

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
                });


    }

    private void initFragments(List<GoodsTypeBean> data) {
        fragments = new ArrayList<>();
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
        for (int i = 0; i < data.size(); i++) {
            RadioButton button = new RadioButton(this);
            button.setTextSize(28);
            button.setText(data.get(i).name + "");
            button.setButtonDrawable(android.R.color.transparent);
            ViewCompat.setBackground(button, ResourcesCompat.getDrawable(getResources(), R.drawable.bg_rb_history_record, getTheme()));
            button.setTextColor(getResources().getColorStateList(R.color.good_menu_text_color));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UiUtils.pt(70f));
            button.setGravity(Gravity.CENTER);
            mRgMenu.addView(button, lp);
        }
        mRgMenu.check(mRgMenu.getChildAt(0).getId());
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton childAt = (RadioButton) group.getChildAt(i);
            if (childAt.getId() == checkedId) {
                mVpGoods.setCurrentItem(i);
            }
        }

    }
}
