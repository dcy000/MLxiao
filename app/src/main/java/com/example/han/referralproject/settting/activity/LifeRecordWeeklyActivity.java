package com.example.han.referralproject.settting.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.example.han.referralproject.settting.fragment.DietAdviceFragment;
import com.example.han.referralproject.settting.fragment.SportAdviceFragment;
import com.medlink.danbogh.utils.T;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LifeRecordWeeklyActivity extends BaseActivity {

    @BindView(R.id.rb_diet_advice)
    RadioButton rbDietAdvice;
    @BindView(R.id.rv_sport_advice)
    RadioButton rvSportAdvice;
    @BindView(R.id.rg)
    RadioGroup rg;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    private FragmentManager fm;
    private DietAdviceFragment diet;
    private SportAdviceFragment sport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_record_weekly);
        ButterKnife.bind(this);
        initTitle();
        initRB();
        initFragment();
    }

    private void initFragment() {
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        diet = new DietAdviceFragment();
        sport = new SportAdviceFragment();
        ft.add(R.id.fl_container, sport);
        ft.add(R.id.fl_container, diet);
        ft.commit();
    }


    private void initTitle() {
        mToolbar.setVisibility(View.VISIBLE);
        mTitleText.setText("周生活记录");
    }

    private void initRB() {
        ((RadioButton) rg.getChildAt(0)).setChecked(true);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_diet_advice:
                        showDietFrgment();
                        break;
                    case R.id.rv_sport_advice:
                        showSportFrgment();
                        break;
                }
            }
        });
    }

    private void showDietFrgment() {
        FragmentTransaction ft = fm.beginTransaction();
        hideAll(ft);
        if (diet == null) {
            diet = new DietAdviceFragment();
            ft.add(R.id.fl_container, diet).commit();
        } else {
            ft.show(diet).commit();
        }
    }

    private void showSportFrgment() {
        FragmentTransaction ft = fm.beginTransaction();
        hideAll(ft);
        if (sport == null) {
            sport = new SportAdviceFragment();
            ft.add(R.id.fl_container, sport).commit();
        } else {
            ft.show(sport).commit();
        }
    }


    private void hideAll(FragmentTransaction ft) {
        if (ft == null) {
            return;
        }

        if (sport != null) {
            ft.hide(sport);
        }

        if (diet != null) {
            ft.hide(diet);
        }
    }

}
