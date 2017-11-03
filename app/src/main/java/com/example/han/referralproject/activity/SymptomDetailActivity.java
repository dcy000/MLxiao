package com.example.han.referralproject.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.han.referralproject.R;
import com.example.han.referralproject.adapter.PagerViewAdapter;

import java.util.ArrayList;

public class SymptomDetailActivity extends BaseActivity {
    private RadioGroup mRadioGroup;
    private ViewPager mViewPager;
    private ArrayList<View> mContentList = new ArrayList<View>();
    private PagerViewAdapter mContentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_detail);
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_tab);
        mRadioGroup.setOnCheckedChangeListener(mCheckedChangeListener);
        mViewPager = (ViewPager) findViewById(R.id.vp_content);
        mContentAdapter = new PagerViewAdapter(mContentList);
        mViewPager.setAdapter(mContentAdapter);

    }

    private RadioGroup.OnCheckedChangeListener mCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId){
            }
        }
    };
}
