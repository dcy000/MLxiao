package com.example.han.referralproject.health.intelligentdetection;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;

public class HealthIntelligentDetectionActivity extends BaseActivity {

    private boolean isFirstIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_activity_intelligent_detection);
        isFirstIn = true;
        if (isFirstIn) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            Fragment fragment = fm.findFragmentByTag(HealthFirstTipsFragment.class.getName());
            if (fragment != null) {
                transaction.show(fragment);
            } else {
                fragment = new HealthFirstTipsFragment();
                transaction.add(R.id.fl_container, fragment);
            }
            transaction.commitAllowingStateLoss();
            speak(R.string.health_first_detect_tips);
        } else {

        }
    }

    @Override
    protected void onActivitySpeakFinish() {

    }
}
