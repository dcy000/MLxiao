package com.example.han.referralproject.health_manager_program;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.han.referralproject.R;
import com.example.han.referralproject.intelligent_diagnosis.IChangToolbar;
import com.gcml.module_health_record.fragments.HealthRecordBloodpressureFragment;
import com.gzq.administrator.lib_common.base.BaseFragment;

public class LastWeekTrendFragment extends BaseFragment {
    private FrameLayout mLastweekTrendFl;
    private TextView mConclusion;
    private HealthRecordBloodpressureFragment bloodpressureFragment;
    private IChangToolbar iChangToolbar;

    public void setOnChangToolbar(IChangToolbar iChangToolbar) {
        this.iChangToolbar = iChangToolbar;
    }
    @Override
    protected int initLayout() {
        return R.layout.fragment_lastweek_trend;
    }

    @Override
    protected void initView(View view, Bundle bundle) {

        mLastweekTrendFl = (FrameLayout) view.findViewById(R.id.lastweek_trend_fl);
        mConclusion = (TextView) view.findViewById(R.id.conclusion);

        bloodpressureFragment = new HealthRecordBloodpressureFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.lastweek_trend_fl, bloodpressureFragment).commit();

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (iChangToolbar != null) {
                iChangToolbar.onChange(this);
            }
        }
    }
}
