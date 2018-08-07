package com.example.han.referralproject.health.intelligentdetection;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.HashMap;

public class DataCacheFragment extends Fragment {

    public static DataCacheFragment get(FragmentManager fm) {
        Fragment fragment = fm.findFragmentByTag(DataCacheFragment.class.getName());
        if (fragment == null) {
            fragment = new DataCacheFragment();
            fm.beginTransaction()
                    .add(fragment, DataCacheFragment.class.getName())
                    .commitAllowingStateLoss();
        }
        return (DataCacheFragment) fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private final HashMap<String, Object> dataCache = new HashMap<>();

    public HashMap<String, Object> getDataCache() {
        return dataCache;
    }
}
