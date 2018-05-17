package com.example.han.referralproject.yiyuan.adpater;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by lenovo on 2018/5/8.
 */

public class MainFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public MainFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int arg0) {
        if (mFragments == null) {
            return null;
        }
        return mFragments.get(arg0);
    }

    @Override
    public int getCount() {
        if (mFragments == null) {
            return 0;
        }
        return mFragments.size();
    }
}
