package com.example.han.referralproject.intelligent_diagnosis;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.han.referralproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2018/3/11.
 */

public class LifeRecordWeeklyFragment extends Fragment {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_life_record_weekly, container, false);
        ButterKnife.bind(this, view);
        initTitle();
        initRB();
        initFragment();
        return view;
    }

    private void initFragment() {
        fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        diet = new DietAdviceFragment();
        sport = new SportAdviceFragment();
        ft.add(R.id.fl_container, sport);
        ft.add(R.id.fl_container, diet);
        ft.hide(sport);
        ft.commit();
    }


    private void initTitle() {
//        mToolbar.setVisibility(View.VISIBLE);
//        mTitleText.setText("周生活记录");
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
