package com.example.han.referralproject.health.intelligentdetection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class DataFragment extends Fragment {

    public static DataFragment get(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(DataFragment.class.getName());
        if (fragment == null) {
            fragment = new DataFragment();
            fm.beginTransaction()
                    .add(fragment, DataFragment.class.getName())
                    .commitAllowingStateLoss();
        }
        return (DataFragment) fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
