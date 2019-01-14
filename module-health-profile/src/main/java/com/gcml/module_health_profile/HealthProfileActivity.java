package com.gcml.module_health_profile;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gcml.common.utils.UtilsManager;
import com.gcml.common.utils.base.ToolbarBaseActivity;
import com.gcml.common.utils.ui.UiUtils;
import com.gcml.common.widget.dialog.LoadingDialog;
import com.gcml.module_health_profile.bean.HealthProfileMenuBean;
import com.gcml.module_health_profile.fragments.BloodSugarFollowupFragment;
import com.gcml.module_health_profile.fragments.BloodpressureFollowupFragment;
import com.gcml.module_health_profile.fragments.HealthCheckupFragment;
import com.gcml.module_health_profile.fragments.HealthFileFragment;
import com.gcml.module_health_profile.fragments.ZhongyiFollowupFragment;
import com.iflytek.synthetize.MLVoiceSynthetize;

import java.util.ArrayList;
import java.util.List;

public class HealthProfileActivity extends ToolbarBaseActivity implements RadioGroup.OnCheckedChangeListener {
    private List<Fragment> fragments;
    private RadioGroup mRgMenu;
    private ViewPager mVpGoods;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_profile);
        initView();
        initData();
    }


    private void initView() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("家 庭 医 生 服 务");
        MLVoiceSynthetize.startSynthesize(UtilsManager.getApplication(), "主人，欢迎来到家庭医生服务");
        mRgMenu = (RadioGroup) findViewById(R.id.rg_menu);
        mVpGoods = (ViewPager) findViewById(R.id.vp_goods);
        mRgMenu.setOnCheckedChangeListener(this);
        mVpGoods.setOffscreenPageLimit(1);
    }

    private void initData() {
        dialog = new LoadingDialog.Builder(this)
                .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();

//        HealthProfileRepository repository = new HealthProfileRepository();
//        repository.getMenu()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(disposable -> dialog.show())
//                .doOnTerminate(() -> dialog.dismiss())
//                .as(RxUtils.autoDisposeConverter(this))
//                .subscribe(listApiResult -> {
//                    initRadioGroup(listApiResult);
//                    //初始化fragment
//                    initFragments(listApiResult);
//                }, Timber::e);
        List<HealthProfileMenuBean> menuBeans = new ArrayList<>();
        HealthProfileMenuBean menu1 = new HealthProfileMenuBean();
        HealthProfileMenuBean menu2 = new HealthProfileMenuBean();
        HealthProfileMenuBean menu3 = new HealthProfileMenuBean();
        HealthProfileMenuBean menu4 = new HealthProfileMenuBean();
        HealthProfileMenuBean menu5 = new HealthProfileMenuBean();
        menu1.setName("健康档案");
        menu2.setName("健康体检");
        menu3.setName("高血压随访");
        menu4.setName("糖尿病随访");
        menu5.setName("中医体质辨识");
        menuBeans.add(menu1);
        menuBeans.add(menu2);
        menuBeans.add(menu3);
        menuBeans.add(menu4);
        menuBeans.add(menu5);
        initFragments(menuBeans);
        initRadioGroup(menuBeans);
    }

    private void initFragments(List<HealthProfileMenuBean> data) {
        fragments = new ArrayList<>();
        fragments.add(HealthFileFragment.instance());
        fragments.add(HealthCheckupFragment.instance());
        fragments.add(BloodpressureFollowupFragment.instance());
        fragments.add(BloodSugarFollowupFragment.instance());
        fragments.add(ZhongyiFollowupFragment.instance());
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
    private void initRadioGroup(List<HealthProfileMenuBean> data) {
        for (int i = 0; i < data.size(); i++) {
            RadioButton button = new RadioButton(this);
            button.setTextSize(28);
            button.setText(data.get(i).getName());
            button.setButtonDrawable(android.R.color.transparent);
            ViewCompat.setBackground(button, ResourcesCompat.getDrawable(getResources(), R.drawable.bg_rb_health_profile_menu, getTheme()));
            button.setTextColor(getResources().getColorStateList(R.color.health_profile_gray));

            Drawable drawableLeft = getResources().getDrawable(
                    R.drawable.bg_rb_history_profile_shape);
            button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                    null, null, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UiUtils.pt(160f));
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
                childAt.setTextSize(32);
                childAt.getPaint().setFakeBoldText(true);
                childAt.setTextColor(ContextCompat.getColor(this, R.color.health_profile_toolbar_bg));
            } else {
                childAt.setTextSize(28);
                childAt.getPaint().setFakeBoldText(false);
                childAt.setTextColor(ContextCompat.getColor(this, R.color.health_profile_gray));
            }
        }
    }
}
