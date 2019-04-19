package com.gcml.module_health_profile.bracelet.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.gcml.common.recommend.fragment.RencommendForMarketFragment;
import com.gcml.common.utils.ui.UiUtils;
import com.gcml.common.widget.MLViewPager;
import com.gcml.common.widget.toolbar.ToolBarClickListener;
import com.gcml.common.widget.toolbar.TranslucentToolBar;
import com.gcml.module_health_profile.R;
import com.gcml.module_health_profile.bracelet.fragment.CorrelationNumber2Fragment;
import com.gcml.module_health_profile.bracelet.fragment.CorrelationNumberFragment;
import com.gcml.module_health_profile.bracelet.fragment.DeviceInfoFragment;
import com.gcml.module_health_profile.bracelet.fragment.ServiceHistoryFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lenovo on 2019/2/20.
 */

public class BraceletActivtity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup radioGroup;
    private MLViewPager viewPager;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bracelet);
        initView();
    }

    private void initView() {
        TranslucentToolBar title = findViewById(R.id.tb_bracelet);
        title.setData("我 的 设 备",
                R.drawable.common_btn_back, "返回",
                0, null, new ToolBarClickListener() {
                    @Override
                    public void onLeftClick() {
                        finish();
                    }

                    @Override
                    public void onRightClick() {

                    }
                });

        radioGroup = findViewById(R.id.rg_bracele);
        radioGroup.setOnCheckedChangeListener(this);
        viewPager = findViewById(R.id.vp_bracelet_info);
        onDataResult();
    }

    private void onDataResult() {
        List<String> menus = falseData();
        initRadioGroup(menus);
        initFragments(menus);
    }


    private void initFragments(List<String> data) {
        fragments = new ArrayList<>();
        int size = data.size();
        for (int i = 0; i < size; i++) {
            if (i == 0)
                fragments.add(DeviceInfoFragment.newInstance(null, null));
            if (i == 1)
                fragments.add(CorrelationNumber2Fragment.newInstance(null, null));
            if (i == 2)
                fragments.add(ServiceHistoryFragment.newInstance(null, null));

        }

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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

    private void initRadioGroup(List<String> data) {
        for (int i = 0; i < data.size(); i++) {
            RadioButton button = new RadioButton(this);
            button.setSingleLine(true);
        button.setEllipsize(TextUtils.TruncateAt.END);
        button.setTextSize(28);
        button.setText(data.get(i));
        button.setButtonDrawable(android.R.color.transparent);
        ViewCompat.setBackground(button, ResourcesCompat.getDrawable(getResources(), R.drawable.bg_rb_health_profile_menu, getTheme()));
        button.setTextColor(getResources().getColorStateList(R.color.health_profile_gray));

        Drawable drawableLeft = getResources().getDrawable(
                R.drawable.bg_rb_history_profile_shape);
        button.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                null, null, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, UiUtils.pt(160f));
        button.setGravity(Gravity.CENTER);
        radioGroup.addView(button, lp);
    }
        radioGroup.check(radioGroup.getChildAt(0).getId());
    }


    private List<String> falseData() {
        return Arrays.asList("设备信息", "关联号码", "服务历史");
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton childAt = (RadioButton) group.getChildAt(i);
            if (childAt.getId() == checkedId) {
                viewPager.setCurrentItem(i);
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
