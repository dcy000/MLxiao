package com.ml.bci.game.common.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by afirez on 2018/6/29.
 */

public class FragmentUtils {

    public static void show(FragmentManager fm, int id, Class<? extends Fragment> clazz) {
        Fragment fragment = getInstance(fm, id, clazz);
        if (fragment.isHidden()) {
            fm.beginTransaction().show(fragment).commitNowAllowingStateLoss();
        }
    }

    public static Fragment getInstance(FragmentManager fm, int id, Class<? extends Fragment> clazz) {
        return findOrAddFragment(fm, id, clazz);
    }

    public static <T extends Fragment> T get(FragmentManager fm, Class<T> clazz) {
        return (T) findOrAddFragment(fm, clazz);
    }

    public static Fragment findOrAddFragment(FragmentManager fm, Class<? extends Fragment> clazz) {
        String tag = clazz.getCanonicalName();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = newInstance(clazz);
            fm.beginTransaction()
                    .add(fragment, tag)
                    .commitNowAllowingStateLoss();
        }
        return fragment;
    }

    public static Fragment findOrAddFragment(FragmentManager fm, int id, Class<? extends Fragment> clazz) {
        String tag = clazz.getCanonicalName();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = newInstance(clazz);
            fm.beginTransaction()
                    .add(id, fragment, tag)
                    .commitNowAllowingStateLoss();
        }
        return fragment;
    }

    private static Fragment newInstance(Class<? extends Fragment> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
