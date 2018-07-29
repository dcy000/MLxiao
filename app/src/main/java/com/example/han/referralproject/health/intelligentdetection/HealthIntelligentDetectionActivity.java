package com.example.han.referralproject.health.intelligentdetection;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

public class HealthIntelligentDetectionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_activity_intelligent_detection);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment fragment = fm.findFragmentByTag(HealthFirstTipsFragment.class.getName());
        if (fragment != null && fragment.isHidden()) {
            transaction.show(fragment);
        } else {
            fragment = new HealthFirstTipsFragment();
            transaction.add(R.id.fl_container, fragment);
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    protected void onActivitySpeakFinish() {
        if (HealthFirstTipsFragment.speakTips) {
            HealthFirstTipsFragment.speakTips = false;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment fragment = fm.findFragmentByTag(HealthBloodDetectionUiFragment.class.getName());
            if (fragment != null) {
                transaction.show(fragment);
            } else {
                fragment = new HealthBloodDetectionUiFragment();
                transaction.add(R.id.fl_container, fragment);
            }
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    protected void onResume() {
        setEnableListeningLoop(false);
        super.onResume();
    }
}
