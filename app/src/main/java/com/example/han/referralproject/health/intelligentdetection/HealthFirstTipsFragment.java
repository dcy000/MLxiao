package com.example.han.referralproject.health.intelligentdetection;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.activity.BaseActivity;
import com.gcml.module_blutooth_devices.base.BluetoothBaseFragment;

/**
 *
 */
public class HealthFirstTipsFragment extends BluetoothBaseFragment {


    public HealthFirstTipsFragment() {
        // Required empty public constructor
    }

    @Override
    protected int initLayout() {
        return R.layout.health_fragment_first_tips;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

    }


    @Override
    public void onResume() {
        super.onResume();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ((BaseActivity) activity).speak(getResources().getString(R.string.health_first_detect_tips));
            activity.getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (fragmentChanged!=null){
                        fragmentChanged.onFragmentChanged(HealthFirstTipsFragment.this,null);
                    }
                }
            }, 3000);
        }
    }

}
