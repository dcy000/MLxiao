package com.example.module_person.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class PersonDetailFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> data;

    public PersonDetailFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.data = list;
    }

    @Override
    public Fragment getItem(int arg0) {
        if (data.isEmpty()) {
            return null;
        }
        return data.get(arg0);//显示第几个页面
    }

    @Override
    public int getCount() {
        if (data.isEmpty()) {
            return 0;
        }
        return data.size();//有几个页面
    }
}