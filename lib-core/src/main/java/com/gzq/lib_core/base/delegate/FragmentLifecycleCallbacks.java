package com.gzq.lib_core.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

public class FragmentLifecycleCallbacks extends FragmentManager.FragmentLifecycleCallbacks{
    private FragmentLifecycle fragmentLifecycle;
    @Override
    public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
        super.onFragmentAttached(fm, f, context);
        if (fragmentLifecycle==null){
            fragmentLifecycle=new FragmentDetegate(context,f);
        }
        fragmentLifecycle.onAttach(context);
    }

    @Override
    public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        super.onFragmentCreated(fm, f, savedInstanceState);
        if (fragmentLifecycle!=null){
            fragmentLifecycle.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState);
        if (fragmentLifecycle!=null){
            fragmentLifecycle.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        super.onFragmentActivityCreated(fm, f, savedInstanceState);
        if (fragmentLifecycle!=null){
            fragmentLifecycle.onActivityCreate(savedInstanceState);
        }
    }

    @Override
    public void onFragmentStarted(FragmentManager fm, Fragment f) {
        super.onFragmentStarted(fm, f);
        if (fragmentLifecycle!=null){
            fragmentLifecycle.onStart();
        }
    }

    @Override
    public void onFragmentResumed(FragmentManager fm, Fragment f) {
        super.onFragmentResumed(fm, f);
        if (fragmentLifecycle!=null){
            fragmentLifecycle.onResume();
        }
    }

    @Override
    public void onFragmentPaused(FragmentManager fm, Fragment f) {
        super.onFragmentPaused(fm, f);
        if (fragmentLifecycle!=null){
            fragmentLifecycle.onPause();
        }
    }

    @Override
    public void onFragmentStopped(FragmentManager fm, Fragment f) {
        super.onFragmentStopped(fm, f);
        if (fragmentLifecycle!=null){
            fragmentLifecycle.onStop();
        }
    }

    @Override
    public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
        super.onFragmentSaveInstanceState(fm, f, outState);
        if (fragmentLifecycle!=null){
            fragmentLifecycle.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
        super.onFragmentViewDestroyed(fm, f);
        if (fragmentLifecycle!=null){
            fragmentLifecycle.onDestroyView();
        }
    }

    @Override
    public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
        super.onFragmentDestroyed(fm, f);
        if (fragmentLifecycle!=null){
            fragmentLifecycle.onDestroy(f);
            fragmentLifecycle=null;
        }
    }

    @Override
    public void onFragmentDetached(FragmentManager fm, Fragment f) {
        super.onFragmentDetached(fm, f);
        if (fragmentLifecycle!=null){
            fragmentLifecycle.onDetach();
        }
    }
}
